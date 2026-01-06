package com.hypercube.evisa.applicant.api.resource;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hypercube.evisa.applicant.api.service.ApplicantCommonServiceFacade;
import com.stripe.exception.StripeException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Data
public class dateresource {

	 /**
     * 
     */
    @Autowired
    private ApplicantCommonServiceFacade applicantCommonServiceFacade;
    
    @CrossOrigin
    @PostMapping(value = "/v1/api/dateressource")
    public ResponseEntity<Date> dateRessource()
            throws StripeException {
        log.info("PaymentResource-paymentChargeRequest");

        return new ResponseEntity<>(applicantCommonServiceFacade.dateverification(), HttpStatus.OK);
    }

}
