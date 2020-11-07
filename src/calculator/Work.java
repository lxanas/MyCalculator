package calculator;

import java.util.Scanner;

public class Work
{

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String exp = in.nextLine();
//        System.out.println(isInteger(exp));
        double res=Calculator.putIn(exp);
        System.out.println(exp + " = " + res);
    }

}