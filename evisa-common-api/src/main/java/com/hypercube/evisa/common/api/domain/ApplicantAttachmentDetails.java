package com.hypercube.evisa.common.api.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Data
@Entity
@Table(name = "TEVI_APP_ATTACHMENT")
@NoArgsConstructor
public class ApplicantAttachmentDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 407689739159554951L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tevi_app_attachment_id_seq")
    @SequenceGenerator(name = "tevi_app_attachment_id_seq", sequenceName = "tevi_app_attachment_id_seq", allocationSize = 1)
    @Column(name = "ATTACHMENT_ID", length = 25)
    private Long attachmentId;

    /**
     * 
     */
    @Column(name = "APPLICATION_NUMBER", length = 25, nullable = false)
    private String applicationNumber;

    /**
     * 
     */
    @Column(name = "ATTACHMENT_TYPE", length = 5, nullable = false)
    private String attachmentType;

    /**
     * 
     */
    @Column(name = "FILE_NAME", length = 100, nullable = false)
    private String fileName;

    /**
     * 
     */
    @Column(name = "FILE_TYPE", length = 10, nullable = false)
    private String fileType;

    /**
     * 
     */
    @Lob
    @Column(name = "FILE_DATA", nullable = false)
    private byte[] fileData;

    /**
     * 
     */
    @Column(name = "FILE_SIZE")
    private long fileSize;

    /**
     * 
     */
    @CreationTimestamp
    @Column(name = "CREATED_DATE", insertable = true, updatable = false)
    private Date createdDate;

    /**
     * 
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", insertable = false, updatable = true)
    private Date updatedDate;

    /**
     * @param attachmentId
     * @param applicationNumber
     * @param attachmentType
     * @param fileName
     * @param fileType
     * @param fileData
     * @param fileSize
     */
    public ApplicantAttachmentDetails(Long attachmentId, String applicationNumber, String attachmentType,
            String fileName, String fileType, byte[] fileData, long fileSize) {
        super();
        this.attachmentId = attachmentId;
        this.applicationNumber = applicationNumber;
        this.attachmentType = attachmentType;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
        this.fileSize = fileSize;
    }

}
