/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_APPLICATION_FILE")
@NoArgsConstructor
public class ApplicationFile implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -695728006628922578L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "djibevisa_file_no_seq")
    @GenericGenerator(
        name = "djibevisa_file_no_seq", 
        strategy = "com.hypercube.evisa.common.api.util.DatePrefixedSequenceIdGenerator", 
                parameters = {
                        @Parameter(name = "valuePrefix", value = "F"),
                        })
    @Column(name = "FILE_NUMBER", length = 25)
    private String fileNumber;

    /**
     * 
     */
    @Column(name = "USERNAME", length = 25, nullable = false)
    private String username;

    /**
     * 
     */
    @Column(name = "IS_EXPRESS_VISA")
    private boolean isExpressVisa;

    /**
     * 
     */
    @Column(name = "TOTAL_GROUP_APPLICATIONS", length = 2)
    private int totalGroupApplications;

    /**
     * 
     */
    @Column(name = "APPLICANT_TYPE", length = 5, nullable = false)
    private String applicantType;

    /**
     * 
     */
    @Column(name = "VISA_TYPE", length = 5, nullable = false)
    private String visaType;
    
    /**
     * 
     */
    @Column(name = "STATUS", length = 5, nullable = false)
    private String status;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * @param fileNumber
     * @param isExpressVisa
     * @param applicantType
     * @param visaType
     * @param createdDate
     */
    public ApplicationFile(String fileNumber, boolean isExpressVisa, String applicantType, String visaType,
            Date createdDate) {
        super();
        this.fileNumber = fileNumber;
        this.isExpressVisa = isExpressVisa;
        this.applicantType = applicantType;
        this.visaType = visaType;
        this.createdDate = createdDate;
    }
    
}
