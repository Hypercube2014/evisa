package com.hypercube.evisa.common.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hypercube.evisa.common.api.customsrepo.ProductConfigCustomsRepo;
import com.hypercube.evisa.common.api.domain.ProductConfig;

/**
 * @author SivaSreenivas
 *
 */
@Repository
public interface ProductConfigRepository extends JpaRepository<ProductConfig, Long>, ProductConfigCustomsRepo {

    /**
     * @param configCode
     * @return
     */
    ProductConfig findByConfigCode(String configCode);

    /**
     * @param configCode
     * @return
     */
    @Query(value = "SELECT pc.configValue FROM ProductConfig pc WHERE pc.configCode = :configCode ")
    String getConfigValueByConfigCode(@Param("configCode") String configCode);

    /**
     * @param configId
     * @return
     */
    ProductConfig findByConfigId(Long configId);

    /**
     * @param configCode
     * @return
     */
    boolean existsByConfigCode(String configCode);

}
