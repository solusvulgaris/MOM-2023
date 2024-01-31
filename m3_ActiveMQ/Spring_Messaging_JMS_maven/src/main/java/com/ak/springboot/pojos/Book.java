package com.ak.springboot.pojos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {

    @JsonCreator
    public Book(
            @JsonProperty("bookId") String bookId,
            @JsonProperty("title") String title) {
        this.bookId = bookId;
        this.title = title;
    }

    private final String bookId;
    private final String title;

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }
}
