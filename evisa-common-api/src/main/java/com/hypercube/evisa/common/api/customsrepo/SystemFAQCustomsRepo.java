/**
 * 
 */
package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.domain.SystemFAQ;
import com.hypercube.evisa.common.api.model.SystemFAQSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface SystemFAQCustomsRepo {
    
    /**
     * @param locale
     * @param systemFAQSearchDTO
     * @return
     */
    Page<SystemFAQ> searchSystemFaq(String locale, SystemFAQSearchDTO systemFAQSearchDTO);

}
