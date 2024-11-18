package com.calculator.app.service.impl;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.calculator.app.entity.LoanCalculationRequestEntity;
import com.calculator.app.entity.LoanCalculationSingleResultEntity;
import com.calculator.app.entity.LoanCalculationTotalResultEntity;
import com.calculator.app.mapper.LoanCalculationRequestMapper;
import com.calculator.app.mapper.LoanCalculationResponseMapper;
import com.calculator.app.repository.LoanCalculationRequestEntityRepository;
import com.calculator.app.repository.LoanCalculationTotalResultEntityRepository;
import com.calculator.app.service.LoanCalculationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanCalculationServiceImpl implements LoanCalculationService {

    private final LoanCalculationRequestEntityRepository loanCalculationRequestEntityRepository;
    private final LoanCalculationTotalResultEntityRepository loanCalculationTotalResultEntityRepository;
    private final LoanCalculationRequestMapper loanCalculationRequestMapper;
    private final LoanCalculationResponseMapper loanCalculationResponseMapper;

    @Override
    @Transactional
    public LoanCalculationResponseDto calculateLoan(LoanCalculationRequestDto loanCalculationRequestDto) {
        log.debug("Entered method for calculating loan.");

        Integer numberOfMonths = loanCalculationRequestDto.getNumberOfMonths();
        BigDecimal loanAmount = loanCalculationRequestDto.getAmount();
        BigDecimal annualInterestPercentage = loanCalculationRequestDto.getAnnualInterestPercentage();

        log.debug("Saving loan calculation request entity.");
        LoanCalculationRequestEntity loanCalculationRequest = getLoanCalculationRequestEntityAndSave(loanCalculationRequestDto);
        log.debug("Successfully saved loan calculation request entity: {}", loanCalculationRequest);

        log.debug("Populating and saving loan calculation results.");
        LoanCalculationTotalResultEntity loanCalculationTotalResult = calculateGetAndSaveLoanCalculationResults(numberOfMonths, loanAmount, annualInterestPercentage, loanCalculationRequest);
        log.debug("Successfully saved loan calculation results: {}", loanCalculationTotalResult);

        LoanCalculationResponseDto loanCalculationResponseDto = loanCalculationResponseMapper.mapLoanCalculationTotalResultEntityToDto(loanCalculationTotalResult);
        log.debug("Successfully calculated and saved information about loan calculation request");
        return loanCalculationResponseDto;
    }

    private LoanCalculationTotalResultEntity calculateGetAndSaveLoanCalculationResults(Integer numberOfMonths, BigDecimal loanAmount, BigDecimal annualInterestPercentage, LoanCalculationRequestEntity loanCalculationRequestEntity) {
        BigDecimal monthlyInterestRate = calculateMonthlyInterestedRate(annualInterestPercentage);
        log.debug("Calculating total payment loanAmount for monthly interest rate: {}, {} months and loan amount: {}", monthlyInterestRate, numberOfMonths, loanAmount);
        BigDecimal calculatedFixedMonthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, numberOfMonths);
        log.debug("Calculated fixed monthly payment: {}", calculatedFixedMonthlyPayment);

        BigDecimal balanceOwed = loanAmount;
        int currentMonth = 1;
        BigDecimal totalPaymentAmount = BigDecimal.ZERO;
        LoanCalculationTotalResultEntity loanCalculationTotalResult = new LoanCalculationTotalResultEntity();

        while (currentMonth <= numberOfMonths) {

            BigDecimal currentMonthPaymentAmount = getCurrentMonthPaymentAmount(currentMonth, numberOfMonths, calculatedFixedMonthlyPayment, balanceOwed, monthlyInterestRate);
            BigDecimal interestPaymentAmount = balanceOwed.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPaymentAmount = currentMonthPaymentAmount.subtract(interestPaymentAmount).setScale(2, RoundingMode.HALF_UP);
            balanceOwed = balanceOwed.subtract(principalPaymentAmount).setScale(2, RoundingMode.HALF_UP);

            log.debug("Calculated payment: month - {}, interest payment loanAmount - {}, principal payment loanAmount - {}, balance owned - {}, current month payment - {}", currentMonth, interestPaymentAmount, principalPaymentAmount, balanceOwed, currentMonthPaymentAmount);

            LoanCalculationSingleResultEntity loanCalculationResult = generateLoanCalculationResultEntity(currentMonth, interestPaymentAmount, principalPaymentAmount, balanceOwed, currentMonthPaymentAmount, loanCalculationTotalResult);
            loanCalculationTotalResult.getLoanCalculationResults().add(loanCalculationResult);
            currentMonth++;
            totalPaymentAmount = totalPaymentAmount.add(currentMonthPaymentAmount);

        }

        loanCalculationTotalResult.setTotalPaymentAmount(totalPaymentAmount);
        loanCalculationTotalResult.setTotalInterestAmount(totalPaymentAmount.subtract(loanAmount));
        loanCalculationTotalResult.setLoanCalculationRequest(loanCalculationRequestEntity);

        log.debug("Returning generated LoanCalculationTotalResultEntity: {}", loanCalculationTotalResult);
        return loanCalculationTotalResultEntityRepository.save(loanCalculationTotalResult);

    }

    private BigDecimal calculateMonthlyInterestedRate(BigDecimal annualInterestPercentage) {
        return Objects.equals(annualInterestPercentage, BigDecimal.ZERO) ? BigDecimal.ZERO : annualInterestPercentage
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
    }

    private BigDecimal getCurrentMonthPaymentAmount(int currentMonth, Integer numberOfMonths, BigDecimal monthlyPaymentAmount, BigDecimal balanceOwned, BigDecimal monthlyInterestedRate) {
        if (currentMonth != numberOfMonths) {
            return monthlyPaymentAmount;
        }
        return calculateMonthlyPayment(balanceOwned, monthlyInterestedRate, 1);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, Integer numberOfMonths) {
        if (Objects.equals(monthlyInterestRate, BigDecimal.ZERO)) {
            return loanAmount.divide(BigDecimal.valueOf(numberOfMonths), 2, RoundingMode.HALF_UP);
        }
        return loanAmount
                .multiply(monthlyInterestRate)
                .multiply(monthlyInterestRate.add(BigDecimal.ONE).pow(numberOfMonths))
                .divide(((monthlyInterestRate.add(BigDecimal.ONE)).pow(numberOfMonths)
                        .subtract(BigDecimal.ONE)), 2, RoundingMode.HALF_UP);
    }

    private LoanCalculationSingleResultEntity generateLoanCalculationResultEntity(int currentMonth, BigDecimal interestPayment, BigDecimal principalPayment, BigDecimal remainingBalance, BigDecimal monthlyPayment, LoanCalculationTotalResultEntity loanCalculationTotalResult) {
        LoanCalculationSingleResultEntity loanCalculationResult = new LoanCalculationSingleResultEntity();
        loanCalculationResult.setBalanceOwed(remainingBalance);
        loanCalculationResult.setInterestAmount(interestPayment);
        loanCalculationResult.setMonthValue(currentMonth);
        loanCalculationResult.setPaymentAmount(monthlyPayment);
        loanCalculationResult.setPrincipalAmount(principalPayment);
        loanCalculationResult.setLoanCalculationTotalResult(loanCalculationTotalResult);
        return loanCalculationResult;
    }

    private LoanCalculationRequestEntity getLoanCalculationRequestEntityAndSave(LoanCalculationRequestDto loanCalculationRequestDto) {
        LoanCalculationRequestEntity loanCalculationRequest = loanCalculationRequestMapper.mapLoanCalculationRequestDtoToEntity(loanCalculationRequestDto);
        return loanCalculationRequestEntityRepository.save(loanCalculationRequest);
    }

}
