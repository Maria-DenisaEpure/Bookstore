package com.db.bookstore.controller;

import com.db.bookstore.model.Author;
import com.db.bookstore.model.Book;
import com.db.bookstore.model.NewBook;
import com.db.bookstore.model.User;
import com.db.bookstore.service.AuthorService;
import com.db.bookstore.service.BookService;
import com.db.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	BookService bookService;
	@Autowired
	AuthorService authorService;

	@GetMapping("/register")
	public ModelAndView getRegisterForm() {
		ModelAndView modelAndView = new ModelAndView("register-form");
		return modelAndView;
	}

	@PostMapping("/register")
	public ModelAndView addUser(User user){
		user.setRole("client");
		userService.insertUser(user);
		ModelAndView modelAndView = new ModelAndView("redirect:/login");
		return modelAndView;
	}

	@GetMapping("/login")
	public ModelAndView getLoginForm(){
		ModelAndView modelAndView = new ModelAndView("login-form");
		return modelAndView;
	}

	@PostMapping("/login")
	public ModelAndView verifyUser(User user, HttpServletResponse response){
		try {
			User user1 = userService.findByUsernameOrEmailAndPassword(user);
			response.addCookie(new Cookie("id", "" + user1.getId()));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ModelAndView modelAndView = new ModelAndView("redirect:/dashboard");
		return modelAndView;
	}

	@GetMapping("/dashboard")
	public ModelAndView getDashboard(@CookieValue("id") int cookie) {
		User user = userService.findById(cookie);
		ModelAndView modelAndView = new ModelAndView("dashboard");
		modelAndView.addObject("user", user);
		List<Book> bookList = bookService.findAll();
		modelAndView.addObject("bookList", bookList);
		return modelAndView;
	}

	@GetMapping("/add-book")
	public ModelAndView getBookForm(@CookieValue("id") int cookie) {
		User user = userService.findById(cookie);
		if(userService.checkIfAdmin(user)) {
			ModelAndView modelAndView = new ModelAndView("book-form");
			List<Author> authors = authorService.findAll();
			modelAndView.addObject("authors", authors);
			return modelAndView;
		}
		return new ModelAndView("error-client");
	}

	@PostMapping("/add-book")
	public ModelAndView addNewBook(NewBook newBook) {
		Book book = new Book();
		book.setPages(newBook.getPages());
		book.setPublisher(newBook.getPublisher());
		book.setTitle(newBook.getTitle());
		book.setAuthorList(new HashSet<>());
		for (int id : newBook.getAuthorIds()) {
			Author author = authorService.findById(id);
			book.getAuthorList().add(author);
		}
		bookService.insertBook(book);
		ModelAndView modelAndView = new ModelAndView("added-book");
		return modelAndView;
	}

}
