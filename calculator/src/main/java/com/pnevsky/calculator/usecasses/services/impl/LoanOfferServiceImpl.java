package com.pnevsky.calculator.usecasses.services.impl;

import com.pnevsky.calculator.usecasses.services.LoanOfferService;
import com.pnevsky.calculator.usecasses.dto.LoanOfferDto;
import com.pnevsky.calculator.usecasses.dto.LoanStatementRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import static com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate.calculateInsurancePrice;
import static com.pnevsky.calculator.usecasses.services.util.ServiceForCalculate.calculateMonthlyPayment;

@Slf4j
@Service
public class LoanOfferServiceImpl implements LoanOfferService {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Value("${loan.salary-client-rate-discount}")
    private BigDecimal salaryClientDiscount;

    @Value("${loan.insurance-rate-discount}")
    private BigDecimal insuranceRateDiscount;

    private BigDecimal insurancePrice;

    @Override
    public List<LoanOfferDto> generateLoanOffers(LoanStatementRequestDto request) {
        log.info("Starting loan offers generation with request: {}", request);

        List<LoanOfferDto> offers = new ArrayList<>();
        BigDecimal requestedAmount = request.amount();
        Integer term = request.term();

        log.info("Input values - Requested Amount: {}, Term: {}", requestedAmount, term);
        insurancePrice = calculateInsurancePrice(requestedAmount);
        log.info("Insurance price will be: {}", insurancePrice);

        for (boolean isInsuranceEnabled : new boolean[]{false, true}) {
            for (boolean isSalaryClient : new boolean[]{false, true}) {

                BigDecimal rate = calculateRateForLoanOffers(isInsuranceEnabled, isSalaryClient);
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

        offers.sort(Comparator.comparing(LoanOfferDto::rate).reversed());
        log.info("Generated loan offers: {}", offers);

        return offers;
    }

    private BigDecimal calculateRateForLoanOffers(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.debug("Calculating rate with IsInsuranceEnabled: {}, IsSalaryClient: {}", isInsuranceEnabled, isSalaryClient);
        BigDecimal rate = baseRate;
        if (isInsuranceEnabled)
            rate = rate.subtract(insuranceRateDiscount);
        if (isSalaryClient)
            rate = rate.subtract(salaryClientDiscount);
        log.debug("Calculated rate: {}", rate);

        return rate;
    }
}
