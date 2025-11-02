package books.api.service;

import books.api.dto.BookRequest;
import books.api.dto.BookResponse;
import books.api.mapper.BookMapper;
import books.api.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repo;
    private final BookMapper mapper;


    public Page<BookResponse> list(String q, Pageable pageable){
        var page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByTitleContainingIgnoreCase(q, pageable);
        return page.map(mapper::toResponse);
    }


    public BookResponse get(Long id){
        var e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.toResponse(e);
    }


    public BookResponse create(@Valid BookRequest r){
        var e = mapper.toEntity(r);
        return mapper.toResponse(repo.save(e));
    }


    public BookResponse update(Long id, @Valid BookRequest r){
        var e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapper.update(e, r);
        return mapper.toResponse(repo.save(e));
    }


    public void delete(Long id){
        if(!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
    }
}