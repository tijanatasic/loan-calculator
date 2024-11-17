package com.calculator.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationResponseDto {

    @JsonProperty(value = "totalPaymentAmount")
    private BigDecimal totalPaymentAmount;

    @JsonProperty(value = "totalInterestAmount")
    private BigDecimal totalInterestAmount;

    @JsonProperty(value = "calculationByMonth")
    private List<LoanCalculationByMonthDto> calculationByMonth;

}
