/**
 * 
 */
package com.hypercube.evisa.common.api.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SivaSreenivas
 *
 */
@Entity
@Table(name = "user_profile_attachment")
@Data
@NoArgsConstructor
public class UserProfileAttachment {

    @Id
    @Column(name = "username", length = 25)
    private String username;

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
     * @param username
     * @param fileType
     * @param fileData
     */
    public UserProfileAttachment(String username, String fileType, byte[] fileData) {
        super();
        this.username = username;
        this.fileType = fileType;
        this.fileData = fileData;
    }

}
