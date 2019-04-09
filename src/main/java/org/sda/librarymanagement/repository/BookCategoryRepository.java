package org.sda.librarymanagement.repository;

import java.util.Optional;

import org.sda.librarymanagement.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

}