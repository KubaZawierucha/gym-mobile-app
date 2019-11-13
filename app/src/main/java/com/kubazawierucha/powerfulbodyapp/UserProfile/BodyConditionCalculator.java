package com.kubazawierucha.powerfulbodyapp.UserProfile;

public class BodyConditionCalculator {

    public static double calculateBMI(double height, double mass) {
        try {
            return mass / height / height;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static BMIClassifier classifyBMI(double BMI) {
        if (BMI < 16) {
            return BMIClassifier.SEVERE_THINNESS;
        } else if (BMI < 17) {
            return BMIClassifier.MODERATE_THINNESS;
        } else if (BMI < 18.5) {
            return BMIClassifier.MILD_THINNESS;
        } else if (BMI < 25) {
            return BMIClassifier.NORMAL;
        } else if (BMI < 30) {
            return BMIClassifier.OVERWEIGHT;
        } else if (BMI < 35) {
            return BMIClassifier.OBESE_CLASS_I;
        } else if (BMI < 40) {
            return BMIClassifier.OBESE_CLASS_II;
        } else {
            return BMIClassifier.OBESE_CLASS_III;
        }
    }


    public static double calculateBFP(double waist, double neck, double hip, double height, String gender) {
        double denominator;
        if (gender.equals("M")) {
            denominator = 1.0324 - 0.19077 * Math.log10(waist - neck) + 0.15456 * Math.log10(height);
        } else {
            denominator = 1.29579 - 0.35004 * Math.log10(waist + hip - neck) + 0.221 * Math.log10(height);
        }
        try {
            return 495 / denominator - 450;
        } catch (ArithmeticException e) {
            return 0.0;
        }
    }
}
