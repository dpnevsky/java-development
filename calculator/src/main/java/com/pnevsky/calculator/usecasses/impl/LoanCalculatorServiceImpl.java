package com.pnevsky.calculator.usecasses.impl;

import com.pnevsky.calculator.usecasses.LoanCalculatorService;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.PaymentScheduleElementDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(10);

    @Override
    public CreditDto calculateLoan(ScoringDataDto scoringData) {
        log.info("Starting loan calculation with scoring data: {}", scoringData);

        BigDecimal requestedAmount = scoringData.amount();
        Integer term = scoringData.term();
        boolean isInsuranceEnabled = scoringData.isInsuranceEnabled();
        boolean isSalaryClient = scoringData.isSalaryClient();

        log.info("Input values - Requested Amount: {}, Term: {}, IsInsuranceEnabled: {}, IsSalaryClient: {}",
                requestedAmount, term, isInsuranceEnabled, isSalaryClient);

        BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);
        log.info("Calculated rate: {}", rate);

        BigDecimal monthlyPayment = calculateMonthlyPayment(requestedAmount, rate, term);
        log.info("Calculated monthly payment: {}", monthlyPayment);

        BigDecimal psk = calculatePSK(requestedAmount, rate, monthlyPayment, term);
        log.info("Calculated PSK (Full Loan Cost): {}", psk);

        List<PaymentScheduleElementDto> paymentSchedule = generatePaymentSchedule(requestedAmount, rate, monthlyPayment, term);
        log.info("Generated payment schedule with {} elements", paymentSchedule.size());

        CreditDto creditDto = CreditDto.builder()
                .withAmount(requestedAmount)
                .withTerm(term)
                .withMonthlyPayment(monthlyPayment)
                .withRate(rate)
                .withPsk(psk)
                .withIsInsuranceEnabled(isInsuranceEnabled)
                .withIsSalaryClient(isSalaryClient)
                .withPaymentSchedule(paymentSchedule)
                .build();

        log.info("Loan calculation completed. Result: {}", creditDto);
        return creditDto;
    }

    private BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.debug("Calculating rate with IsInsuranceEnabled: {}, IsSalaryClient: {}", isInsuranceEnabled, isSalaryClient);
        BigDecimal rate = BASE_RATE;

        if (isInsuranceEnabled) {
            rate = rate.subtract(BigDecimal.valueOf(3));
        }

        if (isSalaryClient) {
            rate = rate.subtract(BigDecimal.valueOf(1));
        }

        log.debug("Final calculated rate: {}", rate);
        return rate;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        log.debug("Calculating monthly payment with TotalAmount: {}, Rate: {}, Term: {}", totalAmount, rate, term);

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        BigDecimal denominator = BigDecimal.ONE.subtract(
                BigDecimal.ONE.add(monthlyRate).pow(-term, java.math.MathContext.DECIMAL128)
        );

        BigDecimal monthlyPayment = totalAmount.multiply(monthlyRate).divide(denominator, 2, RoundingMode.HALF_UP);

        log.debug("Calculated monthly payment: {}", monthlyPayment);
        return monthlyPayment;
    }

    private BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal rate, BigDecimal monthlyPayment, Integer term) {
        log.debug("Calculating PSK with TotalAmount: {}, Rate: {}, MonthlyPayment: {}, Term: {}", totalAmount, rate, monthlyPayment, term);

        BigDecimal psk = monthlyPayment.multiply(BigDecimal.valueOf(term)).subtract(totalAmount);

        log.debug("Calculated PSK: {}", psk);
        return psk;
    }

    private List<PaymentScheduleElementDto> generatePaymentSchedule(BigDecimal totalAmount, BigDecimal rate, BigDecimal monthlyPayment, Integer term) {
        log.debug("Generating payment schedule with TotalAmount: {}, Rate: {}, MonthlyPayment: {}, Term: {}", totalAmount, rate, monthlyPayment, term);

        List<PaymentScheduleElementDto> paymentScheduleElementDtoList = new ArrayList<>();
        BigDecimal remainingDebt = totalAmount;

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            PaymentScheduleElementDto element = PaymentScheduleElementDto.builder()
                    .withNumber(i)
                    .withDate(java.time.LocalDate.now().plusMonths(i))
                    .withTotalPayment(monthlyPayment)
                    .withInterestPayment(interestPayment)
                    .withDebtPayment(debtPayment)
                    .withRemainingDebt(remainingDebt)
                    .build();

            log.debug("Generated payment schedule element: {}", element);
            paymentScheduleElementDtoList.add(element);
        }

        log.debug("Completed payment schedule generation");
        return paymentScheduleElementDtoList;
    }
}
