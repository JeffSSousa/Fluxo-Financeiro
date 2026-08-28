package com.jeffssousa.fluxo.entities;

import com.jeffssousa.fluxo.enums.IncomeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_incomes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID incomeId;

    private String description;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private IncomeStatus status; // received/ not received

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
