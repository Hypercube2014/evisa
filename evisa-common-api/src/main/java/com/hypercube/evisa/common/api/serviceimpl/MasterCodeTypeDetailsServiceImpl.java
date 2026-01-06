/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.MasterCodeTypeDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeResultDTO;
import com.hypercube.evisa.common.api.model.MasterCodeSearchDTO;
import com.hypercube.evisa.common.api.repository.MasterCodeTypeDetailsRepository;
import com.hypercube.evisa.common.api.service.MasterCodeTypeDetailsService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
@Data
@Service
public class MasterCodeTypeDetailsServiceImpl implements MasterCodeTypeDetailsService {

    /**
     * 
     */
    @Autowired(required = true)
    MasterCodeTypeDetailsRepository masterCodeTypeDetailsRepository;

    /**
     * 
     */
    @Override
    public ApiResultDTO createMasterCodeType(String locale, MasterCodeTypeDetails masterCodeTypeDetails) {
        log.info("MasterCodeTypeDetailsServiceImpl-createMasterCodeType");
        ApiResultDTO apiResultDTO;

        boolean result = masterCodeTypeDetailsRepository.existsByCodeType(masterCodeTypeDetails.getCodeType());

        if (result) {
            List<Object> objArray = new ArrayList<>();
            objArray.add(masterCodeTypeDetails.getCodeType());
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.already.exists", objArray.toArray(), locale, null));
        } else {
            masterCodeTypeDetailsRepository.save(masterCodeTypeDetails);
            apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                    LocaleConfig.getResourceValue("save.success", null, locale, null));
        }
        return apiResultDTO;
    }

    /**
     * 
     */
    @Override
    public ApiResultDTO modifyMasterCodeType(String locale, MasterCodeTypeDetails masterCodeTypeDetails) {
        log.info("MasterCodeTypeDetailsServiceImpl-modifyMasterCodeType");
        ApiResultDTO apiResultDTO;

        if (masterCodeTypeDetails.getId() == null) {
            apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                    LocaleConfig.getResourceValue("error.invalid.request.mandatory", null, locale, null));
        } else {
            boolean result = masterCodeTypeDetailsRepository.existsById(masterCodeTypeDetails.getId());

            if (result) {
                masterCodeTypeDetailsRepository.save(masterCodeTypeDetails);
                apiResultDTO = new ApiResultDTO(CommonsConstants.SUCCESS,
                        LocaleConfig.getResourceValue("update.success", null, locale, null));
            } else {
                apiResultDTO = new ApiResultDTO(CommonsConstants.ERROR,
                        LocaleConfig.getResourceValue("error.invalid.request.notexist", null, locale, null));
            }
        }

        return apiResultDTO;
    }

    /**
     * 
     */
    @Override
    public Optional<MasterCodeTypeDetails> findMasterCodeTypeDetailsById(Long id) {
        log.info("MasterCodeTypeDetailsServiceImpl-findMasterCodeTypeDetailsById");
        return masterCodeTypeDetailsRepository.findById(id);
    }

    /**
     * 
     */
    @Override
    public List<MasterCodeResultDTO> findMasterCodeTypes(String active) {
        log.info("MasterCodeTypeDetailsServiceImpl-findMasterCodeTypeByCodeType");
        return masterCodeTypeDetailsRepository.findByActive(active);
    }

    /**
     * 
     */
    @Override
    public Page<MasterCodeTypeDetails> searchMasterCodeType(String locale, MasterCodeSearchDTO masterCodeSearchDTO) {
        log.info("MasterCodeTypeDetailsServiceImpl-searchMasterCodeType");
        return masterCodeTypeDetailsRepository.searchMasterCodeTypes(masterCodeSearchDTO);
    }

}
