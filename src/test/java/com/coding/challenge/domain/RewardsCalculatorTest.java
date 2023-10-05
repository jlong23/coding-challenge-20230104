package com.coding.challenge.domain;

import com.coding.challenge.domain.model.RewardsPeriodVO;
import com.coding.challenge.domain.model.RewardsVO;
import com.coding.challenge.domain.model.TransactionSummaryVO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RewardsCalculatorTest {

    protected RewardsCalculator calc = new RewardsCalculator();

    /**
     * Positive Test for Rewards Transaction Calculation
     */
    @Test
    public void testCalculateRewardsForTransactionAmount() {
        assertEquals(90,
                calc.calculateRewardsForTransactionAmount(120d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is at first threshold
     */
    @Test
    public void testCalculateRewardsForTransactionUnderLowerThreshold() {
        assertEquals(2, calc.calculateRewardsForTransactionAmount(52d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is equal to first threshold
     */
    @Test
    public void testCalculateRewardsForTransactionEqualThreshold() {
        assertEquals(0, calc.calculateRewardsForTransactionAmount(50d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is nearly equal to first
     * threshold
     */
    @Test
    public void testCalculateRewardsForTransactionNearlyAtThreshold() {
        assertEquals(0, calc.calculateRewardsForTransactionAmount(50.99d));
    }

    /**
     * Validation of the Windowed Period Calculation method.
     */
    @Test
    public void testCalculateRewardsForPeriod( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildTestDate_SummarySet(), new Date());

        assertEquals( 1, results.getRewardsPointsPeriods().size());
        assertEquals( 90, results.getRewardsPointsTotal());
    }

    /**
     * Validation of the Windowed Period Calculation method.
     */
    @Test
    public void testCalculateRewardsForPeriod_3MonthEdgeCase( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildTestData_SummarySet3MonthsEdgeCase(), new Date());

        assertEquals( 3, results.getRewardsPointsPeriods().size());
        assertEquals( 90 * 3, results.getRewardsPointsTotal());
        results.getRewardsPointsPeriods().forEach(p -> assertEquals( 90, p.getAccumulatedRewards()));
        results.getRewardsPointsPeriods().forEach( p -> assertEquals( 122, p.getTotalTranslationsAmount()));
        results.getRewardsPointsPeriods().forEach( p -> assertEquals( 2, p.getTotalTransactions()));
    }

    /**
     * Validation of the Windowed Period Calculation method with Data outside window.
     */
    @Test
    public void testCalculateRewardsForPeriod_3MonthEdgeCasePlus( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildTestData_SummarySet3MonthsEdgeCasePlus(), new Date());

        assertEquals( 3, results.getRewardsPointsPeriods().size());
        assertEquals( 90 * 3, results.getRewardsPointsTotal());
        results.getRewardsPointsPeriods().forEach(p -> assertEquals( 90, p.getAccumulatedRewards()));
        results.getRewardsPointsPeriods().forEach( p -> assertEquals( 122, p.getTotalTranslationsAmount()));
        results.getRewardsPointsPeriods().forEach( p -> assertEquals( 2, p.getTotalTransactions()));
    }


    /**
     * Validation of the Single Period Calculation method.
     */
    @Test
    public void testCalculateRewardsPeriodSegment( ) {
        RewardsPeriodVO results = calc.calculateRewardsPeriodSegment( new Date(), buildTestDate_SummarySet());

        assertEquals( 90, results.getAccumulatedRewards());
        assertEquals( 1, results.getTotalTransactions());
        assertEquals( 120, results.getTotalTranslationsAmount());
    }

    /**
     * Negative Test: Validate that transactions less than threshold amounts do not accumulate rewards
     */
    @Test
    public void testCalculateRewardsPeriodSegmentEdgeCase( ) {
        RewardsPeriodVO results = calc.calculateRewardsPeriodSegment( new Date(), buildTestData_SummarySetEdgeCase());

        assertEquals( 90, results.getAccumulatedRewards());
        assertEquals( 2, results.getTotalTransactions());
        assertEquals( 122, results.getTotalTranslationsAmount());
    }

    private List<TransactionSummaryVO> buildTestDate_SummarySet() {
        ArrayList<TransactionSummaryVO> list = new ArrayList<>();
        list.add( TransactionSummaryVO.builder().transactionId("A").transactionDate(new Date()).transactionTotal(120).build());

        return list;
    }

    private List<TransactionSummaryVO> buildTestData_SummarySetEdgeCase() {
        List<TransactionSummaryVO> list = new ArrayList<>();

        // Latest Date
        list.add( buildTransaction("A", new Date(),120));
        list.add( buildTransaction("B", new Date(),2));

        return list;
    }

    private List<TransactionSummaryVO> buildTestData_SummarySet3MonthsEdgeCase() {
        List<TransactionSummaryVO> list = buildTestData_SummarySetEdgeCase();

        // One Month Ago from Today
        Date oneMonthAgo = buildHistoricalTestDate( 1 );
        list.add( buildTransaction("C", oneMonthAgo,120));
        list.add( buildTransaction("D", oneMonthAgo,2));

        // Two Month Ago from Today
        Date twoMonthAgo = buildHistoricalTestDate( 2 );
        list.add( buildTransaction("E", twoMonthAgo,120));
        list.add( buildTransaction("F", twoMonthAgo,2));

        return list;
    }


    private List<TransactionSummaryVO> buildTestData_SummarySet3MonthsEdgeCasePlus() {
        List<TransactionSummaryVO> list = buildTestData_SummarySet3MonthsEdgeCase();

        // Three Month Ago from Today
        Date threeMonthAgo = buildHistoricalTestDate( 3 );
        list.add( buildTransaction("G", threeMonthAgo,120));
        list.add( buildTransaction("H", threeMonthAgo,2));

        return list;
    }

    private TransactionSummaryVO buildTransaction( String id, Date transactionDate, double amount ) {
        return TransactionSummaryVO.builder().transactionId(id).transactionDate(transactionDate).transactionTotal(amount).build();
    }

    private Date buildHistoricalTestDate( int monthsToSubtract ) {
        return Date.from( LocalDate.now().minusMonths(monthsToSubtract).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}