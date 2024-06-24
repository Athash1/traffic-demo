package com.trafficinfosystem.demo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePasswordDTO implements Serializable {

    private String id;//创建账户时可选

    private String password;
}
