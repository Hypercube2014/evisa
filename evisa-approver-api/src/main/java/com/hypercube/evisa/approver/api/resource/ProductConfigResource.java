package com.hypercube.evisa.approver.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.ProductConfig;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ProductConfigDTO;
import com.hypercube.evisa.common.api.model.ProductConfigSearchDTO;
import com.hypercube.evisa.common.api.service.ProductConfigService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
@Data
public class ProductConfigResource {

	/**
	 * 
	 */
	@Autowired(required = true)
	private ProductConfigService productConfigService;

	/**
	 * @param locale
	 * @param authorization
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/productconfig")
	public ResponseEntity<ApiResultDTO> createProductConfig(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ProductConfigDTO productConfigDTO) {
		log.info("ProductConfigResource-createProductConfig");
		return new ResponseEntity<>(productConfigService.createProductConfig(productConfigDTO), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @return
	 */
	@CrossOrigin
	@PutMapping(value = "/v1/api/productconfig")
	public ResponseEntity<ApiResultDTO> updateProductConfig(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ProductConfigDTO productConfigDTO) {
		log.info("ProductConfigResource-updateProductConfig");
		return new ResponseEntity<>(productConfigService.updateProductConfig(productConfigDTO), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param configid
	 * @return
	 */
	@CrossOrigin
	@GetMapping(value = "/v1/api/productconfig/{configid}")
	public ResponseEntity<ProductConfig> fetchProductConfig(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@PathVariable("configid") Long configid) {
		log.info("ProductConfigResource-fetchProductConfig");
		return new ResponseEntity<>(productConfigService.findByConfigId(configid), HttpStatus.OK);
	}

	/**
	 * @param locale
	 * @param authorization
	 * @param productConfigSearchDTO
	 * @return
	 */
	@CrossOrigin
	@PostMapping(value = "/v1/api/searchproductconfig")
	public ResponseEntity<Page<ProductConfig>> searchProductConfig(
			@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
			@RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
			@RequestBody ProductConfigSearchDTO productConfigSearchDTO) {
		log.info("ProductConfigResource-searchProductConfig");
		return new ResponseEntity<>(productConfigService.searchProductConfig(productConfigSearchDTO), HttpStatus.OK);
	}

}
