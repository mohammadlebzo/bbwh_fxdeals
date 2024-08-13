package com.bloomberg.fxdeals.service;

import java.time.LocalDateTime;

import com.bloomberg.fxdeals.dao.DealRepo;
import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.model.Deal;
import com.bloomberg.fxdeals.utils.DealProcessingException;
import com.bloomberg.fxdeals.utils.DealValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
public class DealService {

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    @Autowired
    private DealRepo dealRepo;

    @Autowired
    private DealValidator dealValidator;

    @Transactional(noRollbackFor = {DealProcessingException.class, DealProcessingException.class})
    public void processDeal(DealRequest dealRequest) {

        Errors errors = new BeanPropertyBindingResult(dealRequest, "dealRequest");
        dealValidator.validate(dealRequest, errors);

        if (errors.hasErrors()) {
            for (FieldError fieldError : errors.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();

                if (fieldName.equals("id")) {
                    throw new DealProcessingException(errorMessage);
                } else {
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }

        try {
            Deal deal = convertToDeal(dealRequest);
            dealRepo.save(deal);
            logger.info("Deal has been saved successfully");
        } catch (Exception ex) {
            logger.error("Error processing deal: {}", dealRequest.getId());
        }
    }

    private Deal convertToDeal(DealRequest dealRequest) {
        Deal deal = new Deal();
        deal.setId(dealRequest.getId());
        deal.setFromCurrency(dealRequest.getFromCurrency());
        deal.setToCurrency(dealRequest.getToCurrency());
        deal.setTimestamp(LocalDateTime.parse(dealRequest.getTimestamp().replace(" ", "T")));
        deal.setAmount(dealRequest.getAmount());

        return deal;
    }

}
