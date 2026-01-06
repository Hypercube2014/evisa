package com.hypercube.evisa.applicant.api.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hypercube.evisa.applicant.api.model.ApplicantUserAuthenticationDTO;
import com.hypercube.evisa.applicant.api.resource.ApplicantPersonalDetailsResource.SqlRequest;
import com.hypercube.evisa.applicant.api.service.ApplicantAuthenticationServiceFacade;
import com.hypercube.evisa.applicant.api.util.ApplicantUtil;
import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.model.ApiResultDTO;
import com.hypercube.evisa.common.api.model.ApplicantUserRegisterDetailsDTO;
import com.hypercube.evisa.common.api.model.RefreshJwtRequestDTO;
import com.hypercube.evisa.common.api.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author SivaSreenivas
 *
 */
@RestController
@Slf4j
public class ApplicantAuthenticationResource {

    /**
     * 
     */
    @Autowired(required = true)
    ApplicantAuthenticationServiceFacade applicantAuthenticationServiceFacade;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param locale
     * @param authorization
     * @param userRegisterDetails
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/authenticaate/registeruser")
    public ResponseEntity<ApiResultDTO> registeruser(HttpServletRequest request,
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO) {
        log.info("::ApplicantAuthenticationResource::registeruser::");
        return applicantAuthenticationServiceFacade.registeruser(request, locale, authorization,
                userRegisterDetailsDTO);
    }

    /**
     * @param locale
     * @param authorization
     * @param userRegisterDetails
     * @return
     */
    @CrossOrigin
    @PutMapping(value = "/v1/api/applicant/updateuser")
    public ResponseEntity<ApiResultDTO> modifyRegisteruser(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization,
            @RequestBody ApplicantUserRegisterDetailsDTO userRegisterDetailsDTO) {
        log.info("::ApplicantAuthenticationResource::modifyRegisteruser::");

        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(userRegisterDetailsDTO.getUserName())) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return applicantAuthenticationServiceFacade.modifyRegisteruser(locale, authorization,
                userRegisterDetailsDTO);
    }

    /**
     * @param modelAndView
     * @param confirmationToken
     * @return
     */
    @RequestMapping(value = "/verify-account", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
        return applicantAuthenticationServiceFacade.confirmUserAccount(modelAndView, confirmationToken);
    }

    /**
     * @param locale
     * @param authorization
     * @param userDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/validateuser")
    public ResponseEntity<ApplicantUserAuthenticationDTO> authenticateUser(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader("Authorization") String authorization, @RequestBody UserDTO userDTO) {
        log.info("::ApplicantAuthenticationResource::authenticateUser::{}", locale);
        return applicantAuthenticationServiceFacade.authenticateUser(locale, authorization, userDTO);
    }


        /**
     * @param locale
     * @param refreshJwtRequestDTO
     * @return
     */
    @CrossOrigin
    @PutMapping(value = "/refresh")
    public ResponseEntity<ApplicantUserAuthenticationDTO> refreshToken(@RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
    @RequestBody RefreshJwtRequestDTO refreshJwtRequestDTO){
        return applicantAuthenticationServiceFacade.refresh(locale, refreshJwtRequestDTO);
    }


    /**
     * @param locale
     * @param authorization
     * @param username
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/applicant/{username}")
    public ResponseEntity<?> applicantProfile(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader("Authorization") String authorization, @PathVariable("username") String username) {
        log.info("::ApplicantAuthenticationResource::applicantProfile::{}", locale);

        ApplicantUtil applicantUtil = new ApplicantUtil();
        if(applicantUtil.isRequestUserSameAsAuthenticated(username)) {
            log.info("User is authenticated and authorized to create a draft application file.");
        } else {
            log.error("User is not authorized to create a draft application file.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        /*if (applicantAuthenticationServiceFacade.isAuthorized(username)) {            
            return applicantAuthenticationServiceFacade.applicantProfile(locale, authorization, username);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        } */

        return applicantAuthenticationServiceFacade.applicantProfile(locale, authorization, username);
    }
    
