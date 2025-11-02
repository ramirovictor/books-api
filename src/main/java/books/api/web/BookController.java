package books.api.web;

import books.api.dto.BookRequest;
import books.api.dto.BookResponse;
import books.api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {


    private final BookService service;


    @GetMapping
    public Page<BookResponse> list(@RequestParam(required = false) String q,
                                   @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return service.list(q, pageable);
    }


    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id){
        return service.get(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@RequestBody @Valid BookRequest body){
        return service.create(body);
    }


    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @RequestBody @Valid BookRequest body){
        return service.update(id, body);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}