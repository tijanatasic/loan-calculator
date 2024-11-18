package com.calculator.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "loan_calculation_single_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationSingleResultEntity {

    @Id
    @SequenceGenerator(name = "loan_calculation_single_result_id_seq", sequenceName = "loan_calculation_single_result_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_calculation_single_result_id_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "month_value")
    private Integer monthValue;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "principal_amount")
    private BigDecimal principalAmount;

    @Column(name = "interest_amount")
    private BigDecimal interestAmount;

    @Column(name = "balance_owed")
    private BigDecimal balanceOwed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_calculation_total_result_id", nullable = false)
    private LoanCalculationTotalResultEntity loanCalculationTotalResult;

}
