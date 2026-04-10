package com.jeffssousa.fluxo.builders;

import com.jeffssousa.fluxo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTestBuilder {

    private UUID userId;
    private String email = "test@email.com";
    private String password = "123456";

    private List<String> roles = List.of("USER");

    private List<Income> incomes = new ArrayList<>();
    private List<Expense> expenses = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    private UserProfile profile;

    private UserTestBuilder() {}

    public static UserTestBuilder aUser() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserTestBuilder withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public UserTestBuilder withIncomes(List<Income> incomes) {
        this.incomes = incomes;
        return this;
    }

    public UserTestBuilder withExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public UserTestBuilder withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public UserTestBuilder withProfile(UserProfile profile) {
        this.profile = profile;
        return this;
    }

    public User build() {
        User user = new User();

        user.setUserId(userId);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setIncomes(incomes);
        user.setExpenses(expenses);
        user.setCategories(categories);
        user.setProfile(profile);

        return user;
    }
}