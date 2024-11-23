package com.mnb.repository;

import com.mnb.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Integer> {
	// Custom query to find an author by author name
    @Query("SELECT a FROM Author a WHERE a.authorName = :authorName")
    Author findByAuthorName(@Param("authorName") String authorName);
}
