package com.calculator.app.repository;

import com.calculator.app.entity.LoanCalculationResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanCalculationResultEntityRepository extends JpaRepository<LoanCalculationResultEntity, Long> {
}
