/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class MasterCodeResultDTO {

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String codeType;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String descriptionOther;

    /**
     * 
     */
    private int visaDuration;

    /**
     * 
     */
    private Long visaFee;

    /**
     * @param code
     * @param description
     * @param descriptionOther
     */
    public MasterCodeResultDTO(String code, String description, String descriptionOther) {
        this.code = code;
        this.description = description;
        this.descriptionOther = descriptionOther;
    }

    /**
     * @param code
     * @param codeType
     * @param description
     * @param descriptionOther
     */
    public MasterCodeResultDTO(String code, String codeType, String description, String descriptionOther) {
        super();
        this.code = code;
        this.codeType = codeType;
        this.description = description;
        this.descriptionOther = descriptionOther;
    }

    /**
     * @param code
     * @param description
     * @param descriptionOther
     * @param visaDuration
     * @param visaFee
     */
    public MasterCodeResultDTO(String code, String description, String descriptionOther, int visaDuration,
            Long visaFee) {
        super();
        this.code = code;
        this.description = description;
        this.descriptionOther = descriptionOther;
        this.visaDuration = visaDuration;
        this.visaFee = visaFee;
    }

}
