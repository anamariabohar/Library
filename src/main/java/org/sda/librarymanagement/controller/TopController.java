package org.sda.librarymanagement.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.service.BookService;
import org.sda.librarymanagement.service.BorrowingRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library")
public class TopController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BorrowingRegistrationService borrowingRegistrationService;

	@GetMapping("/top3MostBorrowedBooks")
	public ResponseEntity<List<Book>> getTopMostThreeMostBorrowedBooks() {

		Map<String, Integer> theMostBorrowedBooksList = new HashMap<>();

		borrowingRegistrationService
		.getAllBorrowingRegistrations()
		.stream()
		.forEach(borrowing -> theMostBorrowedBooksList
				.merge(borrowing.getBook().getBookName(), 1, (oldV, newV) -> oldV + 1));
		
		LinkedHashMap<String, Integer> reverseSortedMap=new LinkedHashMap<>();
		
		theMostBorrowedBooksList
		.entrySet()
		.stream()
		.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.limit(3)
		.forEach(record -> reverseSortedMap.put(record.getKey(), record.getValue()));		

		List<Book> books=new ArrayList<>();
		
		reverseSortedMap
		.entrySet()
		.stream()
		.forEach(bookName -> books.add(bookService.getOneBookByName(bookName.getKey())));
		
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

}
