package calculator.service.impl;

import calculator.dto.CreditDto;
import calculator.dto.PaymentScheduleElementDto;
import calculator.dto.ScoringDataDto;
import calculator.exception.LoanCheckException;
import calculator.service.LoanCalculatorService;
import calculator.service.util.loancheck.LoanApplicationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static calculator.service.util.Constant.INITIAL_INSURANCE_PRICE;
import static calculator.service.util.Constant.ONE_HUNDRED_PERCENT;
import static calculator.service.util.Constant.TWELVE_MONTH;
import static calculator.service.util.ServiceForCalculate.calculateInsurancePrice;
import static calculator.service.util.ServiceForCalculate.calculateMonthlyPayment;
import static calculator.service.util.ServiceForCalculate.calculatePSK;

@Slf4j
@Service
public class LoanCalculatorServiceImpl implements LoanCalculatorService {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    private final LoanApplicationProcessor loanApplicationProcessor;

    public LoanCalculatorServiceImpl(LoanApplicationProcessor loanApplicationProcessor) {
        this.loanApplicationProcessor = loanApplicationProcessor;
    }

    @Override
    public CreditDto calculateLoan(ScoringDataDto scoringData) throws LoanCheckException {
        log.info("Starting loan calculation with scoring data: {}", scoringData);
        BigDecimal rate = loanApplicationProcessor.processLoanApplication(scoringData, baseRate);
        log.debug("Calculated rate: {}", rate);

        Boolean insuranceEnabled = scoringData.isInsuranceEnabled();
        Boolean isSalaryClient = scoringData.isSalaryClient();
        BigDecimal requestedAmount = scoringData.amount();
        BigDecimal insurancePrice = INITIAL_INSURANCE_PRICE;
        if(insuranceEnabled) {
            insurancePrice = calculateInsurancePrice(scoringData.amount());
            requestedAmount = scoringData.amount().add(insurancePrice);
        }
        log.debug("Insured price: {}, Requested amount with insurance price: {}", insurancePrice, requestedAmount);

        Integer term = scoringData.term();
        BigDecimal monthlyPayment = calculateMonthlyPayment(requestedAmount, rate, term);
        log.debug("Calculated monthly payment: {}", monthlyPayment);

        BigDecimal psk = calculatePSK(requestedAmount, monthlyPayment, term, rate);
        log.debug("Calculated PSK (Full Loan Cost): {}", psk);

        List<PaymentScheduleElementDto> paymentSchedule = generatePaymentSchedule(rate, monthlyPayment, term);
        log.debug("Generated payment schedule with {} elements", paymentSchedule.size());

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

        final BigDecimal[] remainingDebt = {monthlyPayment.multiply(BigDecimal.valueOf(term))};
        List<PaymentScheduleElementDto> paymentScheduleElementDtoList =
                IntStream.rangeClosed(1, term)
                        .mapToObj(i -> {
                            BigDecimal interestPayment = remainingDebt[0]
                                    .multiply(rate.divide(ONE_HUNDRED_PERCENT, 2, RoundingMode.HALF_UP))
                                    .divide(TWELVE_MONTH, 2, RoundingMode.HALF_UP);
                            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);

                            remainingDebt[0] = remainingDebt[0].subtract(monthlyPayment);

                            PaymentScheduleElementDto element = PaymentScheduleElementDto.builder()
                                    .withNumber(i)
                                    .withDate(LocalDate.now().plusMonths(i))
                                    .withTotalPayment(monthlyPayment)
                                    .withInterestPayment(interestPayment)
                                    .withDebtPayment(debtPayment)
                                    .withRemainingDebt(remainingDebt[0])
                                    .build();

                            log.debug("Generated payment schedule element: {}", element);
                            return element;
                        })
                        .collect(Collectors.toList());

        log.debug("Completed payment schedule generation");
        return paymentScheduleElementDtoList;
    }
}
