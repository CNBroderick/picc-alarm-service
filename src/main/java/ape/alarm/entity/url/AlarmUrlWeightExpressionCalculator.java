package ape.alarm.entity.url;

import org.bklab.quark.util.text.StringExtractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AlarmUrlWeightExpressionCalculator {

    private static final char OPERATOR_GREATER = '>';
    private static final char OPERATOR_LESS = '<';
    private static final char OPERATOR_EQUAL = '=';
    private static final char OPERATOR_BETWEEN = '-';
    private static final char OPERATOR_OR = '|';
    private static final char OPERATOR_AND = '&';
    private static final String OPERATOR_ANDS = "&,，";

    private List<IExpression> expressions = new ArrayList<>();
    private boolean orCalculator = true;


    public AlarmUrlWeightExpressionCalculator() {
    }

    public AlarmUrlWeightExpressionCalculator(boolean orCalculator) {
        this.orCalculator = orCalculator;
    }

    public AlarmUrlWeightExpressionCalculator setExpression(String expression) {
        this.expressions = createExpressions(expression);
        return this;
    }

    public boolean calc(double number) {
        if (expressions.stream().filter(IExpression::valid).count() < 1) {
            return true;
        }
        return orCalculator
               ? expressions.stream().anyMatch(e -> e.calc(number))
               : expressions.stream().allMatch(e -> e.calc(number));
    }

    public boolean valid() {
        return expressions.stream().allMatch(IExpression::valid);
    }

    private List<IExpression> createExpressions(String expression) {
        List<IExpression> expressions = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (c == ' ') continue;

            if (OPERATOR_ANDS.indexOf(c) >= 0) {
                expressions.add(createExpresion(stringBuilder.toString()));
                stringBuilder = new StringBuilder();
                continue;
            }

            if (c == OPERATOR_OR || isOperator(c) || isNumber(c)) {
                stringBuilder.append(c);
                continue;
            }

            Expression e = new Expression(stringBuilder.toString());
            e.valid = false;
            expressions.add(e);
            stringBuilder = new StringBuilder();
        }
        if (!stringBuilder.isEmpty()) expressions.add(createExpresion(stringBuilder.toString()));
        return expressions;
    }

    private IExpression createExpresion(String string) {
        if (string.contains("|")) {
            ExpressionGroup expressionGroup = new ExpressionGroup();
            for (String s : string.split("\\|")) {
                expressionGroup.add(new Expression(s).parse());
            }
            return expressionGroup;
        }
        return new Expression(string).parse();
    }

    private boolean isOperator(char a) {
        return switch (a) {
            case OPERATOR_BETWEEN, OPERATOR_GREATER, OPERATOR_LESS, OPERATOR_EQUAL -> true;
            default -> false;
        };
    }

    private boolean isNumber(char a) {
        return a >= '0' && a <= '9';
    }

    private enum CalculatorEnum {
        GREATER(">", numbers -> numbers.length == 2 && numbers[1] > numbers[0]),
        LESS("<", numbers -> numbers.length == 2 && numbers[1] < numbers[0]),
        EQUAL("=", numbers -> numbers.length == 2 && numbers[1] == numbers[0]),
        GREATER_EQUAL(">=", numbers -> numbers.length == 2 && numbers[1] >= numbers[0]),
        LESS_EQUAL("<=", numbers -> numbers.length == 2 && numbers[1] <= numbers[0]),
        BETWEEN("-", numbers -> numbers.length == 3 && numbers[0] <= numbers[2] && numbers[1] >= numbers[2]),
        ;

        public final String operator;
        public final ICalculator calculator;

        CalculatorEnum(String operator, ICalculator calculator) {
            this.operator = operator;
            this.calculator = calculator;
        }
    }

    private interface ICalculator {
        boolean calc(double... numbers);
    }

    private interface IExpression {
        boolean calc(double number);

        boolean valid();
    }

    private static class ExpressionGroup implements IExpression {
        private final List<IExpression> expressions = new ArrayList<>();

        public ExpressionGroup(IExpression... expressions) {
            this(Arrays.asList(expressions));
        }

        public ExpressionGroup(List<IExpression> expressions) {
            this.expressions.addAll(expressions);
        }

        public ExpressionGroup add(IExpression expression) {
            this.expressions.add(expression);
            return this;
        }

        @Override
        public boolean calc(double number) {
            return expressions.stream().filter(IExpression::valid).count() < 1
                   || expressions.stream().anyMatch(expression -> expression.valid() && expression.calc(number));
        }

        @Override
        public boolean valid() {
            return expressions.stream().anyMatch(IExpression::valid);
        }
    }

    private static class Expression implements IExpression {
        private final String function;
        private CalculatorEnum calculator;
        private double[] numbers;
        private boolean valid;

        public Expression(String function) {
            this.function = function;
            this.valid = function != null;
        }

        public Expression parse() {
            if (!valid) return this;

            if (function.matches("\\d+-\\d+")) {
                this.calculator = CalculatorEnum.BETWEEN;
                String[] split = function.split("-");
                this.numbers = new double[2];
                this.numbers[0] = StringExtractor.parseDouble(split[0]);
                this.numbers[1] = StringExtractor.parseDouble(split[1]);
                return this;
            }

            String n = parseNumberString(function.toCharArray());
            if (n.length() < 1) {
                this.valid = false;
                return this;
            }
            this.numbers = new double[]{StringExtractor.parseDouble(n)};

            CalculatorEnum calculator = parseOperator(function);
            if (calculator == null) {
                this.valid = false;
                return this;
            }
            this.calculator = calculator;

            return this;
        }

        private String parseNumberString(char[] array) {
            StringBuilder n = new StringBuilder();
            for (char c : array) {
                if (isNumber(c)) n.append(c);
            }
            String number = n.toString();
            return new String(array).contains(number) ? number : "";
        }

        private CalculatorEnum parseOperator(String string) {
            StringBuilder n = new StringBuilder();
            char[] array = string.toCharArray();
            for (char c : array) {
                if (!isNumber(c)) n.append(c);
            }
            String operator = n.toString();
            if (!string.contains(operator)) return null;

            for (CalculatorEnum value : CalculatorEnum.values()) {
                if (Objects.equals(value.operator, operator)) return value;
            }
            return null;
        }

        private boolean isNumber(char c) {
            return c >= '0' && c <= '9';
        }

        public boolean calc(double number) {
            //noinspection SwitchStatementWithTooFewBranches
            return valid && switch (calculator) {
                case BETWEEN -> calculator.calculator.calc(numbers[0], numbers[1], number);
                default -> calculator.calculator.calc(numbers[0], number);
            };
        }

        @Override
        public boolean valid() {
            return valid;
        }
    }

}
