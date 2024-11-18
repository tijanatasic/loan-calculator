package com.calculator.app.integration.tests;

import com.calculator.app.dto.request.LoanCalculationRequestDto;
import com.calculator.app.dto.response.LoanCalculationByMonthDto;
import com.calculator.app.dto.response.LoanCalculationResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static com.calculator.app.unit.tests.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class LoanCalculatorIT extends BaseIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void calculateLoan_success() {
        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = 10;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        LoanCalculationResponseDto loanCalculationResponseDto = response.body().as(LoanCalculationResponseDto.class);

        assertEquals(BigDecimal.valueOf(1023.06), loanCalculationResponseDto.getTotalPaymentAmount());
        assertEquals(BigDecimal.valueOf(23.06), loanCalculationResponseDto.getTotalInterestAmount());
        assertEquals(10, loanCalculationResponseDto.getCalculationByMonth().size());

        List<LoanCalculationByMonthDto> loanCalculationByMonthDtos = loanCalculationResponseDto.getCalculationByMonth();

        assertMonthValues(loanCalculationByMonthDtos.get(0), 1,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(98.14).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(4.17).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(901.86).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(1), 2,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(98.55).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(3.76).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(803.31).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(2), 3,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(98.96).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(3.35).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(704.35).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(3), 4,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(99.38).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(2.93).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(604.97).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(4), 5,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(99.79).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(2.52).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(505.18).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(5), 6,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(100.21).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(2.10).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(404.97).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(6), 7,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(100.62).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(1.69).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(304.35).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(7), 8,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(101.04).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(1.27).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(203.31).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(8), 9,
                BigDecimal.valueOf(102.31).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(101.46).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.85).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(101.85).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(9), 10,
                BigDecimal.valueOf(102.27).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(101.85).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.42).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP));

    }

    @Test
    void calculateLoan_annualInterestRareSuccess() {
        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.ZERO;
        Integer numberOfMonths = 2;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        LoanCalculationResponseDto loanCalculationResponseDto = response.body().as(LoanCalculationResponseDto.class);

        assertEquals(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP), loanCalculationResponseDto.getTotalPaymentAmount());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP), loanCalculationResponseDto.getTotalInterestAmount());
        assertEquals(2, loanCalculationResponseDto.getCalculationByMonth().size());

        List<LoanCalculationByMonthDto> loanCalculationByMonthDtos = loanCalculationResponseDto.getCalculationByMonth();

        assertMonthValues(loanCalculationByMonthDtos.get(0), 1,
                BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(1), 2,
                BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculateLoan_loanAmountAndAnnualInterestRareSuccess() {
        //given
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal annualInterestRate = BigDecimal.ZERO;
        Integer numberOfMonths = 2;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        LoanCalculationResponseDto loanCalculationResponseDto = response.body().as(LoanCalculationResponseDto.class);

        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), loanCalculationResponseDto.getTotalPaymentAmount());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), loanCalculationResponseDto.getTotalInterestAmount());
        assertEquals(2, loanCalculationResponseDto.getCalculationByMonth().size());

        List<LoanCalculationByMonthDto> loanCalculationByMonthDtos = loanCalculationResponseDto.getCalculationByMonth();

        assertMonthValues(loanCalculationByMonthDtos.get(0), 1,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

        assertMonthValues(loanCalculationByMonthDtos.get(1), 2,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculateLoan_nullAmount() {

        //given
        BigDecimal amount = null;
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = 10;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NULL_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeAmount() {

        //given
        BigDecimal amount = BigDecimal.valueOf(-1);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = 10;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NEGATIVE_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullInterestPrecentage() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = null;
        Integer numberOfMonths = 10;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NULL_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeInterestPercentage() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(-1);
        Integer numberOfMonths = 10;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NEGATIVE_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = -1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_LESS_THAN_ONE_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_zeroNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = 0;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_LESS_THAN_ONE_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = null;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(1, errorMessages.size());
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_NULL_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullAmountAndInterestPercentageAndNumberOfMonths() {

        //given
        BigDecimal amount = null;
        BigDecimal annualInterestRate = null;
        Integer numberOfMonths = null;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(3, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NULL_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NULL_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_NULL_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeAmountAndInterestPercentageAndNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(-1);
        BigDecimal annualInterestRate = BigDecimal.valueOf(-1);
        Integer numberOfMonths = -1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(3, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NEGATIVE_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NEGATIVE_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_LESS_THAN_ONE_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullAmountAndInterestPercentage() {

        //given
        BigDecimal amount = null;
        BigDecimal annualInterestRate = null;
        Integer numberOfMonths = 1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NULL_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NULL_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeAmountAndInterestPercentage() {

        //given
        BigDecimal amount = BigDecimal.valueOf(-1);
        BigDecimal annualInterestRate = BigDecimal.valueOf(-1);
        Integer numberOfMonths = 1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NEGATIVE_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NEGATIVE_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullAmountAndNumberOfMonths() {

        //given
        BigDecimal amount = null;
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = null;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NULL_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_NULL_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeAmountAndNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(-1);
        BigDecimal annualInterestRate = BigDecimal.valueOf(5);
        Integer numberOfMonths = -1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(AMOUNT_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(LOAN_AMOUNT_NEGATIVE_MESSAGE, errorMessages.get(AMOUNT_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_LESS_THAN_ONE_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_nullInterestPercentageAndNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = null;
        Integer numberOfMonths = null;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NULL_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_NULL_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    @Test
    void calculateLoan_negativeInterestPercentageAndNumberOfMonths() {

        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal annualInterestRate = BigDecimal.valueOf(-1);
        Integer numberOfMonths = -1;

        //when
        LoanCalculationRequestDto loanCalculationRequestDto = generateLoanCalculationRequestDto(amount, numberOfMonths, annualInterestRate);
        Response response = postCalculateRequest(loanCalculationRequestDto);

        //then
        Map<String, String> errorMessages = response.jsonPath().get("message");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.containsKey(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertTrue(errorMessages.containsKey(NUMBER_OF_MONTHS_ERROR_KEY));
        assertEquals(ANNUAL_INTEREST_RATE_NEGATIVE_MESSAGE, errorMessages.get(ANNUAL_INTEREST_PERCENTAGE_ERROR_KEY));
        assertEquals(NUMBER_OF_MONTHS_LESS_THAN_ONE_MESSAGE, errorMessages.get(NUMBER_OF_MONTHS_ERROR_KEY));

    }

    private Response postCalculateRequest(LoanCalculationRequestDto loanCalculationRequestDto) {

        try {
            return RestAssured.given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(loanCalculationRequestDto))
                    .when()
                    .post(CALCULATOR_BASE_PATH + "/calculate")
                    .then()
                    .log().all()
                    .extract()
                    .response();
        } catch (Exception ex) {
            log.error("Error occurred while sending POST /calculate request", ex);
            throw new RuntimeException("Error occurred while sending POST /calculate request");
        }

    }

    private LoanCalculationRequestDto generateLoanCalculationRequestDto(BigDecimal amount, Integer numberOfMonths, BigDecimal annualInterestPercentage) {
        LoanCalculationRequestDto loanCalculationRequestDto = new LoanCalculationRequestDto();
        loanCalculationRequestDto.setAmount(amount);
        loanCalculationRequestDto.setNumberOfMonths(numberOfMonths);
        loanCalculationRequestDto.setAnnualInterestPercentage(annualInterestPercentage);
        return loanCalculationRequestDto;
    }

    private void assertMonthValues(LoanCalculationByMonthDto loanCalculationByMonthDto, int expectedMonthValue, BigDecimal expectedPaymentAmount,
                                   BigDecimal expectedPrincipalAmount, BigDecimal expectedInterestAmount, BigDecimal expectedBalanceOwed) {
        assertEquals(expectedMonthValue, loanCalculationByMonthDto.getMonthValue());
        assertEquals(expectedPaymentAmount, loanCalculationByMonthDto.getPaymentAmount());
        assertEquals(expectedPrincipalAmount, loanCalculationByMonthDto.getPrincipalAmount());
        assertEquals(expectedInterestAmount, loanCalculationByMonthDto.getInterestAmount());
        assertEquals(expectedBalanceOwed, loanCalculationByMonthDto.getBalanceOwed());
    }
}
