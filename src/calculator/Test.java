package calculator;

import java.util.Scanner;

public class Test
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String exp = in.nextLine();
        double res=Calculator.putIn(exp);
        System.out.println(exp + " = " + res);
    }

}