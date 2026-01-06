/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class IndexDTO {

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String codeType;

    /**
     * @param code
     * @param codeType
     */
    public IndexDTO(String code, String codeType) {
        super();
        this.code = code;
        this.codeType = codeType;
    }

}
