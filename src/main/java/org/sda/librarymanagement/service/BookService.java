package org.sda.librarymanagement.service;

import java.util.List;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.repository.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getOneBookById(@PathVariable Long id) {
		return bookRepository.getOne(id);
	}

	public void saveBook(@RequestBody Book book) {
		bookRepository.saveAndFlush(book);
	}

	public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
		Book existingBook = bookRepository.getOne(id);
		BeanUtils.copyProperties(book, existingBook);
		return bookRepository.saveAndFlush(existingBook);
	}

	public Book deleteBook(@PathVariable Long id) {
		Book existingBook = bookRepository.getOne(id);
		bookRepository.delete(existingBook);
		return existingBook;
	}
}