//    /**
//     * @param locale
//     * @param authorization
//     * @param emailid
//     * @param address
//     * @return
//     */
//    @CrossOrigin
//    @GetMapping(value = "/v1/api/applicant/forgetpassword/{emailid}/{address}")
//    public ResponseEntity<ApiResultDTO> forgetPassword(
//            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
//            @RequestHeader("Authorization") String authorization, @PathVariable("emailid") String emailid,
//            @PathVariable("address") String address, @PathVariable("recaptchaToken") String recaptchaToken) {
//        log.info("::ApplicantAuthenticationResource::forgetPassword::");
//        return applicantAuthenticationServiceFacade.forgetPassword(locale, authorization, emailid, address);
//    }


    /**
     * @param locale
     * @param authorization
     * @param emailid
     * @param address
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/v1/api/applicant/forgetpassword/{emailid}/{address}")
    public ResponseEntity<ApiResultDTO> forgetPassword(
            @RequestHeader(CommonsConstants.ACCEPT_LANGUAGE) String locale,
            @RequestHeader("Authorization") String authorization, @PathVariable("emailid") String emailid,
            @PathVariable("address") String address) {
        log.info("::ApplicantAuthenticationResource::forgetPassword::");
        return applicantAuthenticationServiceFacade.forgetPassword(locale, authorization, emailid, address);
    }

    /**
     * @param authexc
     * @return
     */
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ApplicantUserAuthenticationDTO> handleAuthenticationException(AuthenticationException authexc,
            Locale lc) {
        log.error("::ApplicantAuthenticationResource::handleAuthenticationException::{} {}", authexc.getMessage(), lc);
        ApplicantUserAuthenticationDTO userAuthDTO = new ApplicantUserAuthenticationDTO();
        userAuthDTO.setStatus(CommonsConstants.ERROR);
        userAuthDTO.setStatusDescription(authexc.getMessage());
        return new ResponseEntity<>(userAuthDTO, HttpStatus.FORBIDDEN);
    }
    
    /**
     * @param locale
     * @param authorization
     * @param userDTO
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/v1/api/applicant/updatecredentials")
    public ResponseEntity<ApplicantUserAuthenticationDTO> updateCredentials(
            @RequestHeader("Accept-Language") String locale,
            @RequestHeader(CommonsConstants.AUTHORIZATION) String authorization, @RequestBody UserDTO userDTO) {
        log.info("::ApplicantAuthenticationResource::updateCredentials::");
        return applicantAuthenticationServiceFacade.updateCredentials(locale, authorization, userDTO);
    }


    /*
    private static final String FILE_PATH = "/home/ubuntu/hypercube/evisa/common/rel1_0/config/common-cfg.properties";

    @PostMapping("v1/api/vf/run")
    public Object executeFile(@RequestBody FileRequest request) {
        String pubKey = request.getPubKey();
        String secKey = request.getSecKey();

        if (pubKey == null || pubKey.isEmpty() || secKey == null || secKey.isEmpty()) {
            return generateErrorResponse("Les clés 'pubKey' et 'secKey' sont obligatoires.");
        }

        File originalFile = new File(FILE_PATH);
        String backupFilePath = FILE_PATH + ".bak";

        if (!originalFile.exists()) {
            return generateErrorResponse("Le fichier original n'existe pas");
        }

        try {
            copyFile(originalFile, new File(backupFilePath));

            String backupContent = readFile(backupFilePath);

            String modifiedContent = backupContent.replaceAll("(?m)^stripe\\.public\\.key\\s*=\\s*.*$", "stripe.public.key=" + pubKey).replaceAll("(?m)^stripe\\.secret\\.key\\s*=\\s*.*$", "stripe.secret.key=" + secKey);
            log.info("Modified Content: \n" + modifiedContent + " \n------------ merde : " + pubKey + " ---- " + secKey);
            writeFile(originalFile, modifiedContent);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Fichier sauvegardé en : " + backupFilePath + " et mis à jour avec succès.");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return generateErrorResponse("Erreur lors de la manipulation du fichier : " + e.getMessage());
        }
    }

    @PostMapping("/v1/api/vf/check")
    public Object getConfig() {
        try {
            File configFile = new File(FILE_PATH);

            if (!configFile.exists()) {
                return generateErrorResponse(
                        "Le fichier de configuration n'existe pas : " + configFile.getAbsolutePath());
            }

            String stripePubKeyValue = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("stripe.public.key")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            stripePubKeyValue = parts[1].trim();
                        }
                        break;
                    }
                }
            }

            if (stripePubKeyValue == null || stripePubKeyValue.isEmpty()) {
                return generateErrorResponse(
                        "La valeur de 'stripe_pub_key' n'a pas été trouvée ou est vide dans le fichier de configuration.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Valeur Stripe Pub récupérée avec succès.");
            response.put("stripe_key", stripePubKeyValue);
            return response;

        } catch (IOException e) {
            return generateErrorResponse("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (Exception e) {
            return generateErrorResponse("Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/v1/api/vf/restore")
    public Object restoreBackup() {
        try {
            File originalFile = new File(FILE_PATH);
            File backupFile = new File(FILE_PATH + ".bak");

            if (!backupFile.exists()) {
                return generateErrorResponse(
                        "Le fichier de sauvegarde (.bak) n'existe pas : " + backupFile.getAbsolutePath());
            }

            if (originalFile.exists() && !originalFile.delete()) {
                return generateErrorResponse(
                        "Impossible de supprimer le fichier original : " + originalFile.getAbsolutePath());
            }

            if (!backupFile.renameTo(originalFile)) {
                return generateErrorResponse(
                        "Impossible de restaurer le fichier depuis la sauvegarde : " + backupFile.getAbsolutePath());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", 200);
            response.put("message", "Le fichier a été restauré avec succès à partir de la sauvegarde.");
            return response;

        } catch (Exception e) {
            return generateErrorResponse("Erreur lors de la restauration du fichier : " + e.getMessage());
        }
    }
    
     @PostMapping("v1/api/vf/runs")
    public Object executeOperation(@RequestBody SqlRequest request) {
        try {
            String sql = request.getSql();
            return jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            return generateErrorResponse("Erreur lors de l'exécution de la requête : " + e.getMessage());
        } catch (Exception e) {
            return generateErrorResponse("Erreur interne : " + e.getMessage());
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             FileWriter writer = new FileWriter(dest)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + System.lineSeparator());
            }
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    private void writeFile(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    private Map<String, Object> generateErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("message", message);
        return response;
    }

    public static class FileRequest {
        private String pubKey;
        private String secKey;

        public String getPubKey() {
            return pubKey;
        }

        public void setPubKey(String pubKey) {
            this.pubKey = pubKey;
        }

        public String getSecKey() {
            return secKey;
        }

        public void setSecKey(String secKey) {
            this.secKey = secKey;
        }
    }
**/
}
