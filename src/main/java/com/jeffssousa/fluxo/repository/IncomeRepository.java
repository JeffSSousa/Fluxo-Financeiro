package com.jeffssousa.fluxo.repository;

import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {


    @Query("""
       SELECT COALESCE(SUM(i.amount), 0)
       FROM Income i
       WHERE i.user = :user
       """)
    BigDecimal sumByUser(@Param("user") User user);

    List<Income> findAllByUser(User user);
}
