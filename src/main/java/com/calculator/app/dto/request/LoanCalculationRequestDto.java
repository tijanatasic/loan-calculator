package com.calculator.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationRequestDto {

    @JsonProperty(value = "amount")
    @NotNull(message = "{loan.amount.null.validation}")
    @Min(value = 0, message = "{loan.amount.positive.value.validation}")
    private BigDecimal amount;

    @JsonProperty(value = "annualInterestPercentage")
    @NotNull(message = "{annual.interest.percentage.amount.null.validation}")
    @Min(value = 0, message = "{annual.interest.percentage.amount.positive.value.validation}")
    private BigDecimal annualInterestPercentage;

    @JsonProperty(value = "numberOfMonths")
    @NotNull(message = "{number.of.months.null.validation}")
    @Min(value = 1, message = "{number.of.months.positive.value.validation}")
    private Integer numberOfMonths;

}
