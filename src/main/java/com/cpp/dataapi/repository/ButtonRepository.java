package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Button;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Button entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ButtonRepository extends MongoRepository<Button, String> {}
