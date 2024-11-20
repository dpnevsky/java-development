package com.pnevsky.calculator.usecasses.services.util;

import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import static org.apache.poi.ss.formula.functions.Irr.irr;

@Slf4j
public class ServiceForCalculate {

    /**
     * Рассчитывает аннуитетный платеж на основе суммы кредита, процентной ставки и срока.
     * Формула расчета аннуитетного платежа Х = С * К
     * где X — аннуитетный платеж, С — сумма кредита, К — коэффициент аннуитета.
     * Коэффициент аннуитета считается:
     * К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
     * где М — месячная процентная ставка по кредиту, S — срок кредита в месяцах.
     *
     * @param totalAmount общая сумма кредита
     * @param rate        годовая процентная ставка
     * @param term        срок кредита в месяцах
     * @return аннуитетный платеж
     */
    public static BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {

        log.debug("Calculating monthly payment with TotalAmount: {}, Rate: {}, Term: {}", totalAmount, rate, term);

        BigDecimal M = rate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_EVEN);
        BigDecimal pow = BigDecimal.ONE.add(M).pow(term);
        BigDecimal K = M.multiply(pow).divide(pow.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_EVEN);
        BigDecimal monthlyPayment = totalAmount.multiply(K).setScale(0, RoundingMode.HALF_EVEN);

        log.debug("Calculated monthly payment: {}", monthlyPayment);
        return monthlyPayment;
    }

    /**
     * Рассчитывает стоимость страховки.
     * Стоимость страховки равняется 4%, но не больше 100_000.
     *
     * @param amount запрашиваемая сумма кредита
     * @return стоимость страховки
     */
    public static BigDecimal calculateInsurancePrice(BigDecimal amount){
        log.debug("Calculating insurance price with amount: {}", amount);
        BigDecimal insurancePrice = amount.multiply(BigDecimal.valueOf(0.04));
        if(insurancePrice.compareTo(BigDecimal.valueOf(100_000)) > 0)
            insurancePrice = BigDecimal.valueOf(100_000);
        log.debug("Calculated insurance price: {}", insurancePrice);
        return insurancePrice;
    }

    /**
     * В соответствии со статьёй 6 Федерального закона «О потребительском кредите (займе)» 353-ФЗ
     * Полная стоимость потребительского кредита (займа) определяется в процентах годовых по формуле:
     * ПСК = i x ЧБП x 100, где ПСК — полная стоимость кредита в процентах годовых с точностью до
     * третьего знака после запятой;
     * ЧБП — число базовых периодов в календарном году. Продолжительность календарного года признаётся
     * равной трёмстам шестидесяти пяти дням;
     * i — процентная ставка базового периода, выраженная в десятичной форме.
     */
    public static BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal monthlyPayment, Integer term, BigDecimal rate) {
        log.debug("Calculating PSK with TotalAmount: {}, Rate: {}, MonthlyPayment: {}, Term: {}", totalAmount, rate, monthlyPayment, term);

        double[] values = new double[term + 1];
        values[0] = totalAmount.multiply(BigDecimal.valueOf(-1)).doubleValue();
        for (int i = 0; i < term; i++) {
            values[i + 1] = monthlyPayment.doubleValue();
        }

        BigDecimal i = BigDecimal.valueOf(irr(values));
        BigDecimal psk = i.multiply(BigDecimal.valueOf(1200)).setScale(3,RoundingMode.HALF_UP);
        log.debug("Calculated PSK: {}", psk);
        return psk;
    }
}
