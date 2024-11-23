package com.mnb.repository;

import com.mnb.entity.Publisher;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher,Integer> {
	// Custom query method to find a Publisher by its name
    Optional<Publisher> findByPublisherName(String publisherName);
}
