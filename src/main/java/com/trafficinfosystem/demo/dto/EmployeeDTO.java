package com.trafficinfosystem.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private String id;//创建账户时可选

    private String username;

    private String name;

    private String phone;

    private String sex;

    private Integer status;
}
