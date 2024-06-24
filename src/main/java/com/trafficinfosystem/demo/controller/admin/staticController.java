package com.trafficinfosystem.demo.controller.admin;

import com.trafficinfosystem.demo.entity.User;
import com.trafficinfosystem.demo.result.Result;
import com.trafficinfosystem.demo.service.StaticService;
import com.trafficinfosystem.demo.vo.GenderCountVO;
import com.trafficinfosystem.demo.vo.UsageStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/static")
@Slf4j
public class staticController {

    @Autowired
    private StaticService staticService;

    @GetMapping("/total")
    public Result<Long> getTotalUserCount() {
        return Result.success(staticService.getTotalUserCount());
    }

    @GetMapping("/lastMonth")
    public Result<Long> getUsersCreatedWithinLastMonth() {
        return Result.success(staticService.getUsersCreatedWithinLastMonth());
    }

    @GetMapping("/gender")
    public Result<List<GenderCountVO>> countUsersByGender() {
        return Result.success(staticService.countUsersByGender());
    }

    @GetMapping("/usageStatsTotal")
    public Result<Long> getUsageStatsTotal() {
        return Result.success(staticService.getTotalServiceUsage());
    }

    @GetMapping("/usageStatsMonth")
    public Result<Long> getUsageStatsMonth() {
        return Result.success(staticService.getTotalServiceUsageForMonth());
    }
}
