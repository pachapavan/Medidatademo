package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Page entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageRepository extends MongoRepository<Page, String> {}
