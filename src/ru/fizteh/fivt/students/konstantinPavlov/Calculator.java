package ru.fizteh.fivt.students.konstantinPavlov;

import java.util.LinkedList;

public class Calculator {

    public static void main(String[] args) {

        // compiling input expression
        StringBuilder builder = new StringBuilder();
        String str = new String();
        for (int i = 0; i < args.length; ++i) {
            builder.append(args[i]).append(" ");
        }

        str = builder.toString();

        try {
            str = str.trim();

            // checking if input expression is empty
            if (str.isEmpty()) {
                System.err
                        .println("Error: the expression is empty. No arguments.");
                return;
            }

            // checking brackets
            int closedBrakets = 0, openedBrakets = 0;
            for (int i = 0; i < str.length(); ++i) {
                if (str.charAt(i) == '(')
                    ++openedBrakets;
                else if (str.charAt(i) == ')')
                    --openedBrakets;
                if (openedBrakets < 0) {
                    closedBrakets = 0;
                    break;
                }
            }
            if (closedBrakets != 0 || openedBrakets != 0) {
                System.err
                        .println("Error: wrong input. Something wrong with brakets.");
                return;
            }

            String[] parsedInput = parse(toRpn(str));

            if (!checkRpn(parsedInput)) {
                System.err.println("Error: wrong input");
                return;
            }

            System.out.println("Input: " + str);
            System.out.println("Answer: " + calculate(parsedInput));
        } catch (Exception expt) {
            System.err.println("Error: " + expt.getMessage());
        }
    }

    private static boolean checkRpn(String[] parsedInput) {
        int countOfDigits = 0;
        int countOfOperands = 0;
        for (String s : parsedInput) {
            if (s.equals("+") || s.equals("-") || s.equals("/")
                    || s.equals("*")) {
                ++countOfOperands;
            } else {
                ++countOfDigits;
            }
        }
        if ((countOfDigits - countOfOperands) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private static String toRpn(String str) {
        MyStack<Character> s = new MyStack<Character>();
        String resStr = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); ++i) {
            char cur;
            if (Character.isDigit(str.charAt(i))) {
                builder.append(str.charAt(i));
                if (i != str.length() - 1
                        && !Character.isDigit(str.charAt(i + 1))) {
                    builder.append(",");
                } else if (i == str.length() - 1)
                    builder.append(",");
            } else {
                if (str.charAt(i) == '(') {
                    s.push(str.charAt(i));
                } else {
                    if (str.charAt(i) == '+' || str.charAt(i) == '-'
                            || str.charAt(i) == '/' || str.charAt(i) == '*') {
                        while (!s.isEmpty()) {
                            if (s.peek() == '/'
                                    || s.peek() == '*'
                                    || ((s.peek() == '-' || s.peek() == '+') && (str
                                            .charAt(i) == '-' || str.charAt(i) == '+'))) {
                                builder.append(s.pop().charValue()).append(",");
                            } else
                                break;
                            if (s.isEmpty() || s.peek() == '(') {
                                break;
                            }
                        }
                        s.push(str.charAt(i));
                    } else {
                        if (str.charAt(i) == ')') {
                            while (!s.isEmpty() && s.peek() != '(') {
                                builder.append(s.pop().charValue()).append(",");
                            }
                        } else {
                            if (!Character.isWhitespace(str.charAt(i))
                                    && str.charAt(i) != '\n'
                                    && str.charAt(i) != '\t') {
                                return "";
                            }
                        }
                    }
                }
            }
            if (i == str.length() - 1) {
                while (!s.isEmpty()) {
                    if (!s.isEmpty()) {
                        cur = s.pop().charValue();
                        if (cur != '(')
                            builder.append(cur).append(",");
                    }
                }
            }
        }
        resStr = builder.toString();
        return resStr;
    }

    private static String[] parse(String str) {
        String[] resArray = str.split(",");
        return resArray;
    }

    private static int calculate(String[] parsedInput) throws RuntimeException {
        int res;
        LinkedList<String> s = new LinkedList<String>();
        s.push(parsedInput[0]);
        for (int i = 1; i < parsedInput.length; ++i) {
            if (parsedInput[i].equals("+") || parsedInput[i].equals("-")
                    || parsedInput[i].equals("/") || parsedInput[i].equals("*")) {
                if (s.size() >= 2) {
                    int b = Integer.parseInt(s.pop());
                    int a = Integer.parseInt(s.pop());
                    int cur = 0;
                    if (parsedInput[i].equals("+")) {
                        cur = a + b;
                        if (cur - a != b) {
                            System.err.println("Error: Integer overflow");
                            System.exit(1);
                        }
                        s.push(Integer.toString(cur));
                    }
                    if (parsedInput[i].equals("-")) {
                        cur = a - b;
                        if (cur + b != a) {
                            System.err.println("Error: Integer overflow");
                            System.exit(1);
                        }
                        s.push(Integer.toString(cur));
                    }
                    if (parsedInput[i].equals("/")) {
                        if (b == 0) {
                            System.err.println("Error: Division by 0");
                            System.exit(1);
                        }
                        cur = a / b;
                        s.push(Integer.toString(cur));
                    }
                    if (parsedInput[i].equals("*")) {
                        cur = a * b;
                        if (cur / a != b) {
                            System.err.println("Error: Integer overflow");
                            System.exit(1);
                        }
                        s.push(Integer.toString(cur));
                    }
                }
            } else {
                s.push(parsedInput[i]);
            }
        }
        res = Integer.parseInt(s.pop());
        return res;
    }
}