package lessons.starter;

import lessons.ExpressionConfiguration;
import lessons.spel.Inventor;
import lessons.spel.PlaceOfBirth;
import lessons.utils.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

public class ExpressionLanguageStarter {

    private static ExpressionLanguageStarter starter;

    private static final Logger logger = LogManager.getLogger(ExpressionLanguageStarter.class);

    public static void main(String[] args) throws NoSuchMethodException {
        logger.info("Starting configuration...");

        starter = new ExpressionLanguageStarter();
        starter.helloWorld();
        starter.getValue1();
        starter.parserConfig();
        starter.configAnnotated();
        starter.features();
    }

    private void helloWorld() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        logger.info("helloWorld# Expression value: " + message);
    }

    private void getValue1() {
        Inventor tesla = new Inventor("Nikola Tesla", new Date(), "Serbian");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");

        EvaluationContext context = new StandardEvaluationContext(tesla);
        String name = (String) exp.getValue(context);
        logger.info("getValue1# Expression value: " + name);

        name = (String) exp.getValue(tesla);
        logger.info("getValue1# Expression value: " + name);
    }

    private void parserConfig() {
        class Demo {
            public List<String> list;
        }

        // Включение:
        // - автоматическая инициализация null сылок
        // - автоматическое "выращивание" коллекции
        SpelParserConfiguration config = new SpelParserConfiguration(true,true);
        ExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("list[3]");
        Demo demo = new Demo();
        Object o = expression.getValue(demo);
        logger.info("parserConfig# Expression value: " + demo.list.size()); //4
        logger.info("parserConfig# Expression value: " + o); // пустая строка
    }

    private void configAnnotated() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ExpressionConfiguration.class);
        Inventor inventor = context.getBean(Inventor.class);
        logger.info("configAnnotated# Expression value: " + inventor.getNameValued());
    }

    private void features() throws NoSuchMethodException {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();
        logger.info("features# Expression value: " + message);

        exp = parser.parseExpression("'Hello World'.concat('!')");
        message = (String) exp.getValue();
        logger.info("features# Expression value: " + message);

        exp = parser.parseExpression("'Hello World'.bytes.length");
        int length = (Integer) exp.getValue();
        logger.info("features# Expression value: " + length);

        exp = parser.parseExpression("new String('hello world').toUpperCase()");
        message = exp.getValue(String.class);
        logger.info("features# Expression value: " + message);

        double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
        logger.info("features# Expression value: " + avogadrosNumber);

        int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
        logger.info("features# Expression value: " + maxValue);

        boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
        logger.info("features# Expression value: " + trueValue);

        Object nullValue = parser.parseExpression("null").getValue();
        logger.info("features# Expression value: " + nullValue);

        List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue();
        logger.info("features# Expression value: " + numbers);

        List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue();
        logger.info("features# Expression value: " + listOfLists);

        Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue();
        logger.info("features# Expression value: " + inventorInfo);

        Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue();
        logger.info("features# Expression value: " + mapOfMaps);

        int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue();
        logger.info("features# Expression value: " + numbers1);

        int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue();
        logger.info("features# Expression value: " + numbers2);

        int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue();
        logger.info("features# Expression value: " + numbers3);

        trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
        logger.info("features# Expression value: " + trueValue);

        boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);
        logger.info("features# Expression value: " + falseValue);

        trueValue = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        logger.info("features# Expression value: " + trueValue);

        falseValue = parser.parseExpression("'xyz' instanceof T(int)").getValue(Boolean.class);
        logger.info("features# Expression value: " + falseValue);

        trueValue = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        logger.info("features# Expression value: " + trueValue);

        falseValue = parser.parseExpression("'5.0067' matches '\\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        logger.info("features# Expression value: " + falseValue);

        falseValue = parser.parseExpression("true and false").getValue(Boolean.class);
        logger.info("features# Expression value: " + falseValue);

        falseValue = parser.parseExpression("!true").getValue(Boolean.class);
        logger.info("features# Expression value: " + falseValue);

        int two = parser.parseExpression("1 + 1").getValue(Integer.class); // 2
        logger.info("features# Expression value: " + two);

        String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class); // test string
        logger.info("features# Expression value: " + testString);

        // Subtraction
        int four = parser.parseExpression("1 - -3").getValue(Integer.class); // 4
        logger.info("features# Expression value: " + four);

        double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class); // -9000
        logger.info("features# Expression value: " + d);

        // Multiplication
        int six = parser.parseExpression("-2 * -3").getValue(Integer.class); // 6
        logger.info("features# Expression value: " + six);

        double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class); // 24.0
        logger.info("features# Expression value: " + twentyFour);

        // Division
        int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class); // -2
        logger.info("features# Expression value: " + minusTwo);

        double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class); // 1.0
        logger.info("features# Expression value: " + one);

        // Modulus
        int three = parser.parseExpression("7 % 4").getValue(Integer.class); // 3
        logger.info("features# Expression value: " + three);

        one = parser.parseExpression("8 / 5 % 2").getValue(Integer.class); // 1
        logger.info("features# Expression value: " + one);

        // Operator precedence
        int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class); // -21
        logger.info("features# Expression value: " + minusTwentyOne);

        Inventor inventor = new Inventor();
        StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);

        parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");
        logger.info("features# Expression value: " + inventor.getName());

        // alternatively
        String aleks = parser.parseExpression("Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
        logger.info("features# Expression value: " + aleks);

        Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
        logger.info("features# Expression value: " + dateClass);

        Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);
        logger.info("features# Expression value: " + stringClass);

        trueValue = parser.parseExpression("T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR").getValue(Boolean.class);
        logger.info("features# Expression value: " + trueValue);

        Inventor einstein = parser.parseExpression("new lessons.spel.Inventor('Albert Einstein', 'German')").getValue(Inventor.class);
        logger.info("features# Expression value: " + einstein.getName());

        Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
        logger.info("features# Expression value: " + tesla.getName());
        StandardEvaluationContext context = new StandardEvaluationContext(tesla);
        context.setVariable("newName", "Mike Tesla");

        parser.parseExpression("Name = #newName").getValue(context);
        logger.info("features# Expression value: " + tesla.getName()); // "Mike Tesla"

        List<Integer> primes = new ArrayList<Integer>();
        primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));

        context = new StandardEvaluationContext();
        context.setVariable("primes", primes);

        // all prime numbers > 10 from the list (using selection ?{...})
        // evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression("#primes.?[#this>10]").getValue(context);
        logger.info("features# Expression value: " + primesGreaterThanTen);

        parser = new SpelExpressionParser();
        context = new StandardEvaluationContext();

        context.registerFunction("reverseString", StringUtils.class.getDeclaredMethod("reverseString", new Class[]{String.class}));

        String helloWorldReversed = parser.parseExpression("#reverseString('hello')").getValue(context, String.class);
        logger.info("features# Expression value: " + helloWorldReversed);

        String falseString = parser.parseExpression("false ? 'trueExp' : 'falseExp'").getValue(String.class);
        logger.info("features# Expression value: " + falseString);

        parser = new SpelExpressionParser();
        tesla = new Inventor("Nikola Tesla", "Serbian");
        context = new StandardEvaluationContext(tesla);
        String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class); //name != null ? name : "Elvis Presley";
        logger.info("features# Expression value: " + name); // Nikola Tesla
        tesla.setName(null);
        name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class); //name != null ? name : "Elvis Presley";
        logger.info("features# Expression value: " + name); // Elvis Presley

        parser = new SpelExpressionParser();
        tesla = new Inventor("Nikola Tesla", "Serbian");
        tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));
        context = new StandardEvaluationContext(tesla);
        String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
        logger.info("features# Expression value: " + city); // Smiljan
        tesla.setPlaceOfBirth(null);
        city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
        logger.info("features# Expression value: " + city); // null - does not throw NullPointerException!!!

        String randomPhrase = parser.parseExpression("random number is #{T(java.lang.Math).random()}", new TemplateParserContext()).getValue(String.class);
        logger.info("features# Expression value: " + randomPhrase);
    }
}
