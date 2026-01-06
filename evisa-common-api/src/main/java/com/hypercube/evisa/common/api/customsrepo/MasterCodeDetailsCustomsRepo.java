/**
 * 
 */
package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.MasterCodeDetails;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MasterCodeDetailsCustomsRepo {
    
    /**
     * @param locale
     * @param masterCodeSearchDTO
     * @return
     */
    Page<MasterCodeDetails> searchMasterCodes(String locale, MasterCodeSearchDTO masterCodeSearchDTO);

}
