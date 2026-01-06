/**
 * 
 */
package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MasterCodeTypeDetailsCustomsRepo {

    /**
     * @param masterCodeSearchDTO
     * @return
     */
    Page<MasterCodeTypeDetails> searchMasterCodeTypes(MasterCodeSearchDTO masterCodeSearchDTO);

}
