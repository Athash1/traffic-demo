package com.trafficinfosystem.demo.repositories;

import com.trafficinfosystem.demo.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Employee findByUsername(String username);

    @Query("{ 'name': {$regex: ?0, $options: 'i'} }")
    Page<Employee> findByNameLike(String namePattern, Pageable pageable);

}