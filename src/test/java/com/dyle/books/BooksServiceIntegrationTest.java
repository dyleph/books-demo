package com.dyle.books;

import com.dyle.books.domain.SharedDomainSharedDataFixture;
import com.dyle.books.entity.Books;
import com.dyle.books.repository.BooksRepository;
import com.dyle.books.service.BooksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.time.ZonedDateTime;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = NONE)
@Sql(scripts = SharedDomainSharedDataFixture.STANDARD_SHARED_BEFORE_SCRIPT, executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = SharedDomainSharedDataFixture.STANDARD_SHARED_AFTER_SCRIPT, executionPhase = AFTER_TEST_METHOD)
public class BooksServiceIntegrationTest {
    @Autowired
    private BooksService booksService;

    @Autowired
    private BooksRepository booksRepository;

    @Test
    @Sql(statements = "DELETE FROM sh_books WHERE id = 702", executionPhase = AFTER_TEST_METHOD)
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    void save_whenDoesNotExist_shouldCreate() {
        booksService.save(702L, SharedDomainSharedDataFixture.standardBook());

        final Books book = booksRepository.findById(702L).orElseThrow();

        assertThat(book).usingComparatorForType(Comparator.comparing(ZonedDateTime::toInstant), ZonedDateTime.class)
                .usingRecursiveComparison()
                .ignoringFields("id", "version")
                .isEqualTo(SharedDomainSharedDataFixture.standardBooks());
        assertThat(book.getId()).isEqualTo(702L);
        assertThat(book.getVersion()).isZero();
    }
}
