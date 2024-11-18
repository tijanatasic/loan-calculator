package com.calculator.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_calculation_total_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationTotalResultEntity {

    @Id
    @SequenceGenerator(name = "loan_calculation_total_result_id_seq", sequenceName = "loan_calculation_total_result_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_calculation_total_result_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_payment_amount")
    private BigDecimal totalPaymentAmount;

    @Column(name = "total_interest_amount")
    private BigDecimal totalInterestAmount;

    @Column(name = "created_on")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_calculation_request_id", referencedColumnName = "id")
    @ToString.Exclude
    private LoanCalculationRequestEntity loanCalculationRequest;

    @OneToMany(mappedBy = "loanCalculationTotalResult", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<LoanCalculationSingleResultEntity> loanCalculationResults = new ArrayList<>();

}
