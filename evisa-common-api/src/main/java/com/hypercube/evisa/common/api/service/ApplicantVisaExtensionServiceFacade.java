package com.hypercube.evisa.common.api.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.hypercube.evisa.common.api.domain.ApplicantVisaExtension;
import com.hypercube.evisa.common.api.domain.VisaExtensionCheck;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantVisaExtensionHistoryDTO;
import com.hypercube.evisa.common.api.model.ChargeResponseDTO;
import com.hypercube.evisa.common.api.model.DashboardDTO;
import com.hypercube.evisa.common.api.model.ExtensionPreviewDTO;
import com.hypercube.evisa.common.api.model.VisaExtensionSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantVisaExtensionServiceFacade {

    /**
     * @param request
     * @param applicantVisaExtension
     * @param file
     * @return
     * @throws IOException
     */
    ChargeResponseDTO applyVisaExtension(HttpServletRequest request, ApplicantVisaExtension applicantVisaExtension,
            MultipartFile file) throws IOException;

    /**
     * @param visaExtensionCheckSearchDTO
     * @return
     */
    Page<VisaExtensionCheck> searchVisaExtensionCheck(VisaExtensionSearchDTO visaExtensionCheckSearchDTO);

    /**
     * @param visaExtensionSearchDTO
     * @return
     */
    Page<ApplicantVisaExtension> searchApplicantVisaExtension(VisaExtensionSearchDTO visaExtensionSearchDTO);

    /**
     * @param extensionId
     * @return
     */
    ApplicantVisaExtension fetchApplicantVisaExtension(String extensionId);

    /**
     * @param locale
     * @param approverHistoryDDetaisDTO
     * @return
     */
    ApiResultDTO visaExtensionProcessApproval(String locale,
            ApplicantVisaExtensionHistoryDTO applicantVisaExtensionHistoryDTO);

    /**
     * @param locale
     * @param loggeduser
     * @return
     */
    ApiResultDTO processNextSetOfExtensionFiles(String locale, String loggeduser);

    /**
     * @param request
     * @param locale
     * @param extensionNumber
     * @return
     */
    ExtensionPreviewDTO extensionPreview(HttpServletRequest request, String locale, String extensionNumber);

    /**
     * @param loggeduser
     * @param period
     * @return
     */
    DashboardDTO decisionMakerExtensionDashboard(String loggeduser, String period);

}
