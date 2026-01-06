package com.hypercube.evisa.common.api.service;

import com.hypercube.evisa.common.api.domain.UserProfileAttachment;
import com.hypercube.evisa.common.api.model.ApiResultDTO;

/**
 * @author SivaSreenivas
 *
 */
public interface UserProfileAttachmentService {
    
    /**
     * @param userProfileAttachment
     * @return
     */
    ApiResultDTO saveUserProfileAttachment(String locale, UserProfileAttachment userProfileAttachment);
    
    /**
     * @param username
     * @return
     */
    UserProfileAttachment getUserProfileAttachment(String username);

    /**
     * @param username
     * @return
     */
    ApiResultDTO removeProfilePic(String username);
    

}
