/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.ManagementVisaDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.VisaDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ManagementVisaDetailsService {

    /**
     * @param managementVisaDetails
     * @return
     */
    ApiResultDTO createVisaDetails(String locale, ManagementVisaDetails managementVisaDetails);

    /**
     * @param managementVisaDetails
     * @return
     */
    ApiResultDTO modifyVisaDetails(String locale, ManagementVisaDetails managementVisaDetails);

    /**
     * @param status
     * @return
     */
    List<MasterCodeResultDTO> getActiveVisaTypes(String status);

    /**
     * @param expressVisa
     * @param status
     * @return
     */
    List<MasterCodeResultDTO> getActiveVisaTypeAndExpressVisa(boolean expressVisa, String status);

    /**
     * @param visaType
     * @return
     */
    MasterCodeResultDTO getVisaDetailsByVisaType(String visaType);

    /**
     * @param string
     * @return
     */
    ManagementVisaDetails getVisaCompleteDetailsByVisaType(String string);

    /**
     * @param visaId
     * @return
     */
    ManagementVisaDetails findVisaDetailsById(Long visaId);

    /**
     * @param locale
     * @param visaDetailsSearchDTO
     * @return
     */
    Page<ManagementVisaDetails> searchVisaDetails(String locale, VisaDetailsSearchDTO visaDetailsSearchDTO);

    /**
     * @return
     */
    List<MasterCodeResultDTO> findExtensionVisaTypes();

}
