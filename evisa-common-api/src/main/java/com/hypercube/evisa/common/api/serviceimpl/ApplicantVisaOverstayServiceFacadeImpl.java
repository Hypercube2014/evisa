package com.hypercube.evisa.common.api.serviceimpl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.CheckOutPaymentDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.service.ApplicantVisaOverstayServiceFacade;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Service
@Data
@Slf4j
public class ApplicantVisaOverstayServiceFacadeImpl implements ApplicantVisaOverstayServiceFacade{

	
	@Override
	public ChargeResponseDTO applyVisaOverstay(HttpServletRequest request, VisaCheckOverstayDTO visaDto)
			throws IOException {
	 {
			log.info("ApplicantCommonServiceFacadeImpl-paymentWithCheckOutPage");
/*
			ChargeResponseDTO chargeResponseDTO;
			Stripe.apiKey = stripeSecretKey;

			checkOutPaymentDTO.setSuccessUrl(
					appUrl + request.getContextPath() + "/#/main/success-payment?session_id={CHECKOUT_SESSION_ID}");
			checkOutPaymentDTO.setCurrency("USD");
			checkOutPaymentDTO.setQuantity(1L);

			String name = "";
			if ("EVISA_PAY".equals(checkOutPaymentDTO.getInstrType())) {
				checkOutPaymentDTO.setDescription(
						"Evisa for Djibouti checkout request for …: " + checkOutPaymentDTO.getReferenceNumber());
				name = "E-Visa Payment";
			} else if ("EVISA_EXT".equals(checkOutPaymentDTO.getInstrType())) {
				checkOutPaymentDTO.setDescription("DJIB E-Visa Extension Checkout Request For Ref Number: "
						+ checkOutPaymentDTO.getReferenceNumber());
				name = "E-Visa Extension Payment";
			}

			SessionCreateParams params = SessionCreateParams.builder()
					.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
					.setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(checkOutPaymentDTO.getSuccessUrl())
					.setCancelUrl(checkOutPaymentDTO.getCancelUrl())
					.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(checkOutPaymentDTO.getQuantity())
							.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
									.setCurrency(checkOutPaymentDTO.getCurrency())
									.setUnitAmount(checkOutPaymentDTO.getAmount())
									.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
											.setDescription(checkOutPaymentDTO.getDescription()).setName(name).build())
									.build())
							.build())
					.build();

			try {
				Session session = Session.create(params);
				log.info("session.getId -=-=- {}", session.getId());

			
				applicantPaymentService.updatePaymentSessionId(checkOutPaymentDTO.getReferenceNumber(), session.getId());

				chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.SUCCESS, session.getPaymentStatus(),
						session.getId());
				log.info("chargeResponseDTO -=-=- {}", chargeResponseDTO);
			} catch (StripeException stexe) {
				log.error("paymentChargeRequest-StripeException {}", stexe.getMessage());
				chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, stexe.getMessage(), null, null);
			} catch (Exception exe) {
				log.error("paymentChargeRequest-Exception {}", exe.getCause());
				chargeResponseDTO = new ChargeResponseDTO(CommonsConstants.ERROR, exe.getMessage(), null, null);
			}
			return chargeResponseDTO;
			*/
		}
		return null;
	}

}
