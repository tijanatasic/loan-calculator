package com.calculator.app.controller;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.service.LoanCalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("loan-calculator")
@RequiredArgsConstructor
@Slf4j
public class LoanCalculatorController {

    private final LoanCalculationService loanCalculationService;

    @PostMapping("/calculate")
    public ResponseEntity<LoanCalculationResponseDto> calculateLoan(@Valid @RequestBody LoanCalculationRequestDto loanCalculationRequestDto) {
        log.info("Received request for calculating loan with request body: {}", loanCalculationRequestDto);
        LoanCalculationResponseDto response = loanCalculationService.calculateLoan(loanCalculationRequestDto);
        log.info("Returning successfully calculated loan response: {}", response);
        return ResponseEntity.ok().body(response);
    }

}
