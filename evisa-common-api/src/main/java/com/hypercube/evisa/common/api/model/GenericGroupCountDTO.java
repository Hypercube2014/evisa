/**
 * 
 */
package com.hypercube.evisa.common.api.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SivaSreenivas
 *
 */
@Setter
@Getter
public class GenericGroupCountDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9182663641897229793L;
    /**
     * 
     */
    private String refNo;
    /**
     * 
     */
    private long count;

    /**
     * @param refNo
     * @param count
     */
    public GenericGroupCountDTO(String refNo, long count) {
        super();
        this.refNo = refNo;
        this.count = count;
    }

}
