package com.trafficinfosystem.demo.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotBlank(message = "姓名不能为空")
    //姓名
    private String name;
    @NotBlank(message = "用户名不能为空")
    //用户名
    private String username;
    @NotBlank(message = "密码不能为空")
    //密码
    private String password;
    @NotBlank(message = "手机号不能为空")
    //手机号
    private String phone;
    @NotBlank(message = "性别不能为空")
    //性别 0 女 1 男
    private String sex;

    @NotBlank(message = "状态不能为空")//状态: 0:禁用 1:启用
    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    //用户位置，格式为"经度,纬度"
    private String location;

    //位置的更新时间
    private LocalDateTime locationUpdateTime;

    //用户总的服务使用次数
    private Long totalServiceUsage = 0L;

    public void incrementServiceUsage() {
        if (totalServiceUsage == null) {
            totalServiceUsage = 0L;
        }
        totalServiceUsage++;
    }
}