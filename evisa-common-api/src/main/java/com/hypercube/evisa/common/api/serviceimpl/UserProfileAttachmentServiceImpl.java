/**
 * 
 */
package com.hypercube.evisa.common.api.serviceimpl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypercube.evisa.common.api.configuration.LocaleConfig;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.domain.UserProfileAttachment;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.repository.UserProfileAttachmentRepository;
import com.hypercube.evisa.common.api.service.UserProfileAttachmentService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@Service
@Slf4j
@Data
public class UserProfileAttachmentServiceImpl implements UserProfileAttachmentService {

    /**
     * 
     */
    @Autowired(required = true)
    private UserProfileAttachmentRepository userProfileAttachmentRepository;

    /**
     * 
     */
    @Override
    public ApiResultDTO saveUserProfileAttachment(String locale, UserProfileAttachment userProfileAttachment) {
        log.info("UserProfileAttachmentServiceImpl-saveUserProfileAttachment");
        userProfileAttachmentRepository.save(userProfileAttachment);

        return new ApiResultDTO(CommonsConstants.SUCCESS,
                LocaleConfig.getResourceValue("save.success", null, locale, null),
                Base64.getEncoder().encodeToString(userProfileAttachment.getFileData()));
    }

    /**
     * 
     */
    @Override
    public UserProfileAttachment getUserProfileAttachment(String username) {
        log.info("UserProfileAttachmentServiceImpl-getUserProfileAttachment");
        return userProfileAttachmentRepository.findByUsername(username);
    }

    /**
     *
     */
    @Override
    public ApiResultDTO removeProfilePic(String username) {
        log.info("UserProfileAttachmentServiceImpl-removeProfilePic");
        
        userProfileAttachmentRepository.deleteByUsername(username);
        return new ApiResultDTO(CommonsConstants.SUCCESS, "Removed Pic Successfully!", "");
    }

}
