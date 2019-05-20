package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BookCategory;
import org.sda.librarymanagement.entity.dto.BookDTO;
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

	@Autowired
	private BookCategoryService bookCategoryService;

	@Autowired
	private EntityManager entityManager;

	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book getOneBookById(@PathVariable Long id) {
		return entityManager.find(Book.class, id);
	}

	public Iterable<Book> getBooksByIds(List<Long> ids) {
		return bookRepository.findAllById(ids);
	}

	public void saveBook(@RequestBody Book book) {
		bookRepository.save(book);
	}

	public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
		Book existingBook = entityManager.find(Book.class, id);
		BeanUtils.copyProperties(book, existingBook);
		return bookRepository.save(existingBook);
	}

	public Book deleteBook(@PathVariable Long id) {
		Book existingBook = entityManager.find(Book.class, id);
		bookRepository.delete(existingBook);
		return existingBook;
	}

	public Book convertFromDTOToEntity(BookDTO bookDTO) {
		Book book = new Book();
		book.setBookId(bookDTO.getBookId());
		book.setBookName(bookDTO.getBookName());
		book.setAuthorName(bookDTO.getAuthorName());
		book.setBorrowingPeriod(bookDTO.getBorrowingPeriod());
		book.setBorrowingTypeAtHome(bookDTO.isBorrowingTypeAtHome());
		book.setBookCategories(
				(List<BookCategory>) bookCategoryService.getBookCategoriesByIds(bookDTO.getBookCategories()));
		return book;

	}
}