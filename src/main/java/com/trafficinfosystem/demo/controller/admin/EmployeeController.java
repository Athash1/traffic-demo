package com.trafficinfosystem.demo.controller.admin;

import com.trafficinfosystem.demo.apiManage.TrafficApiService;
import com.trafficinfosystem.demo.constant.JwtClaimsConstant;
import com.trafficinfosystem.demo.dto.*;
import com.trafficinfosystem.demo.entity.Employee;
import com.trafficinfosystem.demo.properties.JwtProperties;
import com.trafficinfosystem.demo.result.PageResult;
import com.trafficinfosystem.demo.result.Result;
import com.trafficinfosystem.demo.service.EmployeeService;
import com.trafficinfosystem.demo.service.UserService;
import com.trafficinfosystem.demo.utils.JwtUtil;
import com.trafficinfosystem.demo.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Employee management
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private TrafficApiService trafficApiService;

    /**
     * Log in
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("Employee login：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);


        //登录成功后，生成jwt令牌 (After successful login, generate jwt token)
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * quit
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * Add new employees
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Add new employees: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * Employee paging query
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("Employee paging query, 参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * User paging query
     * @param userPageQueryDTO
     * @return
     */
    @GetMapping("/pageUser")
    @ApiOperation("User paging query")
    public Result<PageResult> pageUser(UserPageQueryDTO userPageQueryDTO) {
        log.info("User paging query, 参数为：{}", userPageQueryDTO);
        PageResult pageResult = userService.pageQuery(userPageQueryDTO);
        return Result.success(pageResult);
    }



    /**
     * Enable and disable employee accounts
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Enable and disable employee accounts")
    public Result startOrStop(@PathVariable Integer status, String id){
        log.info("Enable and disable employee accounts: {}, {}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * Query employee information based on ID
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("Query employee information for ID")
    public Result<Employee> getById(@PathVariable String id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * Edit employee information
     * @return
     */
    @PutMapping
    @ApiOperation("Edit employee information")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("Edit employee information: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
    /**
     * Edit employee password
     * @return
     */
    @PostMapping("/password")
    public Result updatePassword(@RequestBody EmployeePasswordDTO employeePasswordDTO){
        log.info("Edit employee password: {}", employeePasswordDTO);
        employeeService.updatePassword(employeePasswordDTO);
        return Result.success();
    }

    /**
     * Delete employee information based on username
     * @param username
     * @return
     */
    @DeleteMapping("{username}")
    public Result delete(@PathVariable String username){
        log.info("Delete employee information based on username: {}", username);
        employeeService.deleteByUserName(username);
        return Result.success();
    }
}
