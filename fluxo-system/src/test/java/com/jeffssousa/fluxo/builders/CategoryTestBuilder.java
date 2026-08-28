package com.jeffssousa.fluxo.builders;

import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

public class CategoryTestBuilder {

    private Long categoryId;
    private String name = "Food";
    private CategoryType type = CategoryType.EXPENSE;

    private List<Income> incomes = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();

    private User user;

    private CategoryTestBuilder() {}

    public static CategoryTestBuilder aCategory() {
        return new CategoryTestBuilder();
    }

    public CategoryTestBuilder withCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public CategoryTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryTestBuilder withType(CategoryType type) {
        this.type = type;
        return this;
    }

    public CategoryTestBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public CategoryTestBuilder withIncomes(List<Income> incomes) {
        this.incomes = incomes;
        return this;
    }

    public CategoryTestBuilder withExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public Category build() {
        Category category = new Category(name, type, user);
        category.setCategoryId(categoryId);
        category.setIncomes(incomes);
        category.setExpenses(expenses);

        return category;
    }
}