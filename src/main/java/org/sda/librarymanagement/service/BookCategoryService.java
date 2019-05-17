package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BookCategory;
import org.sda.librarymanagement.entity.dto.BookCategoryDTO;
import org.sda.librarymanagement.repository.BookCategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BookCategoryService {

	@Autowired
	private BookCategoryRepository bookCategoryRepository;

	@Autowired
	private BookService bookService;

	@Autowired
	private EntityManager entityManager;

	public Iterable<BookCategory> getAllBookCategories() {
		return bookCategoryRepository.findAll();
	}

	public Iterable<BookCategory> getBookCategoriesByIds(List<Long> ids) {
		return bookCategoryRepository.findAllById(ids);
	}

	public BookCategory getOneBookCategoryById(@PathVariable Long id) {
		return entityManager.find(BookCategory.class, id);
	}

	public void saveBookCategory(BookCategory bookCategory) {
		bookCategoryRepository.save(bookCategory);
	}

	public BookCategory updateBookCategory(@PathVariable Long id, @RequestBody BookCategory bookCategory) {
		BookCategory existingBookCategory = entityManager.find(BookCategory.class, id);
		BeanUtils.copyProperties(bookCategory, existingBookCategory);
		return bookCategoryRepository.save(existingBookCategory);
	}

	public BookCategory deleteBookCategory(@PathVariable Long id) {
		BookCategory existingBookCategory = entityManager.find(BookCategory.class, id);
		bookCategoryRepository.delete(existingBookCategory);
		return existingBookCategory;
	}

	public BookCategory convertFromDTOToEntity(BookCategoryDTO bookCategoryDTO) {
		BookCategory bookCategory = new BookCategory();
		bookCategory.setCategoryId(bookCategoryDTO.getCategoryId());
		bookCategory.setCategoryName(bookCategoryDTO.getCategoryName());
		bookCategory.setBooks((List<Book>) bookService.getBooksByIds(bookCategoryDTO.getBooks()));
		return bookCategory;
	}

}