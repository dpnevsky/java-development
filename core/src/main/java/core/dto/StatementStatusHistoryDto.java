package core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import core.type.ApplicationStatusType;
import core.type.ChangeType;
import java.io.Serializable;
import java.sql.Timestamp;

@Builder(setterPrefix = "with")
public record StatementStatusHistoryDto(

        @Schema(description = "Status of the statement", example = "approved", allowableValues = "preapproval, approved, denied, signed")
        @NotNull(message = "Status is required")
        ApplicationStatusType status,

        @Schema(description = "Timestamp when status was changed", example = "2024-12-01T12:30:00")
        @NotNull(message = "Time of status change is required")
        Timestamp time,

        @Schema(description = "Type of status change", example = "manual", allowableValues = "automatic, manual")
        @NotNull(message = "Change type is required")
        ChangeType changeType
) implements Serializable {}
