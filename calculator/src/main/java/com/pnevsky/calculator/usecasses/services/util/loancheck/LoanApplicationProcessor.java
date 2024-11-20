package com.pnevsky.calculator.usecasses.services.util.loancheck;

import com.pnevsky.calculator.api.exceptions.LoanCheckException;
import com.pnevsky.calculator.usecasses.dto.ScoringDataDto;

import java.math.BigDecimal;

public class LoanApplicationProcessor {
    public static BigDecimal processLoanApplication(ScoringDataDto scoringData, BigDecimal baseRate) throws LoanCheckException {
        LoanCheckHandler employmentStatusHandler = new EmploymentStatusCheckHandler();
        LoanCheckHandler employmentPositionHandler = new EmploymentPositionCheckHandler();
        LoanCheckHandler salaryAndAmountHandler = new SalaryAndAmountCheckHandler();
        LoanCheckHandler maritalStatusHandler = new MaritalStatusCheckHandler();
        LoanCheckHandler ageHandler = new AgeCheckHandler();
        LoanCheckHandler genderHandler = new GenderCheckHandler();
        LoanCheckHandler workExperienceHandler = new WorkExperienceCheckHandler();
        LoanCheckHandler insuranceEnabledAndSalaryClientCheckHandler = new InsuranceEnabledAndSalaryClientCheckHandler();

        // Настраиваем цепочку обработчиков
        employmentStatusHandler.setNext(employmentPositionHandler);
        employmentPositionHandler.setNext(salaryAndAmountHandler);
        salaryAndAmountHandler.setNext(maritalStatusHandler);
        maritalStatusHandler.setNext(ageHandler);
        ageHandler.setNext(genderHandler);
        genderHandler.setNext(workExperienceHandler);
        workExperienceHandler.setNext(insuranceEnabledAndSalaryClientCheckHandler);

        // Запускаем цепочку
        return employmentStatusHandler.handle(scoringData, baseRate);
    }
}
