package com.jeffssousa.fluxo.builders;

import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseTestBuilder {

    private UUID expenseId;
    private String description = "Grocery shopping";
    private BigDecimal amount = BigDecimal.valueOf(100.00);
    private LocalDateTime transactionDate = LocalDateTime.now();
    private ExpenseStatus status = ExpenseStatus.NOT_PAID;
    private LocalDate dueDate = LocalDate.now().plusDays(7);

    private Category category;
    private User user;

    private ExpenseTestBuilder() {}

    public static ExpenseTestBuilder anExpense() {
        return new ExpenseTestBuilder();
    }

    public ExpenseTestBuilder withExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
        return this;
    }

    public ExpenseTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ExpenseTestBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseTestBuilder withTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public ExpenseTestBuilder withStatus(ExpenseStatus status) {
        this.status = status;
        return this;
    }

    public ExpenseTestBuilder withDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public ExpenseTestBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public ExpenseTestBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Expense build() {
        Expense expense = new Expense();

        expense.setExpenseId(expenseId);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setTransactionDate(transactionDate);
        expense.setStatus(status);
        expense.setDueDate(dueDate);
        expense.setCategory(category);
        expense.setUser(user);

        return expense;
    }
}