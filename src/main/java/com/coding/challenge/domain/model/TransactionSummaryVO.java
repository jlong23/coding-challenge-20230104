package com.coding.challenge.domain.model;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A Wrapper for Customer Transaction Activity
 * <p></p>
 * Should contain a list of transactions and dates for Rewards Calculation Engine to process.
 */
@Data
@Builder
@EqualsAndHashCode
@ToString
public class TransactionSummaryVO {

    @Schema(description = "The unique Transaction Id ", example = "9b04619e-b26e-4610-8167-10b41697d432", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String transactionId;

    @Schema(description = "Date Time the Transaction", example = "2023-10-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Date transactionDate;

    @Schema(description = "Transaction total amount", example = "19.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private double transactionTotal;
}
