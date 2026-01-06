package com.hypercube.evisa.common.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_VISA_EXTENSION_UPLOAD")
@NoArgsConstructor
public class ApplicantVisaExtensionFileUpload {

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tevi_visa_extension_upload_file_id_seq")
    @SequenceGenerator(name = "tevi_visa_extension_upload_file_id_seq", sequenceName = "tevi_visa_extension_upload_file_id_seq", allocationSize = 1)
    @Column(name = "file_id", length = 25)
    private Long fileId;

    /**
     * 
     */
    @Column(name = "visa_extension_id", length = 25, nullable = false)
    private String visaExtensionId;

    /**
     * 
     */
    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
    
    /**
     * 
     */
    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType;

    /**
     * 
     */
    @Column(name = "FILE_NAME", length = 255, nullable = false)
    private String fileName;

    /**
     * 
     */
    @Column(name = "FILE_SIZE", length = 25)
    private long fileSize;

    /**
     * @param visaExtensionId
     * @param fileType
     * @param fileData
     * @param fileName
     * @param fileSize
     */
    public ApplicantVisaExtensionFileUpload(String visaExtensionId, String fileType, byte[] fileData, String fileName, long fileSize) {
        super();
        this.visaExtensionId = visaExtensionId;
        this.fileType = fileType;
        this.fileData = fileData;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

}
