package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
@Transactional
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookCategoryService bookCategoryService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<Book> getAllBooks() {
		return (List<Book>) bookRepository.findAll();
	}

	@Transactional
	public Book getOneBookById(@PathVariable Long id) {
		return entityManager.find(Book.class, id);
	}
	
	@Transactional
	public Book getOneBookByName(@PathVariable String bookName) {
		return (Book) entityManager.createQuery("SELECT b FROM Book as b WHERE b.bookName LIKE :name")
				.setParameter("name", bookName)
				.getSingleResult();
	}

	@Transactional
	public List<Book> getBooksByIds(List<Long> ids) {
		return (List<Book>) bookRepository.findAllById(ids);
	}

	@Transactional
	public void saveBook(@RequestBody Book book) {
		bookRepository.save(book);
	}

	@Transactional
	public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
		Book existingBook = entityManager.find(Book.class, id);
		BeanUtils.copyProperties(book, existingBook);
		return bookRepository.save(existingBook);
	}

	@Transactional
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
		book.setBorrowingTypeAtHome(bookDTO.isBorrowingTypeAtHome());
		book.setBookCategories(
				(List<BookCategory>) bookCategoryService.getBookCategoriesByIds(bookDTO.getBookCategories()));
		return book;

	}
}