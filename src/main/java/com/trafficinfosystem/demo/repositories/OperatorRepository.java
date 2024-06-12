package com.trafficinfosystem.demo.repositories;

import com.trafficinfosystem.demo.entity.Operator;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OperatorRepository extends MongoRepository<Operator, String> {
    List<Operator> findByTitleLike(String titlePattern);
}
