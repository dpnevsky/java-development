package com.pnevsky.calculator.usecasses.services.impl;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.services.LoanCalculatorService;
import com.pnevsky.calculator.usecasses.dto.CreditDto;
import com.pnevsky.calculator.usecasses.dto.PaymentScheduleElementDto;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;
import com.pnevsky.calculator.usecasses.services.util.loancheck.LoanApplicationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import static com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate.calculateInsurancePrice;
import static com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate.calculateMonthlyPayment;
import static com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate.calculatePSK;

@Slf4j
@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Override
    public CreditDto calculateLoan(ScoringDataDto scoringData) throws LoanCheckException {
        log.info("Starting loan calculation with scoring data: {}", scoringData);
        BigDecimal rate = LoanApplicationProcessor.processLoanApplication(scoringData, baseRate);
        log.info("Calculated rate: {}", rate);

        Boolean insuranceEnabled = scoringData.isInsuranceEnabled();
        Boolean isSalaryClient = scoringData.isSalaryClient();
        BigDecimal requestedAmount = scoringData.amount();
        BigDecimal insurancePrice = BigDecimal.ZERO;
        if(insuranceEnabled) {
            insurancePrice = calculateInsurancePrice(scoringData.amount());
            requestedAmount = scoringData.amount().add(insurancePrice);
        }
        log.info("Insured price: {}, Requested amount with insurance price: {}", insurancePrice, requestedAmount);

        Integer term = scoringData.term();
        BigDecimal monthlyPayment = calculateMonthlyPayment(requestedAmount, rate, term);
        log.info("Calculated monthly payment: {}", monthlyPayment);

        BigDecimal psk = calculatePSK(requestedAmount, monthlyPayment, term, rate);
        log.info("Calculated PSK (Full Loan Cost): {}", psk);

        List<PaymentScheduleElementDto> paymentSchedule = generatePaymentSchedule(rate, monthlyPayment, term);
        log.info("Generated payment schedule with {} elements", paymentSchedule.size());

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(term));

        CreditDto creditDto = CreditDto.builder()
                .withAmount(totalPayment)
                .withTerm(term)
                .withMonthlyPayment(monthlyPayment)
                .withRate(rate)
                .withPsk(psk)
                .withIsInsuranceEnabled(insuranceEnabled)
                .withIsSalaryClient(isSalaryClient)
                .withPaymentSchedule(paymentSchedule)
                .build();

        log.info("Loan calculation completed. Result: {}", creditDto);
        return creditDto;
    }

    private List<PaymentScheduleElementDto> generatePaymentSchedule(BigDecimal rate, BigDecimal monthlyPayment, Integer term) {
        log.debug("Generating payment schedule with Rate: {}, MonthlyPayment: {}, Term: {}", rate, monthlyPayment, term);

        List<PaymentScheduleElementDto> paymentScheduleElementDtoList = new ArrayList<>();
        BigDecimal remainingDebt = monthlyPayment.multiply(BigDecimal.valueOf(term));

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(rate.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);

            remainingDebt = remainingDebt.subtract(monthlyPayment);

            PaymentScheduleElementDto element = PaymentScheduleElementDto.builder()
                    .withNumber(i)
                    .withDate(LocalDate.now().plusMonths(i))
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
