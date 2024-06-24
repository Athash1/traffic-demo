package com.trafficinfosystem.demo.apiManage;

import com.trafficinfosystem.demo.constant.MessageConstant;
import com.trafficinfosystem.demo.constant.TrafficConstant;
import com.trafficinfosystem.demo.constant.UrlConsstant;
import com.trafficinfosystem.demo.dto.RailwayPageQueryDTO;
import com.trafficinfosystem.demo.entity.Operator;
import com.trafficinfosystem.demo.entity.Railway;
import com.trafficinfosystem.demo.entity.TrainInformation;
import com.trafficinfosystem.demo.exception.EntityNotFoundException;
import com.trafficinfosystem.demo.repositories.OperatorRepository;
import com.trafficinfosystem.demo.repositories.RailwayRepository;
import com.trafficinfosystem.demo.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.trafficinfosystem.demo.vo.TrainInformationVO;

import java.util.Arrays;

@Service
@Slf4j
public class TrafficApiService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private RailwayRepository railwayRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sky.api.key}")
    private String consumerKey;

    /**
     * Fetch and store operator information
     * This method is set to be performed automatically on the first day of the month
     */
    // This method is set to be performed automatically on the first day of the month
    @Scheduled(cron = "0 0 0 1 * ?")  // Performed once at 00:00:00 on the first day of the month
    public void fetchAndStoreOperators() {
        String url = UrlConsstant.TRAFFIC_OPERATOR_URL + consumerKey;
        Operator[] operators = restTemplate.getForObject(url, Operator[].class);
        if (operators != null) {
            operatorRepository.saveAll(Arrays.asList(operators));
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void fetchAndStoreRailways() {
        String url = UrlConsstant.TRAFFIC_RAILWAY_URL + consumerKey;
        log.info("url: {}", url);
        Railway[] railways = restTemplate.getForObject(url, Railway[].class);
        if (railways != null) {
            railwayRepository.saveAll(Arrays.asList(railways));
        }
    }

    /**
     * Get operator information by title
     * @param title operator title
     * @return Operator
     */
    public Operator getOperatorByTitle(String title) {
        return operatorRepository.findFirstByTitleRegex(title)
                .orElseThrow(() -> new EntityNotFoundException(MessageConstant.OPERATOR_NOT_FOUND));
    }


    /**
     * Get train information by railwaySameAs
     * @param railwaySameAs railway sameAs
     * @return TrainInformationVO
     */
    public TrainInformationVO getTrainInformation(String railwaySameAs) {

        // Create a request URL
        String url = UriComponentsBuilder.fromHttpUrl(UrlConsstant.TRAFFIC_TRAIN_INFO_URL + consumerKey)
                .queryParam(TrafficConstant.ODPT_RAILWAY, railwaySameAs)
                .toUriString();

        // Send a GET request and get a response
        TrainInformation[] trainInformation = restTemplate.getForObject(url, TrainInformation[].class);

        // Check if train information is not null and has at least one element
        if (trainInformation != null && trainInformation.length > 0) {
            // Create a new TrainInformationVO object
            TrainInformationVO trainInformationVO = new TrainInformationVO();

            // Copy the ja and en fields from TrainInformation to TrainInformationVO
            trainInformationVO.setJa(trainInformation[0].getTrainInformationText().getJa());
            trainInformationVO.setEn(trainInformation[0].getTrainInformationText().getEn());

            // Return the TrainInformationVO object
            return trainInformationVO;
        }

        // Return null if no train information is found
        return null;
    }

    /**
     * Paging query
     * @param railwayPageQueryDTO
     * @return
     */
    public PageResult pageQuery(RailwayPageQueryDTO railwayPageQueryDTO) {
        if (railwayPageQueryDTO.getName() == null) {
            railwayPageQueryDTO.setName("");
        }
        Pageable pageable = PageRequest.of(railwayPageQueryDTO.getPage() - 1, railwayPageQueryDTO.getPageSize(), Sort.by("createTime").descending());
        Page<Railway> page = railwayRepository.findByTitleLike(railwayPageQueryDTO.getName(), pageable);

        return new PageResult(page.getTotalElements(), page.getContent());
    }
}
