package com.calculator.app.unit.tests.controller;

import com.calculator.app.controller.LoanCalculatorController;
import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationByMonthDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.service.LoanCalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanCalculatorControllerTest {

    @InjectMocks
    private LoanCalculatorController loanCalculatorController;

    @Mock
    private LoanCalculationService loanCalculationService;

    @Test
    public void testLoanCalculatorCreate() {
        // given
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto();
        LoanCalculationResponseDto loanCalculationResponseDto = generateLoanCalculationResponseDto();

        when(loanCalculationService.calculateLoan(loanCalculationRequestDto))
                .thenReturn(loanCalculationResponseDto);

        //when
        ResponseEntity<LoanCalculationResponseDto> response = loanCalculatorController.calculateLoan(loanCalculationRequestDto);

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loanCalculationRequestDto.getNumberOfMonths(), response.getBody().getCalculationByMonth().size());
        assertEquals(BigDecimal.valueOf(1023.06), response.getBody().getTotalPaymentAmount());
        assertEquals(BigDecimal.valueOf(23.06), response.getBody().getTotalInterestAmount());

        LoanCalculationByMonthDto firstResult = response.getBody().getCalculationByMonth().getFirst();
        assertEquals(1, firstResult.getMonthValue());
        assertEquals(BigDecimal.valueOf(102.31), firstResult.getPaymentAmount());
        assertEquals(BigDecimal.valueOf(98.14), firstResult.getPrincipalAmount());
        assertEquals(BigDecimal.valueOf(4.17), firstResult.getInterestAmount());
        assertEquals(BigDecimal.valueOf(901.86), firstResult.getBalanceOwed());

        LoanCalculationByMonthDto lastResult = response.getBody().getCalculationByMonth().getLast();
        assertEquals(10, lastResult.getMonthValue());
        assertEquals(BigDecimal.valueOf(102.27), lastResult.getPaymentAmount());
        assertEquals(BigDecimal.valueOf(0.42), lastResult.getInterestAmount());
        assertEquals(BigDecimal.valueOf(101.85), lastResult.getPrincipalAmount());
        assertEquals(BigDecimal.valueOf(0.00), lastResult.getBalanceOwed());

    }

    private LoanCalculationResponseDto generateLoanCalculationResponseDto() {
        LoanCalculationResponseDto loanCalculationResponseDto = new LoanCalculationResponseDto();
        loanCalculationResponseDto.setTotalPaymentAmount(BigDecimal.valueOf(1023.06));
        loanCalculationResponseDto.setTotalInterestAmount(BigDecimal.valueOf(23.06));

        List<LoanCalculationByMonthDto> loanCalculationByMonthDtoList = new ArrayList<>();
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(1, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.14), BigDecimal.valueOf(4.17), BigDecimal.valueOf(901.86)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(2, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.55), BigDecimal.valueOf(3.76), BigDecimal.valueOf(803.31)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(3, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.96), BigDecimal.valueOf(3.35), BigDecimal.valueOf(704.35)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(4, BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.38), BigDecimal.valueOf(2.93), BigDecimal.valueOf(604.97)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(5, BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.79), BigDecimal.valueOf(2.52), BigDecimal.valueOf(505.18)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(6, BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.21), BigDecimal.valueOf(2.10), BigDecimal.valueOf(404.97)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(7, BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.62), BigDecimal.valueOf(1.69), BigDecimal.valueOf(304.35)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(8, BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.04), BigDecimal.valueOf(1.27), BigDecimal.valueOf(203.31)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(9, BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.46), BigDecimal.valueOf(0.85), BigDecimal.valueOf(101.85)));
        loanCalculationByMonthDtoList.add(new LoanCalculationByMonthDto(10, BigDecimal.valueOf(102.27), BigDecimal.valueOf(101.85), BigDecimal.valueOf(0.42), BigDecimal.valueOf(0.00)));

        loanCalculationResponseDto.setCalculationByMonth(loanCalculationByMonthDtoList);
        return loanCalculationResponseDto;
    }

    private LoanCalculationRequestDto generateLoanCalculationRequestDto() {
        LoanCalculationRequestDto loanCalculationRequestDto = new LoanCalculationRequestDto();
        loanCalculationRequestDto.setAmount(BigDecimal.valueOf(1000));
        loanCalculationRequestDto.setNumberOfMonths(10);
        loanCalculationRequestDto.setAnnualInterestPercentage(BigDecimal.valueOf(5));
        return loanCalculationRequestDto;
    }
}
