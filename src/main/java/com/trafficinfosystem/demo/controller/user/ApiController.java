package com.trafficinfosystem.demo.controller.user;

import com.trafficinfosystem.demo.apiManage.TrafficApiService;
import com.trafficinfosystem.demo.dto.RailwayPageQueryDTO;
import com.trafficinfosystem.demo.result.PageResult;
import com.trafficinfosystem.demo.result.Result;
import com.trafficinfosystem.demo.vo.OperatorVO;
import com.trafficinfosystem.demo.vo.TrainInformationVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trafficinfosystem.demo.entity.Operator;

@RestController
@RequestMapping("/user/api")
@Slf4j
public class ApiController {
    @Autowired
    private TrafficApiService trafficApiService;

    /**
     * Get operator title by title
     * @param title
     * @return
     */
    @GetMapping("/getOperatorTitle")
    public OperatorVO getOperatorTitle(String title) {
        log.info("Get operator title by title: {}", title);
        Operator operator = trafficApiService.getOperatorByTitle(title);

        // Create a new OperatorVO object
        OperatorVO operatorVO = new OperatorVO();

        // Copy the Title and sameAs fields from Operator to OperatorVO
        operatorVO.setTitle(operator.getOperatorTitle());
        operatorVO.setSameAs(operator.getSameAs());

        // Return the OperatorVO object
        return operatorVO;
    }

    @GetMapping("/pageRailway")
    @ApiOperation("Railway paging query")
    public Result<PageResult> pageRailway(RailwayPageQueryDTO railwayPageQueryDTO) {
        log.info("Railway paging query, 参数为：{}", railwayPageQueryDTO);
        PageResult pageResult = trafficApiService.pageQuery(railwayPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Get train information by sameAs-operator
     * @param sameAs
     * @return
     */
    @GetMapping("/getTrainInformation")
    public TrainInformationVO getTrainInformation(String sameAs) {
        log.info("Get train information by sameAs: {}", sameAs);
        return trafficApiService.getTrainInformation(sameAs);
    }
}
