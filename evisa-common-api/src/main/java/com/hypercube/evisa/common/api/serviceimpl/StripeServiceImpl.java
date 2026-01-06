package com.hypercube.evisa.common.api.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.model.ChargeRequestDTO;
import com.hypercube.evisa.common.api.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    /**
     * 
     */
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    /**
     * 
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     *
     */
    @Override
    public Charge paymentChargeRequest(ChargeRequestDTO chargeRequestDTO) throws StripeException, Exception {
        log.info("StripeServiceImpl-paymentChargeRequest");

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequestDTO.getAmount());
        chargeParams.put("currency", chargeRequestDTO.getCurrency());
        chargeParams.put("receipt_email", chargeRequestDTO.getRecieptMail());
        chargeParams.put("description", chargeRequestDTO.getDescription());
        chargeParams.put("source", chargeRequestDTO.getStripeToken());

        return Charge.create(chargeParams);

    }

}
