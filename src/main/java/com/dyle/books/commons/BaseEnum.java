package com.dyle.books.commons;

import com.fasterxml.jackson.annotation.JsonValue;

public interface BaseEnum {

    @JsonValue
    String getValue();
}
