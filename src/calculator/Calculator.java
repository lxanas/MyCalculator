package calculator;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class Calculator
{
    private Deque<String> rpnStack = new LinkedList<>(); //逆波兰表达式堆栈
    private Deque<Character> opStack = new LinkedList<>(); //操作符堆栈
    private Queue<String> exp = new LinkedList<>(); //将表达式读入存入队列
    private final int[] opPriority = new int[]{0, 4, 2, 1, -1, 1, 0, 2, 3}; //操作符优先级
    private int flag = 0; //错误标记

    private String beforeRead(String expression)
    {
        return "0+" + expression;
    }

    private void check(String expression)
    {
        char[] chars = expression.toCharArray();
        int countL = 0;
        int countR = 0;
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] == '(')
            {
                countL++;
            }
            if (chars[i] == ')')
            {
                countR++;
            }
            if (i > 0 && (isOperator_a(chars[i]) && isOperator_a(chars[i - 1])))
            {
                flag = 1;
            }
            if (chars[i] == '（')
                flag = 3;
        }
        if (countL != countR)
            flag = 2;
        if (flag == 1)
            System.out.println("输入非法表达式");
        if (flag == 2)
            System.out.println("括号不匹配");
        if (flag == 3)
            System.out.println("输入了中文括号，请输入英文括号");
    }

    //将表达式读入并处理成数字和运算符进入队列
    private void read(String expression)
    {
        expression = beforeRead(expression);
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
            if (i == arr.length - 1 && !isOperator(curCh))
            {
                exp.offer(new String(arr, curIndex, count));
            }
        }


    }

    //测试函数，方便查看栈内情况
    public static void test(String expression)
    {
        Calculator cal = new Calculator();
        expression = transform(expression);
        cal.read(expression);
//        cal.rpn_a();
        cal.rpn(expression);
        System.out.println("fine.");
    }

    //读入数据并处理返回结果
    public static double putIn(String expression)
    {
        double res;
        Calculator cal = new Calculator();
        try
        {
            cal.check(expression);

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
            case '^' -> res = String.valueOf(Accurate.pow(firstValue, secondValue));
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

    public boolean isInteger(String str)
    {
        char[] chars = str.toCharArray();
        if (chars.length == 1)
            return false;
        else
        {
            return true;
        }
    }

    private void rpn_a()
    {
        String cur = "";
        while (!exp.isEmpty())
        {
            cur = exp.poll();
            cur = untransformed(cur);
            if (isInteger(cur))
            {
                rpnStack.push(cur);
            }
            else
            {
                char[] chars = cur.toCharArray();
                char temp = chars[0];
                if (opStack.isEmpty())
                {
                    opStack.push(temp);
                }
                else if (chars[0] == '(')
                {
                    opStack.push(temp);
                }
                else if (temp == ')')
                {
                    char top = opStack.peek();
                    while (top != '(')
                    {
                        char op = opStack.pop();
                        rpnStack.push(String.valueOf(op));
                        top = opStack.peek();
                    }
                    opStack.pop();
                }
                else
                {
                    while (levelF(temp) < levelS(opStack.peek()))
//                    while (compare(temp, opStack.peek()))
                    {
                        char op = opStack.pop();
                        rpnStack.push(String.valueOf(op));
                        if (opStack.isEmpty())
                            break;
                    }
                    opStack.push(temp);
                }
            }

        }
        while (!opStack.isEmpty())
        {
            char op = opStack.pop();
            rpnStack.push(String.valueOf(op));
        }

    }

    //判断计算符号
    private boolean isOperator(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '^';
    }

    private boolean isOperator_a(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    //比较计算符号优先级
    public boolean compare(char cur, char peek)
    {
        // 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
        boolean result = false;
        if (cur == '^')
            cur -= 46;
        if (peek == '^')
            peek -= 46;
        if (opPriority[(peek) - 40] >= opPriority[(cur) - 40])
        {
            result = true;
        }
        return result;
    }

    public int levelF(char op)
    {
        int a = 0;
        if (op == '+' || op == '-')
            a = 2;
        else if (op == '*' || op == '/')
            a = 4;
        else if (op == '^')
            a = 7;
        return a;
    }

    public int levelS(char op)
    {
        int a = 0;
        if (op == '+' || op == '-')
            a = 3;
        else if (op == '*' || op == '/')
            a = 5;
        else if (op == '^')
            a = 6;
        else if (op == '(')
            a = 1;
        return a;
    }


}
