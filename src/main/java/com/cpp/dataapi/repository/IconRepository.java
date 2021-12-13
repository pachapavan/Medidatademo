package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.Icon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Icon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IconRepository extends MongoRepository<Icon, String> {}
