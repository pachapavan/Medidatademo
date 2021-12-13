package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.TabelValues;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TabelValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TabelValuesRepository extends MongoRepository<TabelValues, String> {}
