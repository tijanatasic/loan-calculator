package com.calculator.app.mapper;

import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.entity.LoanCalculationTotalResultEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LoanCalculationResponseMapper {

    @Mapping(target = "calculationByMonth", source = "loanCalculationResults")
    public abstract LoanCalculationResponseDto mapLoanCalculationTotalResultEntityToDto(LoanCalculationTotalResultEntity loanCalculationTotalResult);
}
