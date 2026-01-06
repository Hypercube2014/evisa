package com.hypercube.evisa.applicant.api.resource;

import java.io.File;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hypercube.evisa.applicant.api.model.FAQ;
import com.hypercube.evisa.applicant.api.model.SystemFAQDTOList;
import com.hypercube.evisa.applicant.api.service.FaqService;
import com.hypercube.evisa.common.api.model.MasterCodeList;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.service.ManagementVisaDetailsService;
import com.hypercube.evisa.common.api.service.MasterCodeDetailsService;
import com.hypercube.evisa.common.api.constants.CommonsConstants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class CommonResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4559368552492177016L;

    @Autowired
    private FaqService faqService;

    /**
     * 
     */
    @Autowired(required = true)
    private MasterCodeDetailsService masterCodeDetailsService;

    /**
     * 
     */
    @Autowired(required = true)
    private ManagementVisaDetailsService managementVisaDetailsService;

    /**
     * @param authorization
     * @param locale
     * @param codeType
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/mastercode/active/{codeType}")
    @ResponseStatus(code = HttpStatus.OK)
    public MasterCodeList findActiveMasterCodesByCodeType(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("codeType") String codeType) {
        log.info("CommonResource-findActiveMasterCodesByCodeType");
        return new MasterCodeList(
                masterCodeDetailsService.findActiveMasterCodesByCodeType(codeType, CommonsConstants.YES));
    }

    /**
     * @param authorization
     * @param locale
     * @param codeType
     * @param code
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/mastercode/{codeType}/{code}")
    public ResponseEntity<MasterCodeResultDTO> findMasterCodesByCodeTypeAndCode(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("codeType") String codeType,
            @PathVariable("code") String code) {
        log.info("CommonResource-findMasterCodesByCodeTypeAndCode");
        return new ResponseEntity<>(masterCodeDetailsService.findMasterCodesByCodeTypeAndCode(codeType, code),
                HttpStatus.OK);
    }

    /**
     * @param authorization
     * @param locale
     * @param active
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/visadetails/{active}")
    @ResponseStatus(code = HttpStatus.OK)
    public MasterCodeList findActiveVisaType(@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("active") String active) {
        log.info("CommonResource-findActiveVisaType");
        return new MasterCodeList(managementVisaDetailsService.getActiveVisaTypes(active));
    }

    /**
     * @param authorization
     * @param locale
     * @param active
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/visadetails/extension")
    @ResponseStatus(code = HttpStatus.OK)
    public MasterCodeList findExtensionVisaTypes(@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale) {
        log.info("CommonResource-findExtensionVisaTypes");
        return new MasterCodeList(managementVisaDetailsService.findExtensionVisaTypes());
    }

    /**
     * @param authorization
     * @param locale
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/visadetailsbyvisatype/{visatype}")
    public ResponseEntity<MasterCodeResultDTO> getVisaDetailsByVisaType(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale, @PathVariable("visatype") String visatype) {
        log.info("CommonResource-getVisaDetailsByVisaType");
        return new ResponseEntity<>(managementVisaDetailsService.getVisaDetailsByVisaType(visatype), HttpStatus.OK);
    }

    /**
     * @param authorization
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/applicant/faqdetails")
    public ResponseEntity<SystemFAQDTOList> getFaqDetails(
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization) {
        log.info("CommonResource-getFaqDetails");


        SystemFAQDTOList systemFAQDTOs = null;
        try {
            // InputStream inJson = SystemFAQDTOList.class
            // .getResourceAsStream("/hypercube/evisa/applicant/rel1_0/faq/faq.json");
            systemFAQDTOs = new ObjectMapper().readValue(
                    new File("/home/ubuntu/hypercube/evisa/applicant/rel1_0/faq/faq.json"),
                    SystemFAQDTOList.class);

            log.info("systemFAQDTOs -=-=-=- {}", systemFAQDTOs);
        } catch (JsonParseException e) {
            log.error("getFaqDetails-JsonParseException {}", e);
        } catch (JsonMappingException e) {
            log.error("getFaqDetails-JsonMappingException {}", e);
        } catch (IOException e) {
            log.error("getFaqDetails-IOException {}", e);
        }

        return new ResponseEntity<>(systemFAQDTOs, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/v1/api/applicant/getFaqdetails")
    public List<FAQ> getAllFaqDetails() {
        log.info("CommonResource-getFaqDetails");


        return faqService.getFaqDetails();
    }

}
