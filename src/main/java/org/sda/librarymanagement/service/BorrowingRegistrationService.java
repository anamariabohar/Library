package org.sda.librarymanagement.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.sda.librarymanagement.repository.BorrowingRegistrationRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BorrowingRegistrationService {

	@Autowired
	private BorrowingRegistrationRepository borrowingRegistrationRepository;

	@Autowired
	private EntityManager entityManager;

	public List<BorrowingRegistration> getAllBorrowingRegistrations() {
		return borrowingRegistrationRepository.findAll();
	}

	public BorrowingRegistration getOneBorrowingRegistrationById(@PathVariable Long id) {
		return entityManager.find(BorrowingRegistration.class, id);
	}

	public void saveBorrowingRegistration(@RequestBody BorrowingRegistration borrowingRegistration) {
		borrowingRegistrationRepository.saveAndFlush(borrowingRegistration);
	}

	public BorrowingRegistration updateBorrowingRegistration(@PathVariable Long id,
			@RequestBody BorrowingRegistration borrowingRegistration) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		BeanUtils.copyProperties(borrowingRegistration, existingBorrowingRegistration);
		return borrowingRegistrationRepository.saveAndFlush(existingBorrowingRegistration);
	}

	public BorrowingRegistration deleteBorrowingRegistration(@PathVariable Long id) {
		BorrowingRegistration existingBorrowingRegistration = entityManager.find(BorrowingRegistration.class, id);
		borrowingRegistrationRepository.delete(existingBorrowingRegistration);
		return existingBorrowingRegistration;
	}

}
