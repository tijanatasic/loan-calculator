package com.calculator.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationByMonthDto {

    @JsonProperty(value = "monthValue")
    private Integer monthValue;

    @JsonProperty(value = "paymentAmount")
    private BigDecimal paymentAmount;

    @JsonProperty(value = "principalAmount")
    private BigDecimal principalAmount;

    @JsonProperty(value = "interestAmount")
    private BigDecimal interestAmount;

    @JsonProperty(value = "balanceOwed")
    private BigDecimal balanceOwed;

}
