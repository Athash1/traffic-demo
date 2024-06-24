package com.trafficinfosystem.demo.repositories;

import com.trafficinfosystem.demo.entity.Operator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OperatorRepository extends MongoRepository<Operator, String> {

    Optional<Operator> findFirstByTitleRegex(String regex);
}
