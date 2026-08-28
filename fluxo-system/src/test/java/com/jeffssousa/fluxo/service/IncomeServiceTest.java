package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.CategoryTestBuilder;
import com.jeffssousa.fluxo.builders.IncomeTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.dto.income.IncomeRequestDTO;
import com.jeffssousa.fluxo.dto.income.IncomeResponseDTO;
import com.jeffssousa.fluxo.entities.Category;
import com.jeffssousa.fluxo.entities.Income;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.enums.CategoryType;
import com.jeffssousa.fluxo.enums.IncomeStatus;
import com.jeffssousa.fluxo.exception.business.TransactionNotFound;
import com.jeffssousa.fluxo.exception.business.UnauthorizedResourceAccessException;
import com.jeffssousa.fluxo.mapper.IncomeMapper;
import com.jeffssousa.fluxo.repository.CategoryRepository;
import com.jeffssousa.fluxo.repository.IncomeRepository;
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
public class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CurrentUserService userService;

    @Mock
    private IncomeMapper mapper;

    @InjectMocks
    private IncomeService service;


    @Nested
    class addIncome{

        @Test
        @DisplayName("Deve salvar uma receita quando a categoria já existir")
        void shouldCreateIncomeWhenCategoryExists(){

            User user = UserTestBuilder.aUser().build();
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(1L)
                    .build();

            Income income = IncomeTestBuilder.anIncome().build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    income.getDescription(),
                    income.getAmount(),
                    income.getTransactionDate(),
                    income.getStatus(),
                    category.getName()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toEntity(dto)).thenReturn(income);
            when(categoryRepository.findByNameAndUserUserId(dto.category(), user.getUserId()))
                    .thenReturn(Optional.of(category));
            when(incomeRepository.save(any(Income.class))).thenReturn(income);

            Income incomeSaved = service.addIncome(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(mapper, times(1)).toEntity(dto);
            verify(categoryRepository, times(1)).findByNameAndUserUserId(
                    dto.category(),
                    user.getUserId()
            );
            verify(incomeRepository, times(1)).save(any(Income.class));
            verify(categoryRepository, never()).save(any(Category.class));


            assertNotNull(incomeSaved);
            assertEquals(dto.description(), incomeSaved.getDescription());
            assertEquals(dto.amount(), incomeSaved.getAmount());
            assertEquals(dto.transactionDate(), incomeSaved.getTransactionDate());
            assertEquals(dto.status(), incomeSaved.getStatus());
            assertEquals(1L, incomeSaved.getCategory().getCategoryId());

        }

        @Test
        @DisplayName("Deve salvar uma receita quando a categoria não existir")
        void shouldCreateIncomeWhenCategoryDoesNotExist(){

            User user = UserTestBuilder.aUser().build();
            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(1L)
                    .build();

            Income income = IncomeTestBuilder.anIncome().build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    income.getDescription(),
                    income.getAmount(),
                    income.getTransactionDate(),
                    income.getStatus(),
                    category.getName()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(mapper.toEntity(dto)).thenReturn(income);
            when(categoryRepository.findByNameAndUserUserId(dto.category(), user.getUserId()))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(incomeRepository.save(any(Income.class))).thenReturn(income);

            Income incomeSaved = service.addIncome(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(mapper, times(1)).toEntity(dto);
            verify(categoryRepository, times(1)).findByNameAndUserUserId(
                    dto.category(),
                    user.getUserId()
            );
            verify(incomeRepository, times(1)).save(any(Income.class));
            verify(categoryRepository, times(1)).save(any(Category.class));


            assertNotNull(incomeSaved);
            assertEquals(dto.description(), incomeSaved.getDescription());
            assertEquals(dto.amount(), incomeSaved.getAmount());
            assertEquals(dto.transactionDate(), incomeSaved.getTransactionDate());
            assertEquals(dto.status(), incomeSaved.getStatus());
            assertEquals(1L, incomeSaved.getCategory().getCategoryId());

        }

    }

    @Nested
    class getAll{

        @Test
        @DisplayName("Deve Buscar todas as despesas com sucesso")
        void shouldFindAllIncomesWithSuccess() {

            User user = UserTestBuilder.aUser().build();

            Category category = CategoryTestBuilder.aCategory()
                    .withCategoryId(1L)
                    .build();

            Income salaryIncome = IncomeTestBuilder.anIncome()
                    .withCategory(category)
                    .withDescription("Salary April")
                    .withAmount(BigDecimal.valueOf(7500.00))
                    .withTransactionDate(LocalDateTime.of(2026,
                            4,
                            5,
                            10,
                            30))
                    .build();

            Income freelanceIncome = IncomeTestBuilder.anIncome()
                    .withCategory(category)
                    .withDescription("Freelance Project")
                    .withAmount(BigDecimal.valueOf(1800.00))
                    .withTransactionDate(LocalDateTime.of(2026,
                            4,
                            12,
                            18,
                            0))
                    .build();

            CategoryResponseDTO categoryDto = new CategoryResponseDTO(
                    category.getCategoryId(),
                    category.getName(),
                    category.getType()
            );

            IncomeResponseDTO salaryIncomeResponse = new IncomeResponseDTO(
                    salaryIncome.getIncomeId(),
                    salaryIncome.getDescription(),
                    salaryIncome.getAmount(),
                    salaryIncome.getTransactionDate(),
                    salaryIncome.getStatus(),
                    categoryDto
            );

            IncomeResponseDTO freelanceIncomeResponse = new IncomeResponseDTO(
                    freelanceIncome.getIncomeId(),
                    freelanceIncome.getDescription(),
                    freelanceIncome.getAmount(),
                    freelanceIncome.getTransactionDate(),
                    freelanceIncome.getStatus(),
                    categoryDto
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findAllByUser(user)).thenReturn(List.of(freelanceIncome, salaryIncome));
            when(mapper.toDTO(salaryIncome)).thenReturn(salaryIncomeResponse);
            when(mapper.toDTO(freelanceIncome)).thenReturn(freelanceIncomeResponse);

            List<IncomeResponseDTO> response = service.getAll();

            verify(userService,times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findAllByUser(user);
            verify(mapper, times(2)).toDTO(any(Income.class));

            assertNotNull(response);
            assertEquals(2,response.size());
            assertEquals(salaryIncome.getDescription(), response.getFirst().description());

        }


    }

    @Nested
    class getById {

        @Test
        @DisplayName("Deve buscar uma receita pelo id com sucesso")
        void shouldFindIncomeByIdWithSuccess() {

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            IncomeResponseDTO dto = new IncomeResponseDTO(
                    income.getIncomeId(),
                    income.getDescription(),
                    income.getAmount(),
                    income.getTransactionDate(),
                    income.getStatus(),
                    null
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId()))
                    .thenReturn(Optional.of(income));
            when(mapper.toDTO(income)).thenReturn(dto);

            IncomeResponseDTO response = service.getById(income.getIncomeId());

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1))
                    .findById(income.getIncomeId());
            verify(mapper, times(1)).toDTO(income);

            assertNotNull(response);
            assertEquals(dto.incomeId(), response.incomeId());
            assertEquals(dto.description(), response.description());
            assertEquals(dto.amount(), response.amount());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não encontre a receita")
        void shouldThrowExceptionWhenIncomeNotFound(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            UUID id = UUID.randomUUID();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(id))
                    .thenReturn(Optional.empty());

            TransactionNotFound e = assertThrows(
                    TransactionNotFound.class,
                    () -> service.getById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1))
                    .findById(id);
            verify(mapper, never()).toDTO(any(Income.class));

            assertNotNull(e);
            assertEquals("Receita não encontrada!", e.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não tenha autorização")
        void shouldThrowExceptionWhenIncomeIsNotAuthorized(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(UserTestBuilder.aUser()
                            .build()
                    )
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId()))
                    .thenReturn(Optional.of(income));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.getById(income.getIncomeId())
            );


            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(mapper, never()).toDTO(income);


            assertNotNull(e);
            assertEquals("Você não pode acessar essa transação", e.getMessage());

        }

    }

    @Nested
    class deleteById{

        @Test
        @DisplayName("Deve deletar uma receita com sucesso")
        void shouldDeleteIncomeByIdWithSuccess(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));

            service.deleteById(income.getIncomeId());

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(incomeRepository, times(1)).deleteById(income.getIncomeId());


        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não encontre a receita")
        void shouldThrowExceptionWhenIncomeNotFound(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            UUID id = UUID.randomUUID();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(id)).thenReturn(Optional.empty());

            TransactionNotFound e = assertThrows(
                    TransactionNotFound.class,
                    () -> service.deleteById(id)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(id);
            verify(incomeRepository, never()).deleteById(id);

            assertNotNull(e);
            assertEquals("Receita não encontrada!", e.getMessage());


        }


        @Test
        @DisplayName("Deve lançar uma exceção caso não tenha autorização")
        void shouldThrowExceptionWhenIncomeIsNotAuthorized(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(UserTestBuilder.aUser()
                            .build()
                    )
                    .build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.deleteById(income.getIncomeId())
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(incomeRepository, never()).deleteById(income.getIncomeId());

            assertNotNull(e);
            assertEquals("Você não pode acessar essa transação", e.getMessage());
        }

    }

    @Nested
    class updateById {

        @Test
        @DisplayName("Deve atualizar uma receita com sucesso")
        void shouldUpdateIncomeByIdWithSuccess() {

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    "Salary April",
                    BigDecimal.valueOf(7500.00),
                    LocalDateTime.of(2026, 4, 5, 10, 30),
                    IncomeStatus.RECEIVED,
                    "Salary"
            );

            Category category = CategoryTestBuilder.aCategory()
                    .withName("Salary")
                    .withCategoryId(1L)
                    .withType(CategoryType.INCOME)
                    .build();

            IncomeResponseDTO updatedResponse = new IncomeResponseDTO(
                    income.getIncomeId(),
                    dto.description(),
                    dto.amount(),
                    dto.transactionDate(),
                    dto.status(),
                    new CategoryResponseDTO(
                            category.getCategoryId(),
                            category.getName(),
                            category.getType()
                    )

            );


            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));
            when(categoryRepository.findByNameAndUserUserId(category.getName(), user.getUserId()))
                    .thenReturn(Optional.of(category));
            when(incomeRepository.save(any(Income.class))).thenReturn(income);
            when(mapper.toDTO(income)).thenReturn(updatedResponse);

            IncomeResponseDTO response = service.updateById(income.getIncomeId(), dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(categoryRepository, times(1))
                    .findByNameAndUserUserId(dto.category(), user.getUserId());
            verify(incomeRepository, times(1)).save(income);
            verify(mapper, times(1)).toDTO(income);
            verify(categoryRepository, never()).save(any(Category.class));

            assertNotNull(response);
            assertEquals(dto.description(), response.description());
            assertEquals(dto.amount(), response.amount());
            assertEquals(dto.transactionDate(), response.transactionDate());
            assertEquals(dto.status(), response.status());
            assertEquals(dto.category(), response.category().name());

        }

        @Test
        @DisplayName("Deve atualizar uma receita quando a categoria não existir")
        void shouldUpdateIncomeWhenCategoryDoesNotExist(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    "Salary April",
                    BigDecimal.valueOf(7500.00),
                    LocalDateTime.of(2026, 4, 5, 10, 30),
                    IncomeStatus.RECEIVED,
                    "Salary"
            );

            Category category = CategoryTestBuilder.aCategory()
                    .withName("Salary")
                    .withCategoryId(1L)
                    .withType(CategoryType.INCOME)
                    .build();

            IncomeResponseDTO updatedResponse = new IncomeResponseDTO(
                    income.getIncomeId(),
                    dto.description(),
                    dto.amount(),
                    dto.transactionDate(),
                    dto.status(),
                    new CategoryResponseDTO(
                            category.getCategoryId(),
                            category.getName(),
                            category.getType()
                    )

            );


            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));
            when(categoryRepository.findByNameAndUserUserId(category.getName(), user.getUserId()))
                    .thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(incomeRepository.save(any(Income.class))).thenReturn(income);
            when(mapper.toDTO(income)).thenReturn(updatedResponse);

            IncomeResponseDTO response = service.updateById(income.getIncomeId(), dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(categoryRepository, times(1))
                    .findByNameAndUserUserId(dto.category(), user.getUserId());
            verify(categoryRepository, times(1)).save(any(Category.class));
            verify(incomeRepository, times(1)).save(income);
            verify(mapper, times(1)).toDTO(income);

            assertNotNull(response);
            assertEquals(dto.description(), response.description());
            assertEquals(dto.amount(), response.amount());
            assertEquals(dto.transactionDate(), response.transactionDate());
            assertEquals(dto.status(), response.status());
            assertEquals(dto.category(), response.category().name());

        }

        @Test
        @DisplayName("Deve manter os valores atuais quando campos da requisição forem nulos")
        void shouldUpdateIncomeWhenRequestDataIsNull(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    null,
                    null,
                    null,
                    null,
                    null
            );

            Category category = CategoryTestBuilder.aCategory()
                    .withName("Salary")
                    .withCategoryId(1L)
                    .withType(CategoryType.INCOME)
                    .build();

            IncomeResponseDTO updatedResponse = new IncomeResponseDTO(
                    income.getIncomeId(),
                    income.getDescription(),
                    income.getAmount(),
                    income.getTransactionDate(),
                    income.getStatus(),
                    new CategoryResponseDTO(
                            category.getCategoryId(),
                            category.getName(),
                            category.getType()
                    )

            );


            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));
            when(incomeRepository.save(any(Income.class))).thenReturn(income);
            when(mapper.toDTO(income)).thenReturn(updatedResponse);

            IncomeResponseDTO response = service.updateById(income.getIncomeId(), dto);

            verify(categoryRepository, never())
                    .findByNameAndUserUserId(dto.category(), user.getUserId());
            verify(categoryRepository, never()).save(any(Category.class));

            assertEquals(income.getDescription(), response.description());
            assertEquals(income.getAmount(), response.amount());
            assertEquals(income.getTransactionDate(), response.transactionDate());
            assertEquals(income.getStatus(), response.status());

        }


        @Test
        @DisplayName("Deve lançar uma exceção caso não encontre a receita")
        void shouldThrowExceptionWhenIncomeNotFound(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            UUID id = UUID.randomUUID();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    "Salary April",
                    BigDecimal.valueOf(7500.00),
                    LocalDateTime.of(2026, 4, 5, 10, 30),
                    IncomeStatus.RECEIVED,
                    "Salary"
            );


            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(id)).thenReturn(Optional.empty());

            TransactionNotFound e = assertThrows(
                    TransactionNotFound.class,
                    () -> service.updateById(id, dto)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(id);
            verify(incomeRepository, never()).save(any(Income.class));
            verify(mapper, never()).toDTO(any(Income.class));

            assertNotNull(e);
            assertEquals("Receita não encontrada!", e.getMessage());

        }

        @Test
        @DisplayName("Deve lançar uma exceção caso não tenha autorização")
        void shouldThrowExceptionWhenIncomeIsNotAuthorized(){

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .build();

            UUID id = UUID.randomUUID();

            Income income = IncomeTestBuilder.anIncome()
                    .withIncomeId(UUID.randomUUID())
                    .withUser(UserTestBuilder.aUser()
                            .withUserId(id)
                            .build()
                    )
                    .build();

            IncomeRequestDTO dto = new IncomeRequestDTO(
                    "Salary April",
                    BigDecimal.valueOf(7500.00),
                    LocalDateTime.of(2026, 4, 5, 10, 30),
                    IncomeStatus.RECEIVED,
                    "Salary"
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(incomeRepository.findById(income.getIncomeId())).thenReturn(Optional.of(income));

            UnauthorizedResourceAccessException e = assertThrows(
                    UnauthorizedResourceAccessException.class,
                    () -> service.updateById(income.getIncomeId(),dto)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(incomeRepository, times(1)).findById(income.getIncomeId());
            verify(incomeRepository, never()).save(any(Income.class));
            verify(mapper, never()).toDTO(income);

            assertNotNull(e);
            assertEquals("Você não pode acessar essa transação", e.getMessage());
        }

    }

}
