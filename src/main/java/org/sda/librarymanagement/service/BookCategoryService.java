package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

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
@Transactional
public class BookCategoryService {

	@Autowired
	private BookCategoryRepository bookCategoryRepository;

	@Autowired
	private BookService bookService;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public List<BookCategory> getAllBookCategories() {
		return (List<BookCategory>) bookCategoryRepository.findAll();
	}

	@Transactional
	public List<BookCategory> getBookCategoriesByIds(List<Long> ids) {
		return (List<BookCategory>) bookCategoryRepository.findAllById(ids);
	}

	@Transactional
	public BookCategory getOneBookCategoryById(@PathVariable Long id) {
		return entityManager.find(BookCategory.class, id);
	}

	@Transactional
	public void saveBookCategory(BookCategory bookCategory) {
		bookCategoryRepository.save(bookCategory);
	}

	@Transactional
	public BookCategory updateBookCategory(@PathVariable Long id, @RequestBody BookCategory bookCategory) {
		BookCategory existingBookCategory = entityManager.find(BookCategory.class, id);
		BeanUtils.copyProperties(bookCategory, existingBookCategory);
		return bookCategoryRepository.save(existingBookCategory);
	}

	@Transactional
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