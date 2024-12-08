package deal.mapper;

import deal.persistence.model.Client;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import core.dto.LoanStatementRequestDto;
import core.util.Passport;
import java.util.UUID;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true), imports = {UUID.class})
public interface ClientMapper {

    @Mapping(target = "clientId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "middleName", source = "middleName")
    @Mapping(target = "birthDate", source = "birthdate")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "passport", expression = "java(mapPassport(dto))")
    @Mapping(target = "employment", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    Client loanStatementRequestDtoToClient(LoanStatementRequestDto dto);

    default Passport mapPassport(LoanStatementRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return Passport.builder()
                .withPassportNumber(dto.passportNumber())
                .withPassportSeries(dto.passportSeries())
                .build();
    }
}
