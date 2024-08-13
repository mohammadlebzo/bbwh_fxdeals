package com.bloomberg.fxdeals.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tblDeals")
public class Deal {

    @Id
    @Column(name = "id", unique = true)
    private Long id;

    @Column(nullable = false)
    private String fromCurrency;

    @Column(nullable = false)
    private String toCurrency;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    public Deal(Long id, String fromCurrency, String toCurrency, LocalDateTime timestamp,
            BigDecimal amount) {
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.timestamp = timestamp;
        this.amount = amount;
    }
}
