package org.sda.librarymanagement.controller;

import java.util.List;

import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.sda.librarymanagement.service.BorrowingRegistrationService;
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
public class BorrowingRegistrationController {

	@Autowired
	private BorrowingRegistrationService borrowingRegistrationService;

	@PostMapping("/borrowingRegistration")
	public ResponseEntity<Void> addBorrowingRegistration(@RequestBody BorrowingRegistration borrowingRegistration,
			UriComponentsBuilder builder) {
		borrowingRegistrationService.saveBorrowingRegistration(borrowingRegistration);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/borrowingRegistration")
				.buildAndExpand(borrowingRegistration.getBorrowingRegistrationId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/boborrowingRegistrationok/{id}")
	public ResponseEntity<BorrowingRegistration> getBorrowingRegistrationById(@PathVariable("id") Long id) {
		BorrowingRegistration borrowingRegistration = borrowingRegistrationService.getOneBorrowingRegistrationById(id);
		return new ResponseEntity<BorrowingRegistration>(borrowingRegistration, HttpStatus.OK);
	}

	@GetMapping("/borrowingRegistrations")
	public ResponseEntity<List<BorrowingRegistration>> getAllBorrowingRegistrations() {
		List<BorrowingRegistration> borrowingRegistration = borrowingRegistrationService.getAllBorrowingRegistrations();
		return new ResponseEntity<List<BorrowingRegistration>>(borrowingRegistration, HttpStatus.OK);
	}

	@PutMapping("/borrowingRegistration/{id}")
	public ResponseEntity<BorrowingRegistration> updateBorrowingRegistration(@PathVariable Long id,
			@RequestBody BorrowingRegistration borrowingRegistration) {
		borrowingRegistrationService.updateBorrowingRegistration(id, borrowingRegistration);
		return new ResponseEntity<BorrowingRegistration>(borrowingRegistration, HttpStatus.OK);
	}

	@DeleteMapping("/borrowingRegistration/{id}")
	public ResponseEntity<Void> deleteBorrowingRegistration(@PathVariable("id") Long id) {
		borrowingRegistrationService.deleteBorrowingRegistration(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
