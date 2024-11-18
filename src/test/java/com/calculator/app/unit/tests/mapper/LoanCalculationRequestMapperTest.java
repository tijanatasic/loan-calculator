package com.calculator.app.unit.tests.mapper;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.entity.LoanCalculationRequestEntity;
import com.calculator.app.mapper.LoanCalculationRequestMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoanCalculationRequestMapperTest {

    @InjectMocks
    private LoanCalculationRequestMapperImpl loanCalculationRequestMapper;

    @Test
    public void testMapLoanCalculationRequestDtoToEntity_validDto() {
        //given
        LoanCalculationRequestDto dto = generateLoanCalculationRequestDto();

        //when
        LoanCalculationRequestEntity entity = loanCalculationRequestMapper.mapLoanCalculationRequestDtoToEntity(dto);

        //then
        assertNotNull(entity);
        assertEquals(BigDecimal.valueOf(1000), entity.getAmount());
        assertEquals(BigDecimal.valueOf(5), entity.getAnnualInterestPercentage());
        assertEquals(10, entity.getNumberOfMonths());
    }

    @Test
    public void testMapLoanCalculationRequestDtoToEntity_nullDto() {
        //when
        LoanCalculationRequestEntity entity = loanCalculationRequestMapper.mapLoanCalculationRequestDtoToEntity(null);

        //then
        assertNull(entity);
    }

    @Test
    public void testMapLoanCalculationRequestDtoToEntity_partialData() {
        //given
        LoanCalculationRequestDto dto = new LoanCalculationRequestDto();
        dto.setAmount(BigDecimal.valueOf(500));

        //when
        LoanCalculationRequestEntity entity = loanCalculationRequestMapper.mapLoanCalculationRequestDtoToEntity(dto);

        //then
        assertNotNull(entity);
        assertEquals(BigDecimal.valueOf(500), entity.getAmount());
        assertNull(entity.getAnnualInterestPercentage());
        assertNull(entity.getNumberOfMonths());
    }

    private LoanCalculationRequestDto generateLoanCalculationRequestDto() {
        LoanCalculationRequestDto loanCalculationRequestDto = new LoanCalculationRequestDto();
        loanCalculationRequestDto.setAmount(BigDecimal.valueOf(1000));
        loanCalculationRequestDto.setNumberOfMonths(10);
        loanCalculationRequestDto.setAnnualInterestPercentage(BigDecimal.valueOf(5));
        return loanCalculationRequestDto;
    }

}
