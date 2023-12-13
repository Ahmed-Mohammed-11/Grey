package com.software.grey.recommendationsystem;

import com.software.grey.models.enums.Feeling;
import com.software.grey.models.projections.FeelingCountProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationStrategyUnitTest {
    @Autowired
    RecommendationStrategy recommendationStrategy;

    @Test
    public void testGetFeelingPercentage() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 3),
                new FeelingCountImpl(Feeling.SAD, 2),
                new FeelingCountImpl(Feeling.ANXIOUS, 1)
        );

        // Call the method to get feeling percentages
        Map<Feeling, Double> percentageMap = recommendationStrategy.getFeelingPercentage(feelingsCount);

        // Check the result against expected values
        assertEquals(0.5, percentageMap.get(Feeling.HAPPY), 0.001);
        assertEquals(0.333, percentageMap.get(Feeling.SAD), 0.001);
        assertEquals(0.167, percentageMap.get(Feeling.ANXIOUS), 0.001);
    }

    @Test
    public void testGetFeelingPercentageWithEmptyList() {
        // Create an empty list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList();

        // Call the method to get feeling percentages
        Map<Feeling, Double> percentageMap = recommendationStrategy.getFeelingPercentage(feelingsCount);

        // Check that the result is an empty map
        assertTrue(percentageMap.isEmpty());
    }

    @Test
    public void testGetFeelingPercentage_WithZeroFeeling() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 5),
                new FeelingCountImpl(Feeling.SAD, 0)
        );

        // Call the method to get feeling percentages
        Map<Feeling, Double> percentageMap = recommendationStrategy.getFeelingPercentage(feelingsCount);

        // Check the result against expected values
        assertEquals(1, percentageMap.get(Feeling.HAPPY), 0.001);
        assertNull(percentageMap.get(Feeling.SAD));
    }

    @Test
    public void testGetFeelingPercentage_WithZeroFeeling2() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 0),
                new FeelingCountImpl(Feeling.SAD, 0)
        );

        // Call the method to get feeling percentages
        Map<Feeling, Double> percentageMap = recommendationStrategy.getFeelingPercentage(feelingsCount);

        // Check the result against expected values
        assertTrue(percentageMap.isEmpty());
    }

    @Test
    public void testGetSumOfFeelings() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 3),
                new FeelingCountImpl(Feeling.SAD, 2),
                new FeelingCountImpl(Feeling.LOVE, 1)
        );

        // Call the method to get the sum of feelings
        int sum = recommendationStrategy.getSumOfFeelings(feelingsCount);

        // Check that the sum is as expected
        assertEquals(6, sum);
    }

    @Test
    public void testGetSumOfFeelingsWithEmptyList() {
        // Create an empty list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList();

        // Call the method to get the sum of feelings
        int sum = recommendationStrategy.getSumOfFeelings(feelingsCount);

        // Check that the sum is 0 for an empty list
        assertEquals(0, sum);
    }

    @Test
    public void testGetSumOfFeelings_WithZeroFeeling() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 5),
                new FeelingCountImpl(Feeling.SAD, 0)
        );

        // Call the method to get the sum of feelings
        int sum = recommendationStrategy.getSumOfFeelings(feelingsCount);

        // Check that the sum is as expected
        assertEquals(5, sum);
    }

    @Test
    public void testGetSumOfFeelings_WithZeroFeeling2() {
        // Create a list of FeelingCountProjection for testing
        List<FeelingCountProjection> feelingsCount = Arrays.asList(
                new FeelingCountImpl(Feeling.HAPPY, 0),
                new FeelingCountImpl(Feeling.SAD, 0)
        );

        // Call the method to get the sum of feelings
        int sum = recommendationStrategy.getSumOfFeelings(feelingsCount);

        // Check that the sum is as expected
        assertEquals(0, sum);
    }
}