package com.cpp.dataapi.repository;

import com.cpp.dataapi.domain.BadgeType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the BadgeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BadgeTypeRepository extends MongoRepository<BadgeType, String> {}
