package com.bloomberg.fxdeals.validator;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.utils.DealValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DealValidatorTest {

    @Mock
    private DealRepo dealRepo;

    @InjectMocks
    private DealValidator dealValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void dealValidatorShouldPassWhenValidatingTheExistanceOfTheImportedRequest() {

        DealRequest dealRequest = new DealRequest(621L, "USD", "EUR", "2023-06-21 11:59:35", new BigDecimal("1000.00"));
        when(dealRepo.existsById(621L)).thenReturn(false);
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void dealValidatorShouldFailWhenPassingDuplicateRequest() {

        DealRequest dealRequest = new DealRequest(621L, "USD", "EUR", "2023-06-21 11:59:35", new BigDecimal("1000.00"));
        when(dealRepo.existsById(621L)).thenReturn(true);

        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        assertTrue(errors.hasFieldErrors("id"));
        assertEquals("Deal with the same ID already exists you cant add the same deal more than once",
                errors.getFieldError("id").getDefaultMessage());
    }

    @Test
    public void dealValidatorShouldFailWhenPassingTheSameExchangeCurrencyCode() {
        DealRequest dealRequest = new DealRequest(621L, "USD", "USD", "2023-06-21 11:59:35", new BigDecimal("1000.00"));
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");

        dealValidator.validate(dealRequest, errors);
        assertTrue(errors.hasFieldErrors("toCurrency"));
        assertEquals("You Cant Exchange to the same currency", errors.getFieldError("toCurrency").getDefaultMessage());
    }

}
