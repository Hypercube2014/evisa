/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MasterCodeTypeDetailsService {

    /**
     * @param locale
     * @param masterCodeTypeDetails
     * @return
     */
    ApiResultDTO createMasterCodeType(String locale, MasterCodeTypeDetails masterCodeTypeDetails);

    /**
     * @param locale
     * @param masterCodeTypeDetails
     * @return
     */
    ApiResultDTO modifyMasterCodeType(String locale, MasterCodeTypeDetails masterCodeTypeDetails);

    /**
     * @param id
     * @return
     */
    Optional<MasterCodeTypeDetails> findMasterCodeTypeDetailsById(Long id);

    /**
     * @param active
     * @return
     */
    List<MasterCodeResultDTO> findMasterCodeTypes(String active);

    /**
     * @param locale
     * @param masterCodeSearchDTO
     * @return
     */
    Page<MasterCodeTypeDetails> searchMasterCodeType(String locale, MasterCodeSearchDTO masterCodeSearchDTO);

}
