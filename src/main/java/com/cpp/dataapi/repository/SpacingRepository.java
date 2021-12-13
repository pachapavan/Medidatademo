package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Spacing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Spacing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpacingRepository extends MongoRepository<Spacing, String> {}
