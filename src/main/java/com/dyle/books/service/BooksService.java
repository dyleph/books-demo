package com.dyle.books.service;

import com.dyle.books.commons.Codes;
import com.dyle.books.commons.Codes.Publisher;
import com.dyle.books.config.ResilienceConfiguration;
import com.dyle.books.entity.Books;
import com.dyle.books.repository.BooksRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class BooksService {
    private final BooksRepository booksRepository;

    @Retry(name = ResilienceConfiguration.DATA_ACCESS_RETRY_NAME)
    public void save(Long id, Map<Publisher, Map<Codes.BookTypes, Books.BookInventory>> bookDetails) {
        Assert.notNull(id, "id must not be null");


        booksRepository.findById(id).ifPresentOrElse(s -> {
            s.update(bookDetails);
            booksRepository.save(s);
        }, () -> booksRepository.save(new Books(id, bookDetails)));
    }

    @Retry(name = ResilienceConfiguration.DATA_ACCESS_RETRY_NAME)
    public void purge(Long id) {
        Assert.notNull(id, "id must not be null");

        final Books book = booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with id %d does not exist".formatted(id)));

        booksRepository.delete(book);
    }
}
