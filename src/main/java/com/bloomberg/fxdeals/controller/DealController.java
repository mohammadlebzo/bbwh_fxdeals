package com.bloomberg.fxdeals.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bloomberg.fxdeals.dto.DealRequest;
import com.bloomberg.fxdeals.service.DealService;
import com.bloomberg.fxdeals.utils.DealProcessingException;

@RestController
public class DealController {

    @Autowired
    private DealService dealService;

    @RequestMapping(value = "/add-deal", method = RequestMethod.POST)
    public ResponseEntity<String> createDeal(@RequestBody DealRequest dealRequest) {

        try {
            dealService.processDeal(dealRequest);

            return new ResponseEntity<>("deal saved", HttpStatus.OK);
        } catch (DealProcessingException dealProcessingException) {
            return new ResponseEntity<>(dealProcessingException.getMessage(), HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
