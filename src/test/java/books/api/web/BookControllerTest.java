package books.api.web;

import books.api.domain.Book;
import books.api.dto.BookRequest;
import books.api.dto.BookResponse;
import books.api.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("BookController - Integration Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService service;

    private BookRequest bookRequest;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
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
    @DisplayName("GET /api/v1/books - Deve retornar lista paginada de livros")
    void shouldReturnPagedListOfBooks() throws Exception {
        // Arrange
        Page<BookResponse> page = new PageImpl<>(List.of(bookResponse), PageRequest.of(0, 10), 1);
        when(service.list(isNull(), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.content[0].author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.content[0].price").value(120.00))
                .andExpect(jsonPath("$.content[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(service).list(isNull(), any());
    }

    @Test
    @DisplayName("GET /api/v1/books?q=clean - Deve buscar livros por titulo")
    void shouldSearchBooksByTitle() throws Exception {
        // Arrange
        Page<BookResponse> page = new PageImpl<>(List.of(bookResponse), PageRequest.of(0, 10), 1);
        when(service.list(eq("clean"), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books")
                        .param("q", "clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"));

        verify(service).list(eq("clean"), any());
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} - Deve retornar livro por ID")
    void shouldReturnBookById() throws Exception {
        // Arrange
        when(service.get(1L)).thenReturn(bookResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        verify(service).get(1L);
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} - Deve retornar 404 quando livro nao existir")
    void shouldReturn404WhenBookNotFound() throws Exception {
        // Arrange
        when(service.get(999L)).thenThrow(new ResponseStatusException(NOT_FOUND));

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(service).get(999L);
    }

    @Test
    @DisplayName("POST /api/v1/books - Deve criar novo livro com sucesso")
    void shouldCreateBookSuccessfully() throws Exception {
        // Arrange
        when(service.create(any(BookRequest.class))).thenReturn(bookResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        verify(service).create(any(BookRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/books - Deve retornar 400 quando titulo for vazio")
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        // Arrange
        BookRequest invalidRequest = new BookRequest(
                "",
                "Robert C. Martin",
                new BigDecimal("120.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    @DisplayName("POST /api/v1/books - Deve retornar 400 quando autor for vazio")
    void shouldReturn400WhenAuthorIsBlank() throws Exception {
        // Arrange
        BookRequest invalidRequest = new BookRequest(
                "Clean Code",
                "",
                new BigDecimal("120.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    @DisplayName("POST /api/v1/books - Deve retornar 400 quando preco for negativo")
    void shouldReturn400WhenPriceIsNegative() throws Exception {
        // Arrange
        BookRequest invalidRequest = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                new BigDecimal("-10.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    @DisplayName("PUT /api/v1/books/{id} - Deve atualizar livro com sucesso")
    void shouldUpdateBookSuccessfully() throws Exception {
        // Arrange
        when(service.update(eq(1L), any(BookRequest.class))).thenReturn(bookResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"));

        verify(service).update(eq(1L), any(BookRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/books/{id} - Deve retornar 404 quando livro nao existir")
    void shouldReturn404WhenUpdatingNonExistentBook() throws Exception {
        // Arrange
        when(service.update(eq(999L), any(BookRequest.class)))
                .thenThrow(new ResponseStatusException(NOT_FOUND));

        // Act & Assert
        mockMvc.perform(put("/api/v1/books/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound());

        verify(service).update(eq(999L), any(BookRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/books/{id} - Deve retornar 400 com dados invalidos")
    void shouldReturn400WhenUpdatingWithInvalidData() throws Exception {
        // Arrange
        BookRequest invalidRequest = new BookRequest(
                "",
                "",
                new BigDecimal("-10.00"),
                LocalDate.of(2008, 8, 1),
                Book.Status.AVAILABLE
        );

        // Act & Assert
        mockMvc.perform(put("/api/v1/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).update(anyLong(), any());
    }

    @Test
    @DisplayName("DELETE /api/v1/books/{id} - Deve deletar livro com sucesso")
    void shouldDeleteBookSuccessfully() throws Exception {
        // Arrange
        doNothing().when(service).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/books/{id} - Deve retornar 404 quando livro nao existir")
    void shouldReturn404WhenDeletingNonExistentBook() throws Exception {
        // Arrange
        doThrow(new ResponseStatusException(NOT_FOUND)).when(service).delete(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/books/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(service).delete(999L);
    }

    @Test
    @DisplayName("GET /api/v1/books - Deve respeitar parametros de paginacao")
    void shouldRespectPaginationParameters() throws Exception {
        // Arrange
        Page<BookResponse> page = new PageImpl<>(
                List.of(bookResponse),
                PageRequest.of(1, 5),
                20
        );
        when(service.list(isNull(), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(20))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(1));

        verify(service).list(isNull(), any());
    }
}
