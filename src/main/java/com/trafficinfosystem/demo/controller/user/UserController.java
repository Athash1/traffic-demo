package com.trafficinfosystem.demo.controller.user;

import com.trafficinfosystem.demo.constant.JwtClaimsConstant;
import com.trafficinfosystem.demo.dto.EmployeeDTO;
import com.trafficinfosystem.demo.dto.EmployeeLoginDTO;
import com.trafficinfosystem.demo.dto.UserDTO;
import com.trafficinfosystem.demo.dto.UserLoginDTO;
import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.entity.User;
import com.trafficinfosystem.demo.properties.JwtProperties;
import com.trafficinfosystem.demo.result.Result;
import com.trafficinfosystem.demo.service.UserService;
import com.trafficinfosystem.demo.utils.JwtUtil;
import com.trafficinfosystem.demo.vo.EmployeeLoginVO;
import com.trafficinfosystem.demo.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "C-side user related interface")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;
//
    /**
     * Log in
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("Employee login：{}", userLoginDTO);

        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌 (After successful login, generate jwt token)
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * quit
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("user exit")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping("/signUp")
    @ApiOperation("Add new user")
    public Result save(@RequestBody UserDTO userDTO) {
        log.info("Add new user: {}", userDTO);
        userService.save(userDTO);
        return Result.success();
    }
}
