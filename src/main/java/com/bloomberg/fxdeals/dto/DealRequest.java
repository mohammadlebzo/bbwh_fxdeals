package com.bloomberg.fxdeals.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DealRequest {
    @NotNull(message = "ID can't be null")
    @NotBlank(message = "ID can't be blank")
    private Long id;

    @NotBlank(message = "Currency can't be blank")
    @Size(min = 3, max = 3, message = "Code should be of char length: 3")
    private String fromCurrency;

    @NotBlank(message = "Currency can't be blank")
    @Size(min = 3, max = 3, message = "Code should be of char length: 3")
    private String toCurrency;

    @NotBlank(message = "Timestamp can't be blank")
    private String timestamp;

    @NotNull(message = "Amount can't be Null")
    @Positive(message = "Amount can't be blank")
    private BigDecimal amount;

    public DealRequest(Long id, String fromCurrency, String toCurrency, String timestamp,
            BigDecimal amount) {
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.timestamp = timestamp;
        this.amount = amount;
    }
}
