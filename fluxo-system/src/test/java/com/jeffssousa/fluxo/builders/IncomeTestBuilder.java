package com.jeffssousa.fluxo.builders;

import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.IncomeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class IncomeTestBuilder {

    private UUID incomeId;
    private String description = "Salary";
    private BigDecimal amount = BigDecimal.valueOf(5000.00);
    private LocalDateTime transactionDate = LocalDateTime.now();
    private IncomeStatus status = IncomeStatus.RECEIVED;

    private Category category;
    private User user;

    private IncomeTestBuilder() {}

    public static IncomeTestBuilder anIncome() {
        return new IncomeTestBuilder();
    }

    public IncomeTestBuilder withIncomeId(UUID incomeId) {
        this.incomeId = incomeId;
        return this;
    }

    public IncomeTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public IncomeTestBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public IncomeTestBuilder withTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public IncomeTestBuilder withStatus(IncomeStatus status) {
        this.status = status;
        return this;
    }

    public IncomeTestBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public IncomeTestBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Income build() {
        Income income = new Income();

        income.setIncomeId(incomeId);
        income.setDescription(description);
        income.setAmount(amount);
        income.setTransactionDate(transactionDate);
        income.setStatus(status);
        income.setCategory(category);
        income.setUser(user);

        return income;
    }
}