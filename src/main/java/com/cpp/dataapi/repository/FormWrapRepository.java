package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.FormWrap;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FormWrap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormWrapRepository extends MongoRepository<FormWrap, String> {}
