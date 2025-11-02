package books.api.service;

import books.api.domain.Book;
import books.api.dto.BookRequest;
import books.api.dto.BookResponse;
import books.api.mapper.BookMapper;
import books.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService - Unit Tests")
class BookServiceTest {

    @Mock
    private BookRepository repo;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookService service;

    private Book book;
    private BookRequest bookRequest;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .price(new BigDecimal("120.00"))
                .publishedAt(LocalDate.of(2008, 8, 1))
                .status(Book.Status.AVAILABLE)
                .build();

        bookRequest = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("120.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );

        bookResponse = new BookResponse(
                1L,
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("120.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );
    }

    @Test
    @DisplayName("Deve listar todos os livros com paginacao")
    void shouldListAllBooksWithPagination() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(repo.findAll(pageable)).thenReturn(bookPage);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        // Act
        Page<BookResponse> result = service.list(null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).title());

        verify(repo).findAll(pageable);
        verify(mapper).toResponse(book);
    }

    @Test
    @DisplayName("Deve buscar livros por titulo")
    void shouldSearchBooksByTitle() {
        // Arrange
        String query = "clean";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(repo.findByTitleContainingIgnoreCase(query, pageable)).thenReturn(bookPage);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        // Act
        Page<BookResponse> result = service.list(query, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).title());

        verify(repo).findByTitleContainingIgnoreCase(query, pageable);
        verify(mapper).toResponse(book);
    }

    @Test
    @DisplayName("Deve buscar livro por ID com sucesso")
    void shouldGetBookByIdSuccessfully() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.of(book));
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.get(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Clean Code", result.title());

        verify(repo).findById(1L);
        verify(mapper).toResponse(book);
    }

    @Test
    @DisplayName("Deve lancar excecao quando livro nao for encontrado por ID")
    void shouldThrowExceptionWhenBookNotFoundById() {
        // Arrange
        when(repo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.get(999L));

        verify(repo).findById(999L);
        verify(mapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve criar novo livro com sucesso")
    void shouldCreateBookSuccessfully() {
        // Arrange
        when(mapper.toEntity(bookRequest)).thenReturn(book);
        when(repo.save(book)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.create(bookRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.title());
        assertEquals("Robert C. Martin", result.author());

        verify(mapper).toEntity(bookRequest);
        verify(repo).save(book);
        verify(mapper).toResponse(book);
    }

    @Test
    @DisplayName("Deve atualizar livro existente com sucesso")
    void shouldUpdateBookSuccessfully() {
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(mapper).update(book, bookRequest);
        when(repo.save(book)).thenReturn(book);
        when(mapper.toResponse(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.update(1L, bookRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());

        verify(repo).findById(1L);
        verify(mapper).update(book, bookRequest);
        verify(repo).save(book);
        verify(mapper).toResponse(book);
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar atualizar livro inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentBook() {
        // Arrange
        when(repo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.update(999L, bookRequest));

        verify(repo).findById(999L);
        verify(mapper, never()).update(any(), any());
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar livro com sucesso")
    void shouldDeleteBookSuccessfully() {
        // Arrange
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> service.delete(1L));

        // Assert
        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao ao tentar deletar livro inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        // Arrange
        when(repo.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> service.delete(999L));

        verify(repo).existsById(999L);
        verify(repo, never()).deleteById(anyLong());
    }
}
