package com.y2o2u2n.demo.web;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
