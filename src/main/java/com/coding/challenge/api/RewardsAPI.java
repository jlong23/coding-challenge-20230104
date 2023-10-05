package com.coding.challenge.api;

import com.coding.challenge.api.error.ServiceNotAvailableException;
import com.coding.challenge.domain.RewardsCalculator;
import com.coding.challenge.domain.model.RewardsVO;
import com.coding.challenge.domain.model.TransactionSummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@Tag(
        name = "rewards",
        description = "API for the Rewards program")
public class RewardsAPI {

    private final Logger logger = LoggerFactory.getLogger(RewardsAPI.class);

    @Autowired
    private RewardsCalculator rewardsCalculator;

    @PostMapping(
            value = "/calculate_period",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Calculate Rewards Points for Period",
            description = "Given a customer loyalty id and a collection of transaction activity, calculate the rewards points for that period",
            tags = { "rewards" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "403", description = "Input Data Parsing Error"),
            @ApiResponse(responseCode = "500", description = "Service not available at this moment."),
    })
    ResponseEntity<RewardsVO> calculateRewardsPeriod(
            @Valid @RequestBody List<TransactionSummaryVO> transactionSetVO ) {

        ResponseEntity.BodyBuilder response = ResponseEntity.ok();

        if( rewardsCalculator == null ) {
            logger.error("Rewards Calculation Dependency is not defined.");
            throw new ServiceNotAvailableException("The Service it not available at this time.");
        }

        return response.body( rewardsCalculator.calculateRewardsForPeriod( transactionSetVO, new Date()));
    }

}
