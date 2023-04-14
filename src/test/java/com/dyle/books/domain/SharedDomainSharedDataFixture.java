package com.dyle.books.domain;

import com.dyle.books.commons.Codes.BookTypes;
import com.dyle.books.commons.Codes.Publisher;
import com.dyle.books.entity.Books;
import com.dyle.books.entity.Books.BookInventory;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Djordje Radomirovic
 */
public final class SharedDomainSharedDataFixture {

    public static final String STANDARD_SHARED_BEFORE_SCRIPT = "/fixture/shared/domain/shared_before.sql";

    public static final String STANDARD_SHARED_AFTER_SCRIPT = "/fixture/shared/domain/shared_after.sql";

    private SharedDomainSharedDataFixture() {
    }

    public static Books standardBooks() {
        final Books books = ReflectionUtils.newInstance(Books.class);
        ReflectionTestUtils.setField(books, "id", 701L);
        ReflectionTestUtils.setField(books, "bookDetails", standardBook());
        ReflectionTestUtils.setField(books, "version", 0L);
        assertThat(books).hasNoNullFieldsOrProperties();

        return books;
    }

    //public static Map<Publisher, Map<BookTypes, BookInventory>> standardBook() {
    public static Map<String, Publisher> standardBook() {
        return Map.of("Testing", Publisher.PUBLISHER_1);
        //return Map.of(Publisher.PUBLISHER_1,
        //        Map.of(BookTypes.FICTION,
        //                new BookInventory(new BookInventory.Inventory(10, 1),
        //                        Map.of(501L, new BookInventory.Inventory(1, 1)))));
    }
}
