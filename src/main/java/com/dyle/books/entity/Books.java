package com.dyle.books.entity;

import com.dyle.books.commons.Codes.BookTypes;
import com.dyle.books.commons.Codes.Publisher;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "sh_books")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Books {
    @Id
    private Long id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    //private Map<Publisher, Map<BookTypes, BookInventory>> bookDetails;
    private Map<String, Publisher> bookDetails;

    @Version
    private Long version;

    //public Books(Long id, Map<Publisher, Map<BookTypes, BookInventory>> bookDetails) {
    public Books(Long id, Map<String, Publisher> bookDetails) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(bookDetails, "books must not be null");

        this.id = id;
        this.bookDetails = bookDetails;
    }

    //public void update(Map<Publisher, Map<BookTypes, BookInventory>> bookDetails) {
    public void update(Map<String, Publisher> bookDetails) {
        Assert.notNull(bookDetails, "books must not be null");

        if (this.bookDetails.equals(bookDetails)) {
            return;
        }

        this.bookDetails = bookDetails;
    }

    public record BookInventory(Inventory global, Map<Long, Inventory> info) {

        public BookInventory {
            Assert.notNull(info, "listings must be provided");
        }

        public record Inventory(Integer maxItems, Integer remainingItems) {
        }
    }
}
