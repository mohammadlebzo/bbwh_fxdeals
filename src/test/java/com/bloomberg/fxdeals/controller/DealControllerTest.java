package com.bloomberg.fxdeals.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.service.DealService;
import com.bloomberg.fxdeals.utils.DealProcessingException;

public class DealControllerTest {

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void dealControllerShouldCreateDealAndReturnOKStatus() {

        DealRequest dealRequest = new DealRequest(621L, "JOD", "EUR", "2023-06-21 11:59:35",
                BigDecimal.valueOf(1000.00));

        doNothing().when(dealService).processDeal(dealRequest);

        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("deal saved");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

    @Test
    public void dealControllerShouldThrowDealProcessingException() {

        DealRequest dealRequest = new DealRequest(621L, "JOD", "EUR", "2023-06-21 11:59:35",
                BigDecimal.valueOf(1000.00));

        doThrow(new DealProcessingException("Deal processing failed")).when(dealService).processDeal(dealRequest);
        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(responseEntity.getBody()).isEqualTo("Deal processing failed");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

    @Test
    public void dealControllerShouldThrowIllegalArgumentException() {

        DealRequest dealRequest = new DealRequest(621L, "US", "EUR", "2023-06-21 11:59:35",
                BigDecimal.valueOf(1000.00));
        doThrow(new IllegalArgumentException("Please use a valid currency code")).when(dealService)
                .processDeal(dealRequest);

        ResponseEntity<String> responseEntity = dealController.createDeal(dealRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("Please use a valid currency code");

        verify(dealService, times(1)).processDeal(dealRequest);
    }

}
