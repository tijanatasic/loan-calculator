package com.calculator.app.repository;

import com.calculator.app.entity.LoanCalculationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanCalculationRequestEntityRepository extends JpaRepository<LoanCalculationRequestEntity, Long> {
}
