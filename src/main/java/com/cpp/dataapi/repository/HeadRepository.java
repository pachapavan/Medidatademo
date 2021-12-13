package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Head;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Head entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeadRepository extends MongoRepository<Head, String> {}
