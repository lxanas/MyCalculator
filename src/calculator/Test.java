package calculator;

import java.util.Scanner;

public class Test
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String exp = in.nextLine();
        String exp1 = "0+"+exp;
        double res=Calculator.putIn(exp1);
        System.out.println(exp + " = " + res);
    }

}