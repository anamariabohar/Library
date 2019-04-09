package org.sda.librarymanagement.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.BookCategory;
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
	private EntityManager entityManager;

	public List<BookCategory> getAllBookCategories() {
		return bookCategoryRepository.findAll();
	}

	public List<BookCategory> getBookCategoriesByIds(List<Long> ids) {
		return bookCategoryRepository.findAllById(ids);
	}

	public BookCategory getOneBookCategoryById(@PathVariable Long id) {
		return entityManager.find(BookCategory.class, id);
	}

	public void saveBookCategory(BookCategory bookCategory) {
		bookCategoryRepository.saveAndFlush(bookCategory);
	}

	public BookCategory updateBookCategory(@PathVariable Long id, @RequestBody BookCategory bookCategory) {
		BookCategory existingBookCategory = bookCategoryRepository.getOne(id);
		BeanUtils.copyProperties(bookCategory, existingBookCategory);
		return bookCategoryRepository.saveAndFlush(existingBookCategory);
	}

	public BookCategory deleteBookCategory(@PathVariable Long id) {
		BookCategory existingBookCategory = bookCategoryRepository.getOne(id);
		bookCategoryRepository.delete(existingBookCategory);
		return existingBookCategory;
	}

}