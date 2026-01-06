package com.hypercube.evisa.applicant.api.service;

import com.hypercube.evisa.applicant.api.model.ContactUsInfoDTO;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ContactUsService {

    /**
     * @param contactUsInfoDTO
     * @param locale
     * @return
     */
    ApiResultDTO sendContactUsFeedback(ContactUsInfoDTO contactUsInfoDTO, String locale);

}
