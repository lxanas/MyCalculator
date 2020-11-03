package calculator;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

public class Calculator
{
    private Deque<String> rpnStack = new LinkedList<>(); //逆波兰表达式堆栈
    private Deque<Character> opStack = new LinkedList<>(); //操作符堆栈
    private int[] opPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 }; //操作符优先级

    public static double calculate(String expression)
    {
        double res=0;
        Calculator cal = new Calculator();
        try{
            expression = transform(expression);
            res = cal.calculate(expression);
        }
        catch (Exception e) {
            // 运算错误返回NaN
            return 0.0 / 0.0;
        }

        return res;
    }

    //为了区分数值的负号和计算的减号，将数值负号转换为~
    private static String transform(String expression)
    {
        char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == '-')
            {
                if (i == 0)
                {
                    arr[i] = '~';
                }
                else
                {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e')
                    {
                        arr[i] = '~';
                    }
                }
            }
        }
        if (arr[0] == '~' || arr[1] == '(')
        {
            arr[0] = '-';
            return "0" + new String(arr);
        }
        else
        {
            return new String(arr);
        }
    }

    private double calculate(String expression)
    {
        Deque<String> resDeque= new LinkedList<>();
        before(expression);

    }

    private void before(String expression)
    {

    }

}
