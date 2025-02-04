package com.library.bible.book.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book {
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookPublisher;
    private String bookReleaseDate;
    private String bookCategory;
    private Long bookPrice;
    private String bookImgPath="";
    private String bookDetail;
    private Long bookTotalStock;
    private Long bookRentStock=0L;
    private String bookLocation;
    private String bookQrPath="";
}
