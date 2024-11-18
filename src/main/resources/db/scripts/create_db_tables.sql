CREATE TABLE IF NOT EXISTS loan_calculation_request (
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC NOT NULL,
    annual_interest_percentage NUMERIC(10,2) NOT NULL,
    number_of_months INT NOT NULL,
    created_on TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS loan_calculation_total_result (
    id BIGSERIAL PRIMARY KEY,
    total_payment_amount NUMERIC(10,2) NOT NULL,
    total_interest_amount NUMERIC(10,2) NOT NULL,
    loan_calculation_request_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    FOREIGN KEY (loan_calculation_request_id) REFERENCES loan_calculation_request (id)
);

CREATE TABLE IF NOT EXISTS loan_calculation_single_result (
    id BIGSERIAL PRIMARY KEY,
    month_value INT NOT NULL,
    payment_amount NUMERIC(10,2) NOT NULL,
    principal_amount NUMERIC(10,2) NOT NULL,
    interest_amount NUMERIC(10,2) NOT NULL,
    balance_owed NUMERIC(10,2) NOT NULL,
    loan_calculation_total_result_id BIGINT NOT NULL,
    FOREIGN KEY (loan_calculation_total_result_id) REFERENCES loan_calculation_total_result (id)
);