/**
 * 
 */
package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.ApplicantPersonalDetailsSearchDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface ApplicantPersonalDetailsCustomsRepo {

    /**
     * @param applicantPersonalDetailsSearchDTO
     * @return
     */
    Page<ApplicantPersonalDetailsSearchDTO> searchApplications(
            ApplicantPersonalDetailsSearchDTO applicantPersonalDetailsSearchDTO);

}
