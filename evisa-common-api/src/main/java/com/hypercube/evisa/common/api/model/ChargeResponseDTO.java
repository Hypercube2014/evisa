package com.hypercube.evisa.common.api.model;

import lombok.Data;

/**
 * @author SivaSreenivas
 *
 */
@Data
public class ChargeResponseDTO {

    /**
     * 
     */
    private String statusCode;

    /**
     * 
     */
    private String statusDesc;

    /**
     * 
     */
    private String recieptUrl;

    /**
     * 
     */
    private String refId;

    /**
     * @param statusCode
     * @param statusDesc
     * @param recieptUrl
     * @param refId
     */
    public ChargeResponseDTO(String statusCode, String statusDesc, String recieptUrl, String refId) {
        super();
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.recieptUrl = recieptUrl;
        this.refId = refId;
    }

    /**
     * @param statusCode
     * @param statusDesc
     * @param refId
     */
    public ChargeResponseDTO(String statusCode, String statusDesc, String refId) {
        super();
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
        this.refId = refId;
    }

}
