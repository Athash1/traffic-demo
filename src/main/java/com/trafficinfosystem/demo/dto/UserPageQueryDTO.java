package com.trafficinfosystem.demo.dto;

import lombok.Data;

@Data
public class UserPageQueryDTO {
    //员工姓名
    private String name;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}
