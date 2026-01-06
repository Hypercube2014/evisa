/**
 * 
 */
package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.SystemFAQPrintDTO;
import com.hypercube.evisa.common.api.model.SystemFAQSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface SystemFAQService {

    /**
     * @param locale
     * @param systemFAQSearchDTO
     * @return
     */
    Page<SystemFAQ> searchSystemFaq(String locale, SystemFAQSearchDTO systemFAQSearchDTO);

    /**
     * @param locale
     * @param systemFaq
     * @return
     */
    ApiResultDTO saveSystemFaq(String locale, SystemFAQ systemFaq);

    /**
     * @param faqId
     * @return
     */
    SystemFAQ getSystemFaqById(Long faqId);

    /**
     * @param locale
     * @param systemFAQ
     * @return
     */
    ApiResultDTO modifySystemFaq(String locale, SystemFAQ systemFAQ);
    
    /**
     * @param status
     * @return
     */
    List<SystemFAQPrintDTO> findAllActiveFAQ(String status);
}
