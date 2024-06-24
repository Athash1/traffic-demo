package com.trafficinfosystem.demo.repositories;

import com.trafficinfosystem.demo.entity.Railway;
import com.trafficinfosystem.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RailwayRepository extends MongoRepository<Railway, String>{

    Optional<Railway> findFirstByTitleRegex(String regex);

    @Query("{ 'title': {$regex: ?0, $options: 'i'} }")
    Page<Railway> findByTitleLike(String namePattern, Pageable pageable);
}
