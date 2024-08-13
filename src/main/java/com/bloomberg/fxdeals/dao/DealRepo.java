package com.bloomberg.fxdeals.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bloomberg.fxdeals.model.Deal;

@Repository
public interface DealRepo extends JpaRepository<Deal, Long> {
    boolean existsById(Long id);
}
