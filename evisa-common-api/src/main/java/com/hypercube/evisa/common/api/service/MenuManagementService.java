package com.hypercube.evisa.common.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.hypercube.evisa.common.api.domain.MenuDetails;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchDTO;
import com.hypercube.evisa.common.api.model.MenuDetailsSearchResultsDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface MenuManagementService {

    /**
     * @param menuDetails
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> insertMenuDetails(MenuDetails menuDetails, String loggeduser, String locale);

    /**
     * @param menuDetails
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> updateMenuDetails(MenuDetails menuDetails, String loggeduser, String locale);

    /**
     * @param menuId
     * @param loggeduser
     * @return
     */
    ResponseEntity<ApiResultDTO> deleteMenudetails(Long menuId, String loggeduser, String locale);

    /**
     * @param menuId
     * @param loggeduser
     * @return
     */
    ResponseEntity<MenuDetails> viewMenuDetails(Long menuId, String loggeduser, String locale);

    /**
     * @param menuDetailsSearchDTO
     * @param loggeduser
     * @return
     */
    ResponseEntity<Page<MenuDetailsSearchResultsDTO>> searchMenuDetails(MenuDetailsSearchDTO menuDetailsSearchDTO,
            String loggeduser, String locale);

    /**
     * @param loggeduser
     * @param locale
     * @return
     */
    ResponseEntity<List<MenuDetails>> getActiveMenu(String loggeduser, String locale);

}
