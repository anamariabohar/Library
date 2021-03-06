package org.sda.librarymanagement.controller;

import java.util.List;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.dto.BookDTO;
import org.sda.librarymanagement.service.BookCategoryService;
import org.sda.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/library")
public class BookController {
	@Autowired
	private BookService bookService;

	@Autowired
	private BookCategoryService bookCategoryService;

	@PostMapping("/book")
	public ResponseEntity<Void> addBook(@RequestBody BookDTO bookDTO, UriComponentsBuilder builder) {
		Book book = bookService.convertFromDTOToEntity(bookDTO);
		bookService.saveBook(book);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/book").buildAndExpand(book.getBookId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/book/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
		Book book = bookService.getOneBookById(id);
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	@GetMapping("/books")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> book = (List<Book>) bookService.getAllBooks();
		return new ResponseEntity<List<Book>>(book, HttpStatus.OK);
	}

	@PutMapping("/book/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
		Book book = bookService.convertFromDTOToEntity(bookDTO);
		bookService.updateBook(id, book);
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}

	@DeleteMapping("/book/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
		bookService.deleteBook(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
