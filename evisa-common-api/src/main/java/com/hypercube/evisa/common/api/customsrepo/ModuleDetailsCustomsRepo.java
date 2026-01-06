package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.ModuleDetailSearchResultsDTO;
import com.hypercube.evisa.common.api.model.ModuleDetailsSearchDTO;


/**
 * @author SivaSreenivas
 *
 */
public interface ModuleDetailsCustomsRepo {

    
    /**
     * @param moduleDetailsSearchDTO
     * @return
     */
    Page<ModuleDetailSearchResultsDTO> searchModuleDetails(ModuleDetailsSearchDTO moduleDetailsSearchDTO);

}
