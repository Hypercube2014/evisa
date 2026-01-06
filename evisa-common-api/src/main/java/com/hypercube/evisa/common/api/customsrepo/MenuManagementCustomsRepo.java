package com.hypercube.evisa.common.api.customsrepo;

import org.springframework.data.domain.Page;

import com.hypercube.evisa.common.api.model.MenuDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchResultsDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MenuManagementCustomsRepo {

    /**
     * @param menuDetailsSearchDTO
     * @return
     */
    Page<MenuDetailsSearchResultsDTO> searchMenuDetails(MenuDetailsSearchDTO menuDetailsSearchDTO);

}
