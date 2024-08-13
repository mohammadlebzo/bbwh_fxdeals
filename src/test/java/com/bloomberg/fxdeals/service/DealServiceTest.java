package com.bloomberg.fxdeals.service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.model.Deal;
import com.bloomberg.fxdeals.utils.DealProcessingException;
import com.bloomberg.fxdeals.utils.DealValidator;

public class DealServiceTest {

    @Mock
    private DealRepo dealRepo;

    @Mock
    private DealValidator dealValidator;

    @InjectMocks
    private DealService dealService;

    private DealRequest dealRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dealRequest = new DealRequest();
        dealRequest.setId(621L);
        dealRequest.setFromCurrency("USD");
        dealRequest.setToCurrency("EUR");
        dealRequest.setTimestamp("2023-06-21 11:59:35");
        dealRequest.setAmount(new BigDecimal("1000.0"));
    }

    @Test
    public void dealServiceShouldHaveTheOutcomeOfSuccessfulValidationForDeal() {
        doNothing().when(dealValidator).validate(any(), any());

        dealService.processDeal(dealRequest);

        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, times(1)).save(any(Deal.class));
    }

    @Test
    public void dealServiceShouldHaveTheOutcomeOfValidationErrorForDealWhenValidatingIDField() {
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        errors.rejectValue("id", "error.id", "Deal unique ID error");

        doAnswer(invocation -> {
            ((Errors) invocation.getArgument(1)).rejectValue("id", "error.d",
                    "Deal unique ID error");
            return null;
        }).when(dealValidator).validate(any(), any());

        Exception exception = assertThrows(DealProcessingException.class, () -> {
            dealService.processDeal(dealRequest);
        });

        assertEquals("Deal unique ID error", exception.getMessage());
        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, never()).save(any(Deal.class));
    }

    @Test
    public void dealServiceShouldHaveTheOutcomeOfValidationErrorForDealWhenValidatingFromCurrencyField() {
        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        errors.rejectValue("fromCurrency", "error.fromCurrency", "From currency error");

        doAnswer(invocation -> {
            ((Errors) invocation.getArgument(1)).rejectValue("fromCurrency", "error.fromCurrency",
                    "From currency error");
            return null;
        }).when(dealValidator).validate(any(), any());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dealService.processDeal(dealRequest);
        });

        assertEquals("From currency error", exception.getMessage());
        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, never()).save(any(Deal.class));
    }

    @Test
    public void dealServiceShouldGetRuntimeErrorWhenTryingToSaveToDB() {
        doNothing().when(dealValidator).validate(any(), any());
        doThrow(new RuntimeException("Database error")).when(dealRepo).save(any(Deal.class));

        dealService.processDeal(dealRequest);

        verify(dealValidator, times(1)).validate(any(), any());
        verify(dealRepo, times(1)).save(any(Deal.class));
    }
}
