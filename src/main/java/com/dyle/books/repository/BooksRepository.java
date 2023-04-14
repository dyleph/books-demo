package com.dyle.books.repository;

import com.dyle.books.entity.Books;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BooksRepository extends Repository<Books, Long> {
    Optional<Books> findById(Long id);

    Books save(Books book);

    void delete(Books book);
}
