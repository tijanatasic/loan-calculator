package com.calculator.app.service;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;

public interface LoanCalculationService {
    LoanCalculationResponseDto calculateLoan(LoanCalculationRequestDto loanCalculationRequestDto);
}
