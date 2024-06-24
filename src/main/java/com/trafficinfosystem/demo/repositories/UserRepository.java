package com.trafficinfosystem.demo.repositories;

import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {
    User findByUsername(String username);

    @Query("{ 'name': {$regex: ?0, $options: 'i'} }")
    Page<User> findByNameLike(String namePattern, Pageable pageable);

    void deleteByUsername(String username);
}
