package com.dyle.books.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class AuditSupport {

    public static final ZoneId UTC = ZoneId.of("UTC");

    private AuditSupport() {
    }

    public static ZonedDateTime getNow() {
        return ZonedDateTime.now(UTC);
    }

    // todo: revisit when security approach is defined
    public static String getCurrentUsername() {
        return null;
    }
}
