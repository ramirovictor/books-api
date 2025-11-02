package books.api.dto;
import books.api.domain.Book;

import java.math.BigDecimal;
import java.time.LocalDate;


public record BookResponse(
        Long id,
        String title,
        String author,
        BigDecimal price,
        LocalDate publishedAt,
        Book.Status status
) {}