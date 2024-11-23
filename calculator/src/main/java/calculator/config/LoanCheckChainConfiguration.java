package calculator.config;

import calculator.service.util.loancheck.AgeCheckHandler;
import calculator.service.util.loancheck.EmploymentPositionCheckHandler;
import calculator.service.util.loancheck.EmploymentStatusCheckHandler;
import calculator.service.util.loancheck.GenderCheckHandler;
import calculator.service.util.loancheck.InsuranceEnabledAndSalaryClientCheckHandler;
import calculator.service.util.loancheck.LoanCheckHandler;
import calculator.service.util.loancheck.MaritalStatusCheckHandler;
import calculator.service.util.loancheck.SalaryAndAmountCheckHandler;
import calculator.service.util.loancheck.WorkExperienceCheckHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoanCheckChainConfiguration {

    @Bean
    public List<LoanCheckHandler> loanCheckHandlers(
            EmploymentStatusCheckHandler employmentStatusHandler,
            EmploymentPositionCheckHandler employmentPositionHandler,
            SalaryAndAmountCheckHandler salaryAndAmountHandler,
            MaritalStatusCheckHandler maritalStatusHandler,
            AgeCheckHandler ageHandler,
            GenderCheckHandler genderHandler,
            WorkExperienceCheckHandler workExperienceHandler,
            InsuranceEnabledAndSalaryClientCheckHandler insuranceEnabledHandler) {

        return List.of(
                employmentStatusHandler,
                employmentPositionHandler,
                salaryAndAmountHandler,
                maritalStatusHandler,
                ageHandler,
                genderHandler,
                workExperienceHandler,
                insuranceEnabledHandler
        );
    }
}
