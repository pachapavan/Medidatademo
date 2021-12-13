package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Body;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Body entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BodyRepository extends MongoRepository<Body, String> {}
