package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.CategoryTestBuilder;
import com.jeffssousa.fluxo.builders.ExpenseTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseRequestDTO;
import com.jeffssousa.fluxo.dto.expense.ExpenseResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        @DisplayName("Deve salvar uma despesa quando a categoria já existir")
        void shouldCreateExpenseWhenCategoryExists(){

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

        @Test
        @DisplayName("Deve salvar uma despesa quando a categoria não existir")
        void shouldCreateExpenseWhenCategoryDoesNotExist(){

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
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

            Expense expenseSaved = service.addExpense(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(mapper, times(1)).toEntity(dto);
            verify(categoryRepository, times(1)).findByNameAndUserUserId(
                    dto.category(),
                    user.getUserId()
            );
            verify(expenseRepository, times(1)).save(any(Expense.class));
            verify(categoryRepository, times(1)).save(any(Category.class));

            assertNotNull(expenseSaved);
            assertEquals(dto.description(), expenseSaved.getDescription());
            assertEquals(dto.amount(), expenseSaved.getAmount());
            assertEquals(dto.transactionDate(), expenseSaved.getTransactionDate());
            assertEquals(dto.dueDate(), expenseSaved.getDueDate());
            assertEquals(dto.status(), expenseSaved.getStatus());
            assertEquals(1L, expenseSaved.getCategory().getCategoryId());

        }

    }


    @Nested
    class getAll{

        @Test
        @DisplayName("Deve Buscar todas as despesas com sucesso")
        void shouldFindAllExpensesWithSuccess() {

            User user = UserTestBuilder.aUser().build();

            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(1L)
                    .build();

            Expense houseRental = ExpenseTestBuilder.anExpense()
                    .withCategory(category)
                    .withDescription("Aluguel")
                    .withAmount(BigDecimal.valueOf(1200))
                    .withTransactionDate(LocalDateTime.of(2025,
                            8,
                            5,
                            12,
                            50))
                    .build();

            Expense car = ExpenseTestBuilder.anExpense()
                    .withCategory(category)
                    .withDescription("Carro")
                    .withAmount(BigDecimal.valueOf(2200))
                    .withTransactionDate(LocalDateTime.of(2025,
                            8,
                            2,
                            11,
                            50))
                    .build();

            CategoryResponseDTO categoryDto = new CategoryResponseDTO(
                    houseRental.getCategory().getCategoryId(),
                    houseRental.getCategory().getName(),
                    houseRental.getCategory().getType()
            );

            ExpenseResponseDTO houseRentalDto = new ExpenseResponseDTO(
                    houseRental.getExpenseId(),
                    houseRental.getDescription(),
                    houseRental.getAmount(),
                    houseRental.getTransactionDate(),
                    houseRental.getDueDate(),
                    houseRental.getStatus(),
                    categoryDto
            );

            ExpenseResponseDTO carDto = new ExpenseResponseDTO(
                    car.getExpenseId(),
                    car.getDescription(),
                    car.getAmount(),
                    car.getTransactionDate(),
                    car.getDueDate(),
                    car.getStatus(),
                    categoryDto
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(expenseRepository.findAllByUser(user)).thenReturn(List.of(houseRental,car));
            when(mapper.toDto(houseRental)).thenReturn(houseRentalDto);
            when(mapper.toDto(car)).thenReturn(carDto);

            List<ExpenseResponseDTO> response = service.getAll();

            verify(userService,times(1)).getAuthenticatedUser();
            verify(expenseRepository, times(1)).findAllByUser(user);
            verify(mapper, times(2)).toDto(any(Expense.class));

            assertNotNull(response);
            assertEquals(2,response.size());
            assertEquals(car.getDescription(), response.getFirst().description());

        }

    }

    @Nested
    class getById{

        @Test
        @DisplayName("Deve buscar uma despesa pelo id com sucesso")
        void shouldFindExpenseByIdWithSuccess(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Expense expense = ExpenseTestBuilder.anExpense()
                    .withExpenseId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            ExpenseResponseDTO dto = new ExpenseResponseDTO(
                    expense.getExpenseId(),
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getTransactionDate(),
                    expense.getDueDate(),
                    expense.getStatus(),
                    null
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(expenseRepository.findById(expense.getExpenseId())).thenReturn(Optional.of(expense));
            when(mapper.toDto(expense)).thenReturn(dto);

            ExpenseResponseDTO response = service.getById(expense.getExpenseId());

            verify(userService, times(1)).getAuthenticatedUser();
            verify(expenseRepository, times(1)).findById(expense.getExpenseId());
            verify(mapper, times(1)).toDto(expense);

            assertNotNull(response);
            assertEquals(dto.expenseId(), response.expenseId());
            assertEquals(dto.description(), response.description());
            assertEquals(dto.amount(), response.amount());
        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não encontre a despesa")
        void shouldThrowExceptionWhenExpenseNotFound(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            UUID id = UUID.randomUUID();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(expenseRepository.findById(id)).thenReturn(Optional.empty());

            TransactionNotFound e = assertThrows(
                    TransactionNotFound.class,
                    () -> service.getById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(expenseRepository, times(1)).findById(id);
            verify(mapper, never()).toDto(any(Expense.class));

            assertNotNull(e);
            assertEquals("Expense não encontrada!", e.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não tenha autorização")
        void shouldThrowExceptionWhenExpenseIsNotAuthorized(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Expense expense = ExpenseTestBuilder.anExpense()
                    .withExpenseId(UUID.randomUUID())
                    .withUser(UserTestBuilder.aUser()
                            .withUserId(UUID.randomUUID()).build()
                    )
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(expenseRepository.findById(expense.getExpenseId())).thenReturn(Optional.of(expense));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.getById(expense.getExpenseId())
            );


            verify(userService, times(1)).getAuthenticatedUser();
            verify(expenseRepository, times(1)).findById(expense.getExpenseId());
            verify(mapper, never()).toDto(expense);


            assertNotNull(e);
            assertEquals("Você não pode acessar essa transação", e.getMessage());
        }

    }

}
