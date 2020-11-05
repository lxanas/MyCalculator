package calculator;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Calculator
{
    private final Deque<String> rpnStack = new LinkedList<>(); //逆波兰表达式堆栈
    private final Deque<Character> opStack = new LinkedList<>(); //操作符堆栈
    private final Queue<String> exp = new LinkedList<>(); //将表达式读入存入队列
    private final int[] opPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2}; //操作符优先级

    //将表达式读入并处理成数字和运算符进入队列
    private void read(String expression)
    {
        char[] arr = expression.toCharArray();
        int count = 0;
        int curIndex = 0;
        char curCh;
        for (int i = 0; i < arr.length; i++)
        {
            curCh = arr[i];
            if (!isOperator(curCh))
            {
                count++;
            }
            else
            {
                if (count > 0)
                {
                    exp.offer(new String(arr, curIndex, count));
                }
                exp.offer(String.valueOf(curCh));
                count = 0;
                curIndex = i + 1;
            }
            if(i == arr.length-1)
            {
                exp.offer(new String(arr, curIndex, count));
            }
        }


    }

    public  static void test(String expression)
    {
        Calculator cal= new Calculator();
        cal.read(expression);
        System.out.println("fine.");
    }

    //读入数据并处理返回结果
    public static double putIn(String expression)
    {
        double res;
        Calculator cal = new Calculator();
        try
        {
            expression = transform(expression);
            res = cal.calculate(expression);
        } catch (Exception e)
        {
            // 运算错误返回NaN
            return 0.0 / 0.0;
        }
//        expression = transform(expression);
//        res = cal.calculate(expression);

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
        return value.replace("~", "-");
    }

    //对整个表达式进行计算
    private double calculate(String expression)
    {
        Deque<String> resDeque = new LinkedList<>();
        rpn(expression);
        String firstValue, secondValue, current;
        while (!rpnStack.isEmpty())
        {
            current = rpnStack.removeLast();
            if (!isOperator(current.charAt(0)))
            {
                current = untransformed(current);
                resDeque.push(current);
            }
            else
            {
                secondValue = resDeque.pop();
                firstValue = resDeque.pop();

                firstValue = untransformed(firstValue);
                secondValue = untransformed(secondValue);

                String temp = calculate(firstValue, secondValue, current.charAt(0));
                resDeque.push(temp);
            }

        }

        return Double.valueOf(resDeque.pop());
    }


    //对表达式计算中的单次计算操作进行处理
    private String calculate(String firstValue, String secondValue, char op)
    {
        String res = "";
        switch (op)
        {
            case '+' -> res = String.valueOf(Accurate.add(firstValue, secondValue));
            case '-' -> res = String.valueOf(Accurate.sub(firstValue, secondValue));
            case '*' -> res = String.valueOf(Accurate.mul(firstValue, secondValue));
            case '/' -> res = String.valueOf(Accurate.div(firstValue, secondValue));
        }
        return res;
    }

    //中缀表达式转为逆波兰式
    private void rpn(String expression)
    {
        opStack.push(',');
        char[] arr = expression.toCharArray();
        int count = 0; //数字长度
        int curIndex = 0; //当前字符位置
        char cur, top; //当前字符和栈顶字符
        for (int i = 0; i < arr.length; i++)
        {
            cur = arr[i];
            if (isOperator(cur))
            {
                if (count > 0)
                {
                    rpnStack.push(new String(arr, curIndex, count));
                }
                top = opStack.peek();
                if (cur == ')')
                {
                    while (opStack.peek() != '(')
                    {
                        rpnStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                }
                else
                {
                    while (cur != '(' && top != ',' && compare(cur, top))
                    {
                        rpnStack.push(String.valueOf(opStack.pop()));
                        top = opStack.peek();
                    }
                    opStack.push(cur);
                }
                count = 0;
                curIndex = i + 1;
            }
            else
            {
                count++;
            }

        }
        if (count > 1 || (count == 1 && !isOperator(arr[curIndex])))
        {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            rpnStack.push(new String(arr, curIndex, count));
        }

        while (opStack.peek() != ',')
        {
            rpnStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }


    }

    //判断计算符号
    private boolean isOperator(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    //比较计算符号优先级
    public boolean compare(char cur, char peek)
    {
        // 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        if (opPriority[(peek) - 40] >= opPriority[(cur) - 40])
        {
            result = true;
        }
        return result;
    }


}
