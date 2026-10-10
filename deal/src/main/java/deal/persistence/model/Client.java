package deal.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import core.dto.EmploymentDto;
import core.util.Passport;
import core.type.GenderType;
import core.type.MaritalStatusType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "client")
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Client implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", updatable = false, nullable = false)
    private UUID clientId;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private GenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", length = 50)
    private MaritalStatusType maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "passport_id")
    private Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "employment_id")
    private EmploymentDto employment;

    @Column(name = "account_number")
    private String accountNumber;
}
