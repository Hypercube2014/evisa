/**
 * 
 */
package com.hypercube.evisa.applicant.api.util;

import java.nio.charset.StandardCharsets;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.hypercube.evisa.common.api.constants.CommonsConstants;

import lombok.extern.slf4j.Slf4j;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author SivaSreenivas
 *
 */
@Slf4j
public class ApplicantUtil {

    /**
     * @param encrypted
     * @param secret
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypted) throws Exception {
        log.info("ApplicantUtil-decrypt {}", encrypted);

        /* decrypt the text */
        Cipher dcipher = Cipher.getInstance(CommonsConstants.AES_VALUE);

        /* Create key and cipher */
        SecretKey aesKey = new SecretKeySpec(CommonsConstants.SECRET_CIPHER_KEY.getBytes(StandardCharsets.UTF_8),
                CommonsConstants.AES_VALUE);

        dcipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] utf8 = dcipher.doFinal(Base64.getDecoder().decode(encrypted));

        /* Decode using utf-8 */
        return new String(utf8, StandardCharsets.UTF_8);
    }

    /**
     * @param plainString
     * @param secret
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainString) throws Exception {
        try {
            log.info("ApplicantUtil-encrypt {}", plainString);

            byte[] utf8 = plainString.getBytes(StandardCharsets.UTF_8);

            /* Create key and cipher */
            SecretKey aesKey = new SecretKeySpec(CommonsConstants.SECRET_CIPHER_KEY.getBytes(StandardCharsets.UTF_8),
                    CommonsConstants.AES_VALUE);

            /* encrypt the text */
            Cipher ecipher = Cipher.getInstance(CommonsConstants.AES_VALUE);
            ecipher.init(Cipher.ENCRYPT_MODE, aesKey);

            /* Encrypt */
            byte[] enc = ecipher.doFinal(utf8);

            return Base64.getEncoder().encodeToString(enc);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return null;
    }

    public boolean isRequestUserSameAsAuthenticated(String requestedUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
    
        String authenticatedUsername = authentication.getName();
        return authenticatedUsername.equals(requestedUsername);
    }
}

