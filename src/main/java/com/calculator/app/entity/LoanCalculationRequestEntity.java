package com.calculator.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_calculation_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationRequestEntity {

    @Id
    @SequenceGenerator(name = "loan_calculation_request_id_seq", sequenceName = "loan_calculation_request_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_calculation_request_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "annual_interest_percentage")
    private BigDecimal annualInterestPercentage;

    @Column(name = "number_of_months")
    private Integer numberOfMonths;

    @Column(name = "created_on")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @OneToOne(mappedBy = "loanCalculationRequest", fetch = FetchType.LAZY)
    @ToString.Exclude
    private LoanCalculationTotalResultEntity loanCalculationTotalResult;

}
