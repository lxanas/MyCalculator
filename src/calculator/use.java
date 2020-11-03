package calculator;

import java.util.Scanner;

public class use
{
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();
        double result = tCalculator.conversion(expression);
        System.out.println(expression + " = " + result);
        System.out.println();
    }
}
