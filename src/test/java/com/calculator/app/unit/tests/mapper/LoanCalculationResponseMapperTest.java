package com.calculator.app.unit.tests.mapper;

import com.calculator.app.dto.response.LoanCalculationByMonthDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.entity.LoanCalculationSingleResultEntity;
import com.calculator.app.entity.LoanCalculationTotalResultEntity;
import com.calculator.app.mapper.LoanCalculationResponseMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanCalculationResponseMapperTest {

    @InjectMocks
    private LoanCalculationResponseMapperImpl loanCalculationResponseMapper;

    @Test
    public void testMapLoanCalculationTotalResultEntityToDto_validData() {
        //given
        LoanCalculationSingleResultEntity result1 = new LoanCalculationSingleResultEntity();
        result1.setMonthValue(1);
        result1.setPaymentAmount(BigDecimal.valueOf(102.31));
        result1.setPrincipalAmount(BigDecimal.valueOf(98.14));
        result1.setInterestAmount(BigDecimal.valueOf(4.17));
        result1.setBalanceOwed(BigDecimal.valueOf(901.86));

        LoanCalculationTotalResultEntity totalResult = new LoanCalculationTotalResultEntity();
        totalResult.setTotalPaymentAmount(BigDecimal.valueOf(1023.06));
        totalResult.setTotalInterestAmount(BigDecimal.valueOf(23.06));
        totalResult.setLoanCalculationResults(Collections.singletonList(result1));

        //when
        LoanCalculationResponseDto responseDto = loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(totalResult);

        //then
        assertNotNull(responseDto);
        assertEquals(BigDecimal.valueOf(1023.06), responseDto.getTotalPaymentAmount());
        assertEquals(BigDecimal.valueOf(23.06), responseDto.getTotalInterestAmount());
        List<LoanCalculationByMonthDto> calculationByMonthDtos = responseDto.getCalculationByMonth();
        assertNotNull(calculationByMonthDtos);
        assertEquals(1, calculationByMonthDtos.size());

        LoanCalculationByMonthDto loanCalculationByMonthDto = calculationByMonthDtos.getFirst();
        assertEquals(1, loanCalculationByMonthDto.getMonthValue());
        assertEquals(BigDecimal.valueOf(102.31), loanCalculationByMonthDto.getPaymentAmount());
        assertEquals(BigDecimal.valueOf(98.14), loanCalculationByMonthDto.getPrincipalAmount());
        assertEquals(BigDecimal.valueOf(4.17), loanCalculationByMonthDto.getInterestAmount());
        assertEquals(BigDecimal.valueOf(901.86), loanCalculationByMonthDto.getBalanceOwed());
    }

    @Test
    public void testMapLoanCalculationTotalResultEntityToDto_nullInput() {
        //when
        LoanCalculationResponseDto responseDto = loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(null);

        //then
        assertNull(responseDto);
    }

    @Test
    public void testMapLoanCalculationTotalResultEntityToDto_emptyResults() {
        //given
        LoanCalculationTotalResultEntity totalResult = new LoanCalculationTotalResultEntity();
        totalResult.setTotalPaymentAmount(BigDecimal.valueOf(1023.06));
        totalResult.setTotalInterestAmount(BigDecimal.valueOf(23.06));
        totalResult.setLoanCalculationResults(Collections.emptyList());

        //when
        LoanCalculationResponseDto responseDto = loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(totalResult);

        //then
        assertNotNull(responseDto);
        assertNotNull(responseDto.getCalculationByMonth());
        assertTrue(responseDto.getCalculationByMonth().isEmpty());
    }

    @Test
    public void testMapLoanCalculationTotalResultEntityToDto_nullResults() {
        //given
        LoanCalculationTotalResultEntity totalResult = new LoanCalculationTotalResultEntity();
        totalResult.setTotalPaymentAmount(BigDecimal.valueOf(1023.06));
        totalResult.setTotalInterestAmount(BigDecimal.valueOf(23.06));
        totalResult.setLoanCalculationResults(null);

        //when
        LoanCalculationResponseDto responseDto = loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(totalResult);

        //then
        assertNotNull(responseDto);
        assertNull(responseDto.getCalculationByMonth());
    }
}
