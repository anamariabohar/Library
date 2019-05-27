package org.sda.librarymanagement.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.service.BookService;
import org.sda.librarymanagement.service.BorrowingRegistrationService;
import org.sda.librarymanagement.service.ClientService;
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
	
	@Autowired
	private ClientService clientService;

	@GetMapping("/top3MostBorrowedBooks")
	public ResponseEntity<List<Book>> getTopThreeMostBorrowedBooks() {

		Map<String, Integer> theMostBorrowedBooksList = new HashMap<>();

		borrowingRegistrationService
		.getAllBorrowingRegistrations()
		.stream()
		.forEach(borrowing -> theMostBorrowedBooksList
				.merge(borrowing.getBook().getBookName(), 1, (oldV, newV) -> oldV + 1));
		
		LinkedHashMap<String, Integer> reverseSortedMap=new LinkedHashMap<>();
		
		Comparator<Entry<String, Integer>> descendingByScore=Collections.reverseOrder(Map.Entry.comparingByValue());
		Comparator<Entry<String, Integer>> ascendingByKey=Collections.reverseOrder(Map.Entry.comparingByKey());
		
		theMostBorrowedBooksList
		.entrySet()
		.stream()
		.sorted(descendingByScore.thenComparing(ascendingByKey))
		.limit(3)
		.forEach(record -> reverseSortedMap.put(record.getKey(), record.getValue()));		

		List<Book> books=new ArrayList<>();
		
		reverseSortedMap
		.entrySet()
		.stream()
		.forEach(bookName -> books.add(bookService.getOneBookByName(bookName.getKey())));
		
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}
	
	@GetMapping("/clientsAndUnreturnedBooks")
	public ResponseEntity<Map<Long, String>> getClientsAndTheirUnreturnedBooks(){
		
		Map<Long, String> map=new HashMap<>();
		
		borrowingRegistrationService.getAllBorrowingRegistrations()
		.stream()
		.filter(borrowing -> borrowing.getReturnDate()==null)
		.forEach(borrowing -> map.put(borrowing.getClient().getClientId(), borrowing.getBook().getBookName()));
		
		
		return new ResponseEntity<Map<Long, String>>(map, HttpStatus.OK);
	}
	
}
