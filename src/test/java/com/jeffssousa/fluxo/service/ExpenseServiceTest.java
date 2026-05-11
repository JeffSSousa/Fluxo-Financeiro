package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.CategoryTestBuilder;
import com.jeffssousa.fluxo.builders.ExpenseTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.mapper.ExpenseMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.ExpenseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrentUserService userService;

    @Mock
    private ExpenseMapper mapper;

    @InjectMocks
    private ExpenseService service;

    @Nested
    class addExpense{

        @Test
        @DisplayName("Deve salvar uma despesa com sucesso onde a categoria ja exista")
        void shouldCreateAExpenseWithSuccess(){

            User user = UserTestBuilder.aUser().build();
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(1L)
                    .build();

            Expense expense = ExpenseTestBuilder.anExpense()
                    .build();

            ExpenseRequestDTO dto = new ExpenseRequestDTO(
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getTransactionDate(),
                    expense.getDueDate(),
                    expense.getStatus(),
                    category.getName()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toEntity(dto)).thenReturn(expense);
            when(categoryRepository.findByNameAndUserUserId(dto.category(), user.getUserId()))
                    .thenReturn(Optional.of(category));
            when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

            Expense expenseSaved = service.addExpense(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(mapper, times(1)).toEntity(dto);
            verify(categoryRepository, times(1)).findByNameAndUserUserId(
                    dto.category(),
                    user.getUserId()
            );
            verify(expenseRepository, times(1)).save(any(Expense.class));
            verify(categoryRepository, never()).save(any(Category.class));

            assertNotNull(expenseSaved);
            assertEquals(dto.description(), expenseSaved.getDescription());
            assertEquals(dto.amount(), expenseSaved.getAmount());
            assertEquals(dto.transactionDate(), expenseSaved.getTransactionDate());
            assertEquals(dto.dueDate(), expenseSaved.getDueDate());
            assertEquals(dto.status(), expenseSaved.getStatus());
            assertEquals(1L, expenseSaved.getCategory().getCategoryId());

        }

    }

}
