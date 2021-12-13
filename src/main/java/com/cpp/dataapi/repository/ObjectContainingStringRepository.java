package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.ObjectContainingString;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ObjectContainingString entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectContainingStringRepository extends MongoRepository<ObjectContainingString, String> {}
