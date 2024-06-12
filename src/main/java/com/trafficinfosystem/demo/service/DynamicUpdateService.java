package com.trafficinfosystem.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class DynamicUpdateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Updates fields in a document that are not null.
     * @param id The id of the document to update.
     * @param updates A map containing the field names and their new values.
     * @param entityClass The class of the document.
     * @return Whether the update was successful.
     */
    public <T> boolean updateFields(String id, Map<String, Object> updates, Class<T> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        updates.forEach(update::set);

        T result = mongoTemplate.findAndModify(query, update, entityClass);
        return result != null;
    }
}

