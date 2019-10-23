package com.kubazawierucha.powerfulbodyapp;

import com.kubazawierucha.powerfulbodyapp.UserProfile.BMIClassifier;
import com.kubazawierucha.powerfulbodyapp.UserProfile.BodyConditionCalculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class BodyConditionCalculatorTest {

    @Test
    public void bmiFor72_57kgAnd1_78mShouldBeAlmostEqual22_9() {
        assertEquals(22.9, BodyConditionCalculator.calculateBMI(1.78, 72.57), 0.1);
    }

    @Test
    public void bmiShouldBe0WhenHeightIsZero() {
        assertEquals(0, BodyConditionCalculator.calculateBMI(1.78, 0.0), 0.001);
    }

    @Test
    public void bfpShouldBe15_7For178Height50Neck96Waist92HipMan() {
        assertEquals(15.7, BodyConditionCalculator.calculateBFP(96, 50, 92, 178, "Male"), 0.1);
    }

    @Test
    public void bfpShouldBe24_1For178Height50Neck96Waist92HipWoman() {
        assertEquals(24., BodyConditionCalculator.calculateBFP(96, 50, 92, 178, "Female"), 0.1);
    }

    @Test
    public void bmi16ShouldBeModerateThin() {
        assertEquals(BMIClassifier.MODERATE_THINNESS, BodyConditionCalculator.classifyBMI(16));
    }

    @Test
    public void bmi17ShouldBeMildThin() {
        assertEquals(BMIClassifier.MILD_THINNESS, BodyConditionCalculator.classifyBMI(17));
    }

    @Test
    public void bmi18_5ShouldBeNormal() {
        assertEquals(BMIClassifier.NORMAL, BodyConditionCalculator.classifyBMI(18.5));
    }

    @Test
    public void bmi25ShouldBeOverweight() {
        assertEquals(BMIClassifier.OVERWEIGHT, BodyConditionCalculator.classifyBMI(25));
    }

    @Test
    public void bmi30ShouldBeObese1() {
        assertEquals(BMIClassifier.OBESE_CLASS_I, BodyConditionCalculator.classifyBMI(30));
    }

    @Test
    public void bmi35ShouldObese2() {
        assertEquals(BMIClassifier.OBESE_CLASS_II, BodyConditionCalculator.classifyBMI(35));
    }

    @Test
    public void bmi40ShouldBeObese3() {
        assertEquals(BMIClassifier.OBESE_CLASS_III, BodyConditionCalculator.classifyBMI(40));
    }

    @Test
    public void bmi10ShouldBeSevereThin() {
        assertEquals(BMIClassifier.SEVERE_THINNESS, BodyConditionCalculator.classifyBMI(10));
    }
}