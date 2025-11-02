package books.api.mapper;

import books.api.domain.Book;
import books.api.dto.BookRequest;
import books.api.dto.BookResponse;
import org.springframework.stereotype.Component;





@Component
public class BookMapper {
    public Book toEntity(BookRequest r) {
        return Book.builder()
                .title(r.title())
                .author(r.author())
                .price(r.price())
                .publishedAt(r.publishedAt())
                .status(r.status())
                .build();
    }


    public void update(Book e, BookRequest r) {
        e.setTitle(r.title());
        e.setAuthor(r.author());
        e.setPrice(r.price());
        e.setPublishedAt(r.publishedAt());
        e.setStatus(r.status());
    }


    public BookResponse toResponse(Book e) {
        return new BookResponse(
                e.getId(),
                e.getTitle(),
                e.getAuthor(),
                e.getPrice(),
                e.getPublishedAt(),
                e.getStatus()
        );
    }
}