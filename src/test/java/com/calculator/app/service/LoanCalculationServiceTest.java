package com.calculator.app.service;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationByMonthDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.entity.LoanCalculationRequestEntity;
import com.calculator.app.entity.LoanCalculationResultEntity;
import com.calculator.app.entity.LoanCalculationTotalResultEntity;
import com.calculator.app.mapper.LoanCalculationRequestMapper;
import com.calculator.app.mapper.LoanCalculationResponseMapper;
import com.calculator.app.repository.LoanCalculationRequestEntityRepository;
import com.calculator.app.repository.LoanCalculationTotalResultEntityRepository;
import com.calculator.app.service.impl.LoanCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanCalculationServiceTest {

    @InjectMocks
    private LoanCalculationServiceImpl loanCalculationService;

    @Mock
    private LoanCalculationRequestEntityRepository loanCalculationRequestEntityRepository;

    @Mock
    private LoanCalculationTotalResultEntityRepository loanCalculationTotalResultEntityRepository;

    @Mock
    private LoanCalculationRequestMapper loanCalculationRequestMapper;

    @Mock
    private LoanCalculationResponseMapper loanCalculationResponseMapper;

    @Test
    public void testCalculateLoan() {
        //given
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto();
        LoanCalculationRequestEntity loanCalculationRequestEntityMapped = getLoanRequestEntityMapped();
        LoanCalculationRequestEntity loanCalculationRequestEntitySaved = getLoanRequestEntitySaved();
        LoanCalculationTotalResultEntity loanCalculationTotalResult = generateLoanCalculationTotalResultEntity();
        LoanCalculationResponseDto loanCalculationResponseDto = generateLoanCalculationResponseDto();

        //when
        when(loanCalculationRequestMapper.mapLoanCalculationRequestDtoToEntity(any()))
                .thenReturn(loanCalculationRequestEntityMapped);
        when(loanCalculationRequestEntityRepository.save(any()))
                .thenReturn(loanCalculationRequestEntitySaved);
        when(loanCalculationTotalResultEntityRepository.save(any()))
                .thenReturn(loanCalculationTotalResult);
        when(loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(any()))
                .thenReturn(loanCalculationResponseDto);

        LoanCalculationResponseDto responseDto = loanCalculationService.calculateLoan(loanCalculationRequestDto);

        //then
        assertNotNull(responseDto);
        assertEquals(loanCalculationRequestDto.getNumberOfMonths(), responseDto.getCalculationByMonth().size());
        assertEquals(BigDecimal.valueOf(1023.06), responseDto.getTotalPaymentAmount());
        assertEquals(BigDecimal.valueOf(23.06), responseDto.getTotalInterestAmount());

        LoanCalculationByMonthDto firstResult = responseDto.getCalculationByMonth().getFirst();
        assertEquals(1, firstResult.getMonthValue());
        assertEquals(BigDecimal.valueOf(102.31), firstResult.getPaymentAmount());
        assertEquals(BigDecimal.valueOf(98.14), firstResult.getPrincipalAmount());
        assertEquals(BigDecimal.valueOf(4.17), firstResult.getInterestAmount());
        assertEquals(BigDecimal.valueOf(901.86), firstResult.getBalanceOwed());

        LoanCalculationByMonthDto lastResult = responseDto.getCalculationByMonth().getLast();
        assertEquals(10, lastResult.getMonthValue());
        assertEquals(BigDecimal.valueOf(102.27), lastResult.getPaymentAmount());
        assertEquals(BigDecimal.valueOf(0.42), lastResult.getInterestAmount());
        assertEquals(BigDecimal.valueOf(101.85), lastResult.getPrincipalAmount());
        assertEquals(BigDecimal.valueOf(0.00), lastResult.getBalanceOwed());

        verify(loanCalculationRequestEntityRepository, times(1)).save(any());
        verify(loanCalculationTotalResultEntityRepository, times(1)).save(any());
        verify(loanCalculationRequestMapper, times(1)).mapLoanCalculationRequestDtoToEntity(any());
        verify(loanCalculationResponseMapper, times(1)).mapLoanCalculationTotalResultEntityToDto(any());

    }

    private LoanCalculationRequestEntity getLoanRequestEntityMapped() {
        LoanCalculationRequestEntity loanCalculationRequest = new LoanCalculationRequestEntity();
        loanCalculationRequest.setAmount(BigDecimal.valueOf(1000));
        loanCalculationRequest.setNumberOfMonths(10);
        loanCalculationRequest.setAnnualInterestPercentage(BigDecimal.valueOf(5));
        return loanCalculationRequest;
    }

    private LoanCalculationRequestEntity getLoanRequestEntitySaved() {
        LoanCalculationRequestEntity loanCalculationRequest = new LoanCalculationRequestEntity();
        loanCalculationRequest.setAmount(BigDecimal.valueOf(1000));
        loanCalculationRequest.setNumberOfMonths(10);
        loanCalculationRequest.setAnnualInterestPercentage(BigDecimal.valueOf(5));
        loanCalculationRequest.setId(1L);
        loanCalculationRequest.setCreatedOn(LocalDateTime.now());
        return loanCalculationRequest;
    }

    private LoanCalculationRequestDto generateLoanCalculationRequestDto() {
        LoanCalculationRequestDto loanCalculationRequestDto = new LoanCalculationRequestDto();
        loanCalculationRequestDto.setAmount(BigDecimal.valueOf(1000));
        loanCalculationRequestDto.setNumberOfMonths(10);
        loanCalculationRequestDto.setAnnualInterestPercentage(BigDecimal.valueOf(5));
        return loanCalculationRequestDto;
    }

    private LoanCalculationTotalResultEntity generateLoanCalculationTotalResultEntity() {
        LoanCalculationTotalResultEntity loanCalculationTotalResult = new LoanCalculationTotalResultEntity();
        List<LoanCalculationResultEntity> loanCalculationResultEntities = new ArrayList<>();
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(1L, 1, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.14), BigDecimal.valueOf(4.17), BigDecimal.valueOf(901.86), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(2L, 2, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.55), BigDecimal.valueOf(3.76), BigDecimal.valueOf(803.31), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(3L, 3, BigDecimal.valueOf(102.31), BigDecimal.valueOf(98.96), BigDecimal.valueOf(3.35), BigDecimal.valueOf(704.35), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(4L, 4, BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.38), BigDecimal.valueOf(2.93), BigDecimal.valueOf(604.97), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(5L, 5, BigDecimal.valueOf(102.31), BigDecimal.valueOf(99.79), BigDecimal.valueOf(2.52), BigDecimal.valueOf(505.18), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(6L, 6, BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.21), BigDecimal.valueOf(2.10), BigDecimal.valueOf(404.97), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(7L, 7, BigDecimal.valueOf(102.31), BigDecimal.valueOf(100.62), BigDecimal.valueOf(1.69), BigDecimal.valueOf(304.35), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(8L, 8, BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.04), BigDecimal.valueOf(1.27), BigDecimal.valueOf(203.31), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(9L, 9, BigDecimal.valueOf(102.31), BigDecimal.valueOf(101.46), BigDecimal.valueOf(0.85), BigDecimal.valueOf(101.85), loanCalculationTotalResult));
        loanCalculationResultEntities.add(new LoanCalculationResultEntity(10L, 10, BigDecimal.valueOf(102.27), BigDecimal.valueOf(101.85), BigDecimal.valueOf(0.42), BigDecimal.valueOf(0.00), loanCalculationTotalResult));

        loanCalculationTotalResult.setLoanCalculationResults(loanCalculationResultEntities);
        loanCalculationTotalResult.setTotalPaymentAmount(BigDecimal.valueOf(1023.06));
        loanCalculationTotalResult.setTotalInterestAmount(BigDecimal.valueOf(23.06));
        loanCalculationTotalResult.setCreatedOn(LocalDateTime.now());
        loanCalculationTotalResult.setId(1L);

        return loanCalculationTotalResult;

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
}
