package books.api.dto;

import books.api.domain.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;


public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @PositiveOrZero BigDecimal price,
        LocalDate publishedAt,
        Book.Status status
) {}