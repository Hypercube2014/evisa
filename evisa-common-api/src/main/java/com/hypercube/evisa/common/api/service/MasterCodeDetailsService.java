/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.MasterCodeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MasterCodeDetailsService {

    /**
     * @param masterCodeDetails
     * @return
     */
    ApiResultDTO createMasterCode(String locale, MasterCodeDetails masterCodeDetails);

    /**
     * @param id
     * @return
     */
    Optional<MasterCodeDetails> findMasterCodeDetailsById(Long id);

    /**
     * @param codeType
     * @param active
     * @return
     */
    List<MasterCodeResultDTO> findActiveMasterCodesByCodeType(String codeType, String active);

    /**
     * @param codeType
     * @param code
     * @return
     */
    MasterCodeResultDTO findMasterCodesByCodeTypeAndCode(String codeType, String code);

    /**
     * @param locale
     * @param masterCodeSearchDTO
     * @return
     */
    Page<MasterCodeDetails> searchMasterCodes(String locale, MasterCodeSearchDTO masterCodeSearchDTO);

    /**
     * @param masterCodeDetailsDTO
     * @return
     */
    ApiResultDTO modifyMasterCode(String locale, MasterCodeDetails masterCodeDetailsDTO);

    /**
     * @param codeList
     * @return
     */
    List<MasterCodeResultDTO> getMasterCodeLists(List<String> codeList);

}
