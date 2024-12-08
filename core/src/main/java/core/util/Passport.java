package core.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Passport implements Serializable {

    private UUID passportId;

    private String passportSeries;

    private String passportNumber;

    private String issueBranch;

    private LocalDate issueDate;
}
