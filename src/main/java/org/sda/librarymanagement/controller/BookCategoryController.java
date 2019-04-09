package org.sda.librarymanagement.controller;

import org.sda.librarymanagement.service.BookCategoryService;

import java.util.List;

import org.sda.librarymanagement.entity.BookCategory;
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
public class BookCategoryController {

	@Autowired
	private BookCategoryService bookCategoryService;

	@PostMapping("/bookCategory")
	public ResponseEntity<Void> addBookCategory(@RequestBody BookCategory bookCategory, UriComponentsBuilder builder) {
		bookCategoryService.saveBookCategory(bookCategory);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/bookCategory").buildAndExpand(bookCategory.getCategoryId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/bookCategory/{id}")
	public ResponseEntity<BookCategory> getBookCategoryById(@PathVariable("id") Long id) {
		BookCategory bookCategory = bookCategoryService.getOneBookCategoryById(id);
		return new ResponseEntity<BookCategory>(bookCategory, HttpStatus.OK);
	}

	@GetMapping("/bookCategories")
	public ResponseEntity<List<BookCategory>> getAllBookCategories() {
		List<BookCategory> bookCategories = bookCategoryService.getAllBookCategories();
		return new ResponseEntity<List<BookCategory>>(bookCategories, HttpStatus.OK);
	}

	@PutMapping("/bookCategory/{id}")
	public ResponseEntity<BookCategory> updateBookCategory(@PathVariable Long id,
			@RequestBody BookCategory bookCategory) {
		bookCategoryService.updateBookCategory(id, bookCategory);
		return new ResponseEntity<BookCategory>(bookCategory, HttpStatus.OK);
	}

	@DeleteMapping("/bookCategory/{id}")
	public ResponseEntity<Void> deleteBookCategory(@PathVariable("id") Long id) {
		bookCategoryService.deleteBookCategory(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
