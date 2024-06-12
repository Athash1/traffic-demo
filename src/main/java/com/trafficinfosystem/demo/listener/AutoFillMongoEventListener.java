package com.trafficinfosystem.demo.listener;

import com.trafficinfosystem.demo.context.BaseContext;
import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.entity.User;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AutoFillMongoEventListener implements BeforeConvertCallback<Object> {

    @Override
    public Object onBeforeConvert(Object entity, String collection) {
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId(); // 确保能获取当前用户ID

        if (entity instanceof Employee employee) {
            if (employee.getCreateTime() == null) {
                employee.setCreateTime(now);
                employee.setCreateUser(currentId);
            }
            employee.setUpdateTime(now);
            employee.setUpdateUser(currentId);
            return employee;
        } else if (entity instanceof User user) {
            if (user.getCreateTime() == null) {
                user.setCreateTime(now);
                user.setCreateUser(currentId);
            }
            user.setUpdateTime(now);
            user.setUpdateUser(currentId);
            return user;
        }
        return entity;
    }
}


