package com.trafficinfosystem.demo.service;

import com.trafficinfosystem.demo.entity.User;
import com.trafficinfosystem.demo.vo.GenderCountVO;
import com.trafficinfosystem.demo.vo.UsageStatsMonthVO;
import com.trafficinfosystem.demo.vo.UsageStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StaticService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Get the total number of users
     * @return
     */
    public Long getTotalUserCount() {
        // Create an empty Query object that does not set any filter conditions
        Query query = new Query();

        // Use the count method to obtain the total number, and "users" is the collection name of the user data stored in MongoDB
        return mongoTemplate.count(query, "user");
    }

    /**
     * Get the number of users created within the last month
     * @return
     */
    public Long getUsersCreatedWithinLastMonth() {
        // Get the current time
        LocalDateTime now = LocalDateTime.now();
        // Get the time a month ago
        LocalDateTime oneMonthAgo = now.minusMonths(1);

        // Create a query condition
        Criteria criteria = Criteria.where("createTime").gte(oneMonthAgo).lte(now);
        Query query = new Query(criteria);

        // Use the count method to obtain the total number of users created within the last month
        return mongoTemplate.count(query, User.class);
    }

    /**
     * count user by gender
     * @return
     */
    public List<GenderCountVO> countUsersByGender() {
        // Create an aggregation object to group by
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("sex").count().as("count"),
                Aggregation.project("count").and("gender").previousOperation()
        );

        // Execute the aggregation operation
        AggregationResults<GenderCountVO> results = mongoTemplate.aggregate(
                aggregation, User.class, GenderCountVO.class
        );

        return results.getMappedResults();
    }

    /**
     * Get the total service usage
     * @return
     */
    public Long getTotalServiceUsage() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group()  //don't specify a key because it needs to aggregate globally
                        .sum("totalServiceUsage").as("totalUsage")
        );

        AggregationResults<UsageStatsVO> results = mongoTemplate.aggregate(
                aggregation, User.class, UsageStatsVO.class
        );
        UsageStatsVO result = results.getUniqueMappedResult();
        return result != null ? (Long) result.getTotalUsage() : 0;
    }

    public Long getTotalServiceUsageForMonth() {
        // Get the current time
        LocalDateTime now = LocalDateTime.now();
        // Get the time a month ago
        LocalDateTime oneMonthAgo = now.minusMonths(1);

        Criteria criteria = Criteria.where("createTime").gte(oneMonthAgo).lte(now);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group()  //don't specify a key because it needs to aggregate globally
                        .sum("totalServiceUsage").as("totalUsage")
        );

        AggregationResults<UsageStatsMonthVO> results = mongoTemplate.aggregate(
                aggregation, User.class, UsageStatsMonthVO.class
        );
        UsageStatsMonthVO result = results.getUniqueMappedResult();
        return result != null ? (Long) result.getTotalUsage() : 0;
    }

}
