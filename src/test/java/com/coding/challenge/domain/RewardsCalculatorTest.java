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
    public void testCalculateRewardsForTransaction() {
        assertEquals(90, calc.calculateRewardsForTransaction(120d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is at first threshold
     */
    @Test
    public void testCalculateRewardsForTransactionUnderLowerThreshold() {
        assertEquals(2, calc.calculateRewardsForTransaction(52d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is equal to first threshold
     */
    @Test
    public void testCalculateRewardsForTransactionEqualThreshold() {
        assertEquals(0, calc.calculateRewardsForTransaction(50d));
    }

    /**
     * Semi-Positive Test for Rewards Transaction Calculation, Where Transaction Amount is nearly equal to first
     * threshold
     */
    @Test
    public void testCalculateRewardsForTransactionNearlyAtThreshold() {
        assertEquals(0, calc.calculateRewardsForTransaction(50.99d));
    }

    /**
     * Validation of the Windowed Period Calculation method.
     */
    @Test
    public void testCalculateRewardsForPeriod( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildSummarySetData(), new Date());

        assertEquals( 1, results.getRewardsPointsPeriods().size());
        assertEquals( 90, results.getRewardsPointsTotal());
    }

    /**
     * Validation of the Windowed Period Calculation method.
     */
    @Test
    public void testCalculateRewardsForPeriodWithPositiveNegatives( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildSummarySetData3Months(), new Date());

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
    public void testCalculateRewardsForPeriodWithPositiveNegativeInputs( ) {
        RewardsVO results = calc.calculateRewardsForPeriod( buildSummarySetData3PlusMonths(), new Date());

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
    public void testCalculateRewardsForSet_1( ) {
        RewardsPeriodVO results = calc.calculateRewardsForSet( new Date(), buildSummarySetData());

        assertEquals( 90, results.getAccumulatedRewards());
        assertEquals( 1, results.getTotalTransactions());
        assertEquals( 120, results.getTotalTranslationsAmount());
    }

    /**
     * Negative Test: Validate that transactions less than threshold amounts do not accumulate rewards
     */
    @Test
    public void testCalculateRewardsForSet_2_Negative( ) {
        RewardsPeriodVO results = calc.calculateRewardsForSet( new Date(), buildSummarySetData2());

        assertEquals( 90, results.getAccumulatedRewards());
        assertEquals( 2, results.getTotalTransactions());
        assertEquals( 122, results.getTotalTranslationsAmount());
    }

    private List<TransactionSummaryVO> buildSummarySetData() {
        ArrayList<TransactionSummaryVO> list = new ArrayList<>();
        list.add( TransactionSummaryVO.builder().transactionId("A").transactionDate(new Date()).transactionTotal(120).build());

        return list;
    }

    private List<TransactionSummaryVO> buildSummarySetData2() {
        ArrayList<TransactionSummaryVO> list = new ArrayList<>();
        list.add( TransactionSummaryVO.builder().transactionId("A").transactionDate(new Date()).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("B").transactionDate(new Date()).transactionTotal(2).build());

        return list;
    }

    private List<TransactionSummaryVO> buildSummarySetData3Months() {
        ArrayList<TransactionSummaryVO> list = new ArrayList<>();

        // Latest Date
        list.add( TransactionSummaryVO.builder().transactionId("A").transactionDate(new Date()).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("B").transactionDate(new Date()).transactionTotal(2).build());

        // One Month Ago from Today
        Date oneMonthAgo = Date.from( LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        list.add( TransactionSummaryVO.builder().transactionId("C").transactionDate(oneMonthAgo).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("D").transactionDate(oneMonthAgo).transactionTotal(2).build());

        // Two Month Ago from Today
        Date twoMonthAgo = Date.from( LocalDate.now().minusMonths(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        list.add( TransactionSummaryVO.builder().transactionId("E").transactionDate(twoMonthAgo).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("F").transactionDate(twoMonthAgo).transactionTotal(2).build());

        return list;
    }


    private List<TransactionSummaryVO> buildSummarySetData3PlusMonths() {
        ArrayList<TransactionSummaryVO> list = new ArrayList<>();

        // Latest Date
        list.add( TransactionSummaryVO.builder().transactionId("A").transactionDate(new Date()).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("B").transactionDate(new Date()).transactionTotal(2).build());

        // One Month Ago from Today
        Date oneMonthAgo = Date.from( LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        list.add( TransactionSummaryVO.builder().transactionId("C").transactionDate(oneMonthAgo).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("D").transactionDate(oneMonthAgo).transactionTotal(2).build());

        // Two Month Ago from Today
        Date twoMonthAgo = Date.from( LocalDate.now().minusMonths(2).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        list.add( TransactionSummaryVO.builder().transactionId("E").transactionDate(twoMonthAgo).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("F").transactionDate(twoMonthAgo).transactionTotal(2).build());

        // Three Month Ago from Today
        Date threeMonthAgo = Date.from( LocalDate.now().minusMonths(3).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        list.add( TransactionSummaryVO.builder().transactionId("G").transactionDate(threeMonthAgo).transactionTotal(120).build());
        list.add( TransactionSummaryVO.builder().transactionId("H").transactionDate(threeMonthAgo).transactionTotal(2).build());

        return list;
    }
}