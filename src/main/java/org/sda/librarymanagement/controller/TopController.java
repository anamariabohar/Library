package org.sda.librarymanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.sda.librarymanagement.entity.Book;
import org.sda.librarymanagement.entity.BorrowingRegistration;
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

		String sql = "select book_name, author_name from Borrowing_Registrations as br \n"
				+ "inner join Books as b  on b.book_id=br.book_id\n" + "group by br.book_id\n"
				+ "order by count(book_name) DESC\n" + "limit 3\n;";

		List<BorrowingRegistration> borrowingRegistrations = borrowingRegistrationService
				.getAllBorrowingRegistrations();

		List<Book> books = bookService.getAllBooks().stream()
				.filter(book -> borrowingRegistrations.stream().anyMatch(
						borrowingRegistration -> book.getBookId().equals(borrowingRegistration.getBook().getBookId())))
				.limit(3).collect(Collectors.toList());

		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

}
