package com.pnevsky.calculator.usecasses.impl;

import com.pnevsky.calculator.usecasses.LoanOfferService;
import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class LoanOfferServiceImpl implements LoanOfferService {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Value("${loan.insurance-price}")
    private BigDecimal insurancePrice;

    @Value("${loan.salary-client-rate-discount}")
    private BigDecimal salaryClientDiscount;

    @Value("${loan.insurance-rate-discount}")
    private BigDecimal insuranceRateDiscount;

    @Override
    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto request) {
        log.info("Starting loan offers generation with request: {}", request);

        List<LoanOfferDto> offers = new ArrayList<>();
        BigDecimal requestedAmount = request.amount();
        Integer term = request.term();

        log.info("Input values - Requested Amount: {}, Term: {}", requestedAmount, term);

        for (boolean isInsuranceEnabled : new boolean[]{false, true}) {
            for (boolean isSalaryClient : new boolean[]{false, true}) {
                BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);
                BigDecimal totalAmount = requestedAmount.add(isInsuranceEnabled ? insurancePrice : BigDecimal.ZERO);
                BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, rate, term);

                log.debug("Intermediate values - IsInsuranceEnabled: {}, IsSalaryClient: {}, Rate: {}, TotalAmount: {}, MonthlyPayment: {}",
                        isInsuranceEnabled, isSalaryClient, rate, totalAmount, monthlyPayment);

                LoanOfferDto offer = LoanOfferDto.builder()
                        .withStatementId(UUID.randomUUID())
                        .withRequestedAmount(requestedAmount)
                        .withTotalAmount(totalAmount)
                        .withTerm(term)
                        .withMonthlyPayment(monthlyPayment)
                        .withRate(rate)
                        .withIsInsuranceEnabled(isInsuranceEnabled)
                        .withIsSalaryClient(isSalaryClient)
                        .build();

                offers.add(offer);
            }
        }

        offers.sort(Comparator.comparing(LoanOfferDto::rate));
        log.info("Generated loan offers: {}", offers);

        return offers;
    }

    private BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.debug("Calculating rate with IsInsuranceEnabled: {}, IsSalaryClient: {}", isInsuranceEnabled, isSalaryClient);

        BigDecimal rate = baseRate;

        if (isInsuranceEnabled) {
            rate = rate.subtract(insuranceRateDiscount);
        }

        if (isSalaryClient) {
            rate = rate.subtract(salaryClientDiscount);
        }

        log.debug("Calculated rate: {}", rate);
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
}
