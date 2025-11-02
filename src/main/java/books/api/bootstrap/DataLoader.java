package books.api.bootstrap;


import books.api.domain.Book;
import books.api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {


    private final BookRepository repo;


    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            repo.saveAll(List.of(
                    Book.builder().title("Clean Code").author("Robert C. Martin")
                            .price(new BigDecimal("120"))
                            .publishedAt(LocalDate.of(2008, 8, 1))
                            .status(Book.Status.AVAILABLE).build(),
                    Book.builder().title("Refactoring").author("Martin Fowler")
                            .price(new BigDecimal("150"))
                            .publishedAt(LocalDate.of(2018, 11, 19))
                            .status(Book.Status.AVAILABLE).build()
            ));
        }
    }
}