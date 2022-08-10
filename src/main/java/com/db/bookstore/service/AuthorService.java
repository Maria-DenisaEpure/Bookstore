package com.db.bookstore.service;

import com.db.bookstore.model.Author;
import com.db.bookstore.model.Book;
import com.db.bookstore.model.User;
import com.db.bookstore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
	@Autowired
	AuthorRepository authorRepository;
	public List<Author> findAll() {
		return authorRepository.findAll();
	}

	public Author findById(int id) {
		Author author = authorRepository.findById(id);
		return author;
	}
}
