package calculator;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Try
{

    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String exp = in.nextLine();
        Calculator.test(exp);
    }
}
