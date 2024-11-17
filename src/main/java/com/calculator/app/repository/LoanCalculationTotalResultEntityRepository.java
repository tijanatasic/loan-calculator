package com.calculator.app.repository;

import com.calculator.app.entity.LoanCalculationTotalResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanCalculationTotalResultEntityRepository extends JpaRepository<LoanCalculationTotalResultEntity, Long> {
}
