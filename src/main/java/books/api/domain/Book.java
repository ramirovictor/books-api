package books.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String title;


    @NotBlank
    private String author;


    @PositiveOrZero
    private BigDecimal price;


    private LocalDate publishedAt;


    @Enumerated(EnumType.STRING)
    private Status status; // AVAILABLE, SOLD_OUT


    public enum Status { AVAILABLE, SOLD_OUT }
}