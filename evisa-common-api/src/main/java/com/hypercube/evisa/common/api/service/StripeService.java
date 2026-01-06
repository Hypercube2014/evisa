package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.model.ChargeRequestDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

/**
 * @author SivaSreenivas
 *
 */
public interface StripeService {

    /**
     * @param chargeRequestDTO
     * @return
     * @throws StripeException
     */
    Charge paymentChargeRequest(ChargeRequestDTO chargeRequestDTO) throws StripeException, Exception;

}
