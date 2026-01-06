package com.hypercube.evisa.applicant.api.resource;

import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hypercube.evisa.applicant.api.serviceimpl.ApplicantCommonServiceFacadeImpl;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.CheckOutPaymentDTO;
import com.hypercube.evisa.common.api.model.VisaCheckOverstayDTO;
import com.hypercube.evisa.common.api.service.ApplicantVisaOverstayServiceFacade;
import com.hypercube.evisa.common.api.service.VisaCheckOverstayService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ayanleh abdousalam
 *
 */
@RestController
@Data
@Slf4j
public class ApplicantVisaPenalityResource{

	
    @Autowired(required = true)
    private ApplicantVisaOverstayServiceFacade applicantVisaOverstayServiceFacade;
	
	 @Autowired(required = true)
	    private VisaCheckOverstayService visacheckOverstayService;
	 @Autowired(required = true)
	 private ApplicantCommonServiceFacadeImpl applicantCommonServiceFacadeImpl;
    
    /**
     * @param authorization
     * @param  VisaCheckOverstayDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/searchavailableoverstay")
    public ResponseEntity<Page<VisaCheckOverstayDTO>> searchVisaCheckOverstay(
    		 @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody VisaCheckOverstayDTO visaCheckOverstayDTO) {
        log.info("ApplicantVisaPenalityResource-searchVisaCheckOverstay");
        System.out.println(visaCheckOverstayDTO.getLoggedUser()+" page "+ visaCheckOverstayDTO.getPageSize()+ "page number "+visaCheckOverstayDTO.getPageNumber());
        return new ResponseEntity<>(visacheckOverstayService.searchVisaCheckoverstay(visaCheckOverstayDTO),  HttpStatus.OK);
   
    }
    
    @CrossOrigin
    @PostMapping(value = "/v1/api/applyoverstay/checkout")
    public ResponseEntity<ChargeResponseDTO> applyOverstay(HttpServletRequest request,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody CheckOutPaymentDTO checkOutPaymentDTO) throws JsonProcessingException, IOException {
        log.info("ApplicantOverstayResource-applyOverstay {}", checkOutPaymentDTO);
        System.out.println("gdhdhdhhdhdhffdffffffffffffffffffffffffffffff");
        
        System.out.println("visaCheckoverstayDTO.getApplicationNumber()"+checkOutPaymentDTO.getReferenceNumber());
        System.out.println("visaCheckoverstayDTO.getPenalityAmount()"+checkOutPaymentDTO.getAmount());
        ChargeResponseDTO  chargeResponseDTO;

        chargeResponseDTO = applicantCommonServiceFacadeImpl.paymentPenalityWithCheckOutPage(request,checkOutPaymentDTO);
        return new ResponseEntity<>(chargeResponseDTO, HttpStatus.OK);
    }
    
    @CrossOrigin
    @PostMapping(value = "/v1/api/searchavailableoverstayview")
    public ResponseEntity<VisaCheckOverstayDTO> searchVisaCheckOverstayview(
    		 @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
    		 @RequestBody VisaCheckOverstayDTO dto) {
        log.info("ApplicantVisaPenalityResource-searchVisaCheckOverstayview");
         System.out.println("ApplicantVisaPenalityResource-searchVisaCheckOverstay");
         System.out.println(dto.getApplicationNumber()+dto.getLoggedUser());
        return new ResponseEntity<>(visacheckOverstayService.findByapplicationNumber(dto.getApplicationNumber()),  HttpStatus.OK);
   
    }
    
//    @CrossOrigin
//    @PostMapping(value = "/v1/api/estimatePenality")
//    public ResponseEntity<VisaCheckOverstayDTO> estimatepenality(
//    		 @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
//    		 @RequestBody VisaCheckOverstayDTO dto) {
//        log.info("ApplicantVisaPenalityResource-searchVisaCheckOverstayview");
//         System.out.println("ApplicantVisaPenalityResource-searchVisaCheckOverstay");
//         System.out.println(dto.getApplicationNumber()+dto.getLoggedUser());
//        return new ResponseEntity<>(applicantCommonServiceFacadeImpl.estimatePenality(dto),HttpStatus.OK);



    }

    

