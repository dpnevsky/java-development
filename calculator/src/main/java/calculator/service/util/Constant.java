package calculator.service.util;

import java.math.BigDecimal;

public class Constant {
    public final static BigDecimal INITIAL_INSURANCE_PRICE = BigDecimal.ZERO;
    public final static BigDecimal ONE_HUNDRED_PERCENT = BigDecimal.valueOf(100);
    public final static BigDecimal TWELVE_MONTH = BigDecimal.valueOf(12);
    public final static BigDecimal INSURANCE_PRICE_IN_PERCENT = BigDecimal.valueOf(0.04);
    public final static BigDecimal MAX_INSURANCE_PRICE = BigDecimal.valueOf(100_000);
    public final static Integer TOTAL_AMOUNT_AS_FIRST_PAYMENT = 1;
    public final static int FEMALE_MIN_AGE_FOR_DISCOUNT = 32;
    public final static int FEMALE_MAX_AGE_FOR_DISCOUNT = 60;
    public final static int MALE_MIN_AGE_FOR_DISCOUNT = 30;
    public final static int MALE_MAX_AGE_FOR_DISCOUNT = 55;
    public final static BigDecimal DISCOUNT_FOR_FEMALE = BigDecimal.valueOf(3);
    public final static BigDecimal DISCOUNT_FOR_MALE = BigDecimal.valueOf(3);
    public final static BigDecimal INCREASE_RATE_FOR_NON_BINARY = BigDecimal.valueOf(7);
    public final static int MIN_AGE_FOR_CREDIT = 20;
    public final static int MAX_AGE_FOR_CREDIT = 65;
    public final static BigDecimal INCREASE_RATE_FOR_SELF_EMPLOYED = BigDecimal.valueOf(2);
    public final static BigDecimal INCREASE_RATE_FOR_BUSINESS_OWNER = BigDecimal.ONE;
    public final static BigDecimal DISCOUNT_FOR_INSURANCE = BigDecimal.ONE;
    public final static BigDecimal DISCOUNT_FOR_SALARY_CLIENT = BigDecimal.valueOf(3);
    public final static BigDecimal DISCOUNT_FOR_MARRIED = BigDecimal.valueOf(3);
    public final static BigDecimal INCREASE_RATE_FOR_DIVORCED = BigDecimal.ONE;
    public final static BigDecimal NUMBER_OF_SALARY = BigDecimal.valueOf(24);
    public final static int MIN_TOTAL_EXPERIENCE_FOR_CREDIT_IN_MONTH = 18;
    public final static int MIN_CURRENT_EXPERIENCE_FOR_CREDIT_IN_MONTH = 3;
}
