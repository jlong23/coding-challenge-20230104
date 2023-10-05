package com.coding.challenge.domain;

import com.coding.challenge.domain.model.RewardsPeriodVO;
import com.coding.challenge.domain.model.RewardsVO;
import com.coding.challenge.domain.model.TransactionSummaryVO;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RewardsCalculator {

    private final Logger logger = LoggerFactory.getLogger(RewardsCalculator.class);

    @Value("${rewards.window.months}")
    private int rewardsWindowMonths;

    @PostConstruct
    public void init() {
        logger.info("** Rewards Window Months configuration = " + rewardsWindowMonths ) ;
    }

    /**
     * Calculate The accumulated rewards for a given data set according to the Rules established in the ReadMe section
     * of the repo.
     * <p></p>
     * The Rules defined in the challenge allude to a sliding window of transaction activity over the period of 3 months
     * but would not include starting days of the month.  With this assumption the calculation will be a strict 3-month
     * sliding window of transactions, excluding transaction dates that falls out of the range is filtered out.
     *
     * @param transactionSet A list of transaction summaries
     * @return the calculated values of Reward Points for the period
     */
    @NotNull
    public RewardsVO calculateRewardsForPeriod(List<TransactionSummaryVO> transactionSet, Date periodEndDate ) {
        RewardsVO rewardsVO = new RewardsVO();

        Date periodStartDate = getStartingPeriod( periodEndDate );
        Date windowEndDate = getEndingPeriod( periodEndDate );

        rewardsVO.setPeriodEnd(windowEndDate);
        rewardsVO.setPeriodStart(periodStartDate);

        Map<Date, List<TransactionSummaryVO>> grouped = transactionSet.stream()
                .filter(transaction -> transaction.getTransactionDate().after(periodStartDate) &&
                transaction.getTransactionDate().before(windowEndDate))
                .collect(Collectors.groupingBy( t -> getFirstDateOfMonth( t.getTransactionDate() ),
                        Collectors.toList()));

        List<RewardsPeriodVO> periods = grouped.entrySet().stream()
                .map( e -> calculateRewardsPeriodSegment(e.getKey(), e.getValue())).collect(Collectors.toList());

        rewardsVO.setRewardsPointsPeriods(periods);

        int totalRewardsPeriod = periods.stream().mapToInt(RewardsPeriodVO::getAccumulatedRewards).sum();
        rewardsVO.setRewardsPointsTotal(totalRewardsPeriod);

        return rewardsVO;
    }

    /**
     * Build the Period Summary Object for the periodStart and the transactions that ar in the period
     *
     * @param periodStart First Day of the Month of the transactionSet
     * @param transactionSet The subset of the transactions
     * @return Population Rewards Points Period value object
     */
    protected RewardsPeriodVO calculateRewardsPeriodSegment(Date periodStart, List<TransactionSummaryVO> transactionSet ) {
        RewardsPeriodVO rewardsPeriodVO = new RewardsPeriodVO();

        double totalTransactionsValue = transactionSet.stream().mapToDouble(TransactionSummaryVO::getTransactionTotal).sum();
        int totalTransactionsCount = transactionSet.size();

        int totalRewardsPeriod = transactionSet.stream().mapToInt( t ->
                calculateRewardsForTransactionAmount( t.getTransactionTotal() )).sum();

        rewardsPeriodVO.setPeriodStart(periodStart);
        rewardsPeriodVO.setTotalTransactions( totalTransactionsCount );
        rewardsPeriodVO.setTotalTranslationsAmount(totalTransactionsValue);
        rewardsPeriodVO.setAccumulatedRewards(totalRewardsPeriod);

        return rewardsPeriodVO;
    }

    /**
     * Calculate the rewards for a Transaction Amount
     *
     * @param transactionTotal a transaction amount
     * @return the calculated rewards points for that transaction
     */
    protected int calculateRewardsForTransactionAmount(double transactionTotal) {
        return (int) (Math.max( 0, Math.floor( transactionTotal - 50 ) * 1 )
                        + Math.max(0, Math.floor( transactionTotal -100 ) * 1));
    }

    /** Calculate the date range period for transaction acceptance
     *
     * @param endingPeriod the last day of the Period
     * @return the Starting Day of the Period
     */
    private Date getStartingPeriod( Date endingPeriod ) {

        return Date.from( endingPeriod.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .minusMonths( rewardsWindowMonths )
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant() );
    }

    /**
     * Calculate the Ending Period from a Date  ( IE: Mas Time )
     *
     * @param endingPeriod date Provided
     * @return Max Time for that Date
     */
    private Date getEndingPeriod( Date endingPeriod ) {

        return Date.from( endingPeriod.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant() );
    }

    /**
     * Calculate the First Day of the month so the Periods look sane
     *
     * @param existingDate date provided
     * @return First Day of that month with Minimum Time.
     */
    protected Date getFirstDateOfMonth( Date existingDate ) {
        return Date.from( existingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1)
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
