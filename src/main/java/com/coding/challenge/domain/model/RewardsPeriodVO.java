package com.coding.challenge.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * Rewards Calculation Response Object for Period (Month)
 * <p></p>
 * Returns the reward points for the customer using the current Rewards Calculation engine.
 */
@Data
@EqualsAndHashCode
@ToString
public class RewardsPeriodVO {

    @Schema(description = "First Day of the Month for the Period Evaluated", example = "2023-10-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Date periodStart;

    @Schema(description = "The total transactions for the period ", example = "0.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private double totalTranslationsAmount;

    @Schema(description = "Accumulated Transaction Count", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int totalTransactions;

    @Schema(description = "Accumulated Rewards for the period", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int accumulatedRewards;
}
