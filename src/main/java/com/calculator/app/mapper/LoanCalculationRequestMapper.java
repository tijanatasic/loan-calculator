package com.calculator.app.mapper;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.entity.LoanCalculationRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LoanCalculationRequestMapper {
    public abstract LoanCalculationRequestEntity mapLoanCalculationRequestDtoToEntity(LoanCalculationRequestDto loanCalculationRequestDto);
}
