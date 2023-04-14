package com.dyle.books.domain;

import com.dyle.books.commons.DateUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.time.ZonedDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Embeddable
public class AuditMetadata {

    private ZonedDateTime createdDate;

    private String createdBy;

    private ZonedDateTime lastModifiedDate;

    private String lastModifiedBy;

    public static AuditMetadata create() {
        final ZonedDateTime now = AuditSupport.getNow();
        final String currentUsername = AuditSupport.getCurrentUsername();

        return new AuditMetadata(now, currentUsername, now, currentUsername);
    }

    public AuditMetadata update() {
        final ZonedDateTime now = AuditSupport.getNow();
        final String currentUsername = AuditSupport.getCurrentUsername();

        return new AuditMetadata(this.getCreatedDate(), this.getCreatedBy(), now, currentUsername);
    }

    public boolean isEqual(AuditMetadata other) {
        return (this == other) || (other != null
                && DateUtils.isEqual(createdDate, other.getCreatedDate())
                && Objects.equals(createdBy, other.getCreatedBy())
                && DateUtils.isEqual(lastModifiedDate, other.getLastModifiedDate())
                && Objects.equals(lastModifiedBy, other.getLastModifiedBy()));
    }
}
