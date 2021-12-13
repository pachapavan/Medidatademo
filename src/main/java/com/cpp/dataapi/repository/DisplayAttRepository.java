package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.DisplayAtt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DisplayAtt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisplayAttRepository extends MongoRepository<DisplayAtt, String> {}
