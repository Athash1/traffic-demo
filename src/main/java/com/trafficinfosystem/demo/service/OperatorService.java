package com.trafficinfosystem.demo.service;

import com.trafficinfosystem.demo.entity.Operator;
import com.trafficinfosystem.demo.repositories.OperatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sky.api.key}")
    private String consumerKey;

    // 这个方法设置为每月的第一天自动执行
    @Scheduled(cron = "0 0 0 1 * ?")  // 在每月的第一天午夜0点0分0秒执行1
    public void fetchAndStoreOperators() {
        String url = "https://api.odpt.org/api/v4/odpt:Operator?acl:consumerKey=";
        Operator[] operators = restTemplate.getForObject(url + consumerKey, Operator[].class);
        if (operators != null) {
            operatorRepository.saveAll(Arrays.asList(operators));
        }
    }
}
