package com.coding.challenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Rewards Calculation Response Object
 * <p></p>
 * Returns the reward points for the customer using the current Rewards Calculation engine.
 */
@Data
@EqualsAndHashCode
@ToString
public class RewardsVO {

    @Schema(description = "First Day of the Month for the Period Evaluated", example = "2023-07-04T00:00:00.000+0000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Date periodStart;

    @Schema(description = "Last Day of the Month for the Period Evaluated", example = "2023-07-04T00:00:00.000+0000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Date periodEnd;

    @Schema(description = "The Total Points Accumulated for the Period", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private int rewardsPointsTotal;

    @Schema(description = "The Monthly Period Total set, may be empty if no transactions were recorded", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private List<RewardsPeriodVO> rewardsPointsPeriods;
}
