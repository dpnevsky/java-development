package deal.mapper;

import core.dto.CreditDto;
import deal.persistence.model.Credit;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface CreditMapper {

    @Mapping(target = "creditStatus", ignore = true)
    @Mapping(target = "creditId", ignore = true)
    Credit fromDtoToEntity(CreditDto creditDto);
}
