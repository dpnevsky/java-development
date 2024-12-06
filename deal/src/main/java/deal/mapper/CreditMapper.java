package deal.mapper;

import core.dto.CreditDto;
import deal.persistence.model.Credit;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import java.util.UUID;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true), imports = {UUID.class})
public interface CreditMapper {

    Credit fromDtoToEntity(CreditDto creditDto);
}
