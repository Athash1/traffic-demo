package com.trafficinfosystem.demo.aspect;

import com.trafficinfosystem.demo.context.BaseContext;
import com.trafficinfosystem.demo.entity.User;
import com.trafficinfosystem.demo.exception.EntityNotFoundException;
import com.trafficinfosystem.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceUsageAspect {

    @Autowired
    private UserRepository userRepository;

    @AfterReturning("execution(* com.trafficinfosystem.demo.controller.user.ApiController.*(..))")
    public void incrementServiceUsage(JoinPoint joinPoint) {
        // 获取用户ID，这里假设用户ID是方法的第一个参数
        String userId = BaseContext.getCurrentId();

        // 获取用户实例
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 增加服务使用次数
        user.incrementServiceUsage();

        // 保存更新后的用户实例
        userRepository.save(user);
        log.info("User {} service usage incremented", userId);
    }
}