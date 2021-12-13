package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.FlexBox;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FlexBox entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexBoxRepository extends MongoRepository<FlexBox, String> {}
