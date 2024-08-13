package com.bloomberg.fxdeals.model;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class DealTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void dealEntityShouldHaveProperMapping() {

        LocalDateTime time = LocalDateTime.now();
        Deal deal = new Deal(621L, "JOD", "EUR", time, new BigDecimal("1000.00"));

        entityManager.persist(deal);
        entityManager.flush();

        entityManager.clear();

        Deal found = entityManager.find(Deal.class, 621L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(deal.getId());
        assertThat(found.getFromCurrency()).isEqualTo(deal.getFromCurrency());
        assertThat(found.getToCurrency()).isEqualTo(deal.getToCurrency());
        assertThat(found.getTimestamp()).isCloseTo(deal.getTimestamp(), within(1, MILLIS));
        assertThat(found.getAmount()).isEqualTo(deal.getAmount());
    }
}
