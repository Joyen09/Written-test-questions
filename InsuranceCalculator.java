package test;


import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

enum FormulaType{
    MONTHLY_CONTACT(12),
    QUARTER_CONTACT(4),
    SINGLE_CONTACT(0.9),
    SEMI_CONTACT(2);

    private final double multiplier;

    FormulaType(double multiplier){
        this.multiplier = multiplier;
    }

    public double calculate(int sum){
        return sum * multiplier;
    }
}
public class InsuranceCalculator {
    private final int[] numbers;
    private final FormulaType formulaType;
    private double result;
    private boolean adjusted = false;
    public InsuranceCalculator(FormulaType formulaType, int... numbers) {
        if (numbers == null || numbers.length < 2) {
            throw new IllegalArgumentException("輸入至少需要兩個數值。");
        }
        this.numbers = numbers;
        this.formulaType = formulaType;
        this.result = calculate();
    }

    private double calculate(){
        int sum = Arrays.stream(numbers).sum();
        return formulaType.calculate(sum);
    }

    public InsuranceCalculator accept(Integer adjustment) {
        this.result += Optional.ofNullable(adjustment).orElse(0);
        this.adjusted = true;
        return this;
    }

    @Override
    public String toString() {
        String baseOutput = String.format("Your Input numbers : %s%nYour Calculate Result: %.1f",
                Arrays.toString(numbers), result);
        if (adjusted) {
            baseOutput += String.format("%nNumber Adjustment Result : %.1f", result);
        }
        return baseOutput;
    }

    public static void main(String[] args) {
        // 範例 1
        InsuranceCalculator calculator1 = new InsuranceCalculator(FormulaType.SINGLE_CONTACT, 10, 20, 30, 40, 50);
        System.out.println(calculator1);

        // 範例 2
        InsuranceCalculator calculator2 = new InsuranceCalculator(FormulaType.QUARTER_CONTACT, 10, 20, 30, 40, 50, 60, 20, 30).accept(100);
        System.out.println(calculator2);

    }
}
