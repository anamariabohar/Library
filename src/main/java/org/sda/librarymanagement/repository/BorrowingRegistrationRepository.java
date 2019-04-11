package org.sda.librarymanagement.repository;

import org.sda.librarymanagement.entity.BorrowingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRegistrationRepository extends JpaRepository<BorrowingRegistration, Long> {

}
