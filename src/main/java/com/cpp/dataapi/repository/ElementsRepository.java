package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Elements;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Elements entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElementsRepository extends MongoRepository<Elements, String> {}
