package com.db.bookstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

//@Entity
@NoArgsConstructor
@Getter
@Setter
public class NewBook {
	private String title;
	private int[] authorIds;
	private int pages;
	private String publisher;
}
