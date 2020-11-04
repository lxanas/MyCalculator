package calculator;

import java.util.Deque;
import java.util.LinkedList;

public class Calculator
{
    private Deque<String> rpnStack = new LinkedList<>(); //逆波兰表达式堆栈
    private Deque<Character> opStack = new LinkedList<>(); //操作符堆栈
    private final int[] opPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 }; //操作符优先级

    //读入数据并处理返回结果
    public static double putIn(String expression)
    {
        double res;
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

    //计算时将负号还原
    private String untransformed(String value)
    {
        return value.replace("~","-");
    }

    //对表达式进行计算
    private double calculate(String expression)
    {
        Deque<String> resDeque= new LinkedList<>();
        rpn(expression);
        String firstValue,secondValue,current;
        while(!rpnStack.isEmpty())
        {
            current=rpnStack.removeLast();
            if(!isOperator(current.charAt(0)))
            {
                current = untransformed(current);
                resDeque.push(current);
            }
            else
            {
                secondValue=resDeque.pop();
                firstValue=resDeque.pop();

                firstValue = untransformed(firstValue);
                secondValue = untransformed(secondValue);

                String temp=calculate(firstValue,secondValue,current.charAt(0));
                resDeque.push(temp);
            }

        }

        return Double.valueOf(resDeque.pop());
    }

    //对表达式计算中的单次计算操作进行处理
    private String calculate(String firstValue,String secondValue,char op)
    {
        String res="";
        switch (op)
        {
            case '+'-> res=String.valueOf(1);
            case '-'-> res=String.valueOf(2);
            case '*'-> res=String.valueOf(3);
            case '/'-> res=String.valueOf(4);
        }
        return res;
    }

    //中缀表达式转为逆波兰式
    private void rpn(String expression)
    {
        opStack.add('#');
        char[] arr = expression.toCharArray();
        int count=0;
        int curIndex=0;
        char cur,top;
        for(int i=0;i<arr.length;i++)
        {
            cur=arr[i];
        }


    }

    private boolean isOperator(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }



}
