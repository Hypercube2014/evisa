package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.common.api.domain.ModuleDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailSearchResultsDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ModuleDetailsService {

    /**
     * @param locale
     * @param authorization
     * @param moduleDetailsDTO
     * @return
     */
    ResponseEntity<ApiResultDTO> saveModuleDetails(ModuleDetails moduleDetails, String locale, String loggeduser);
    
    /**
     * @param moduleDetailsSearchDTO
     * @param loggeduser
     * @return
     */
    ResponseEntity<Page<ModuleDetailSearchResultsDTO>> searchModuleCodedetails(ModuleDetailsSearchDTO moduleDetailsSearchDTO, String loggeduser);
    
    /**
     * @param sysId
     * @param loggeduser
     * @return
     */
    ResponseEntity<ModuleDetails> viewModuleCodedetails(Long moduleId, String loggeduser, String locale);
    
    /**
     * @param sysId
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> deleteModuleCodedetails(Long moduleId, String loggeduser, String locale);

    /**
     * @param moduleDetails
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> updateModuleDetails(ModuleDetails moduleDetails, String loggeduser, String locale);

    /**
     * @param loggeduser
     * @param locale
     * @return
     */
    ResponseEntity<List<ModuleDetails>> getActiveModules(String loggeduser, String locale);
    
    

}
