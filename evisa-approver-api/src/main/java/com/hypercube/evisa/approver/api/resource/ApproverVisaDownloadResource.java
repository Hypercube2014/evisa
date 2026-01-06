package com.hypercube.evisa.approver.api.resource;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.approver.api.exception.VisaNotApprovedException;
import com.hypercube.evisa.approver.api.exception.VisaNotFoundException;
import com.hypercube.evisa.approver.api.model.ErrorResponse;
import com.hypercube.evisa.approver.api.service.ApproverCommonServiceFacade;
import com.hypercube.evisa.common.api.domain.ApplicationTracker;
import com.hypercube.evisa.common.api.service.ApplicationTrackerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/api/downloadApprovedVisaPdf")
@Slf4j
@CrossOrigin(origins = "*")
public class ApproverVisaDownloadResource {

    private static final String APPROVED_STATUS = "APR";

    @Autowired
    private ApproverCommonServiceFacade approverCommonServiceFacade;

    @Autowired
    private ApplicationTrackerService applicationTrackerService;

    /**
     * Télécharge le PDF d'un visa approuvé - API PUBLIQUE
     * Aucune authentification requise
     *
     * @param applicationNumber Numéro de la demande de visa (format: A + 12 chiffres)
     * @param request Requête HTTP pour les logs
     * @return Le fichier PDF du visa ou une erreur appropriée
     */
    @GetMapping("/{applicationNumber}")
    public ResponseEntity<?> downloadApprovedVisa(
            @PathVariable("applicationNumber")
            @NotBlank(message = "Le numéro de demande ne peut pas être vide")
            @Pattern(regexp = "^[A-Z][0-9]{12}$", message = "Format de numéro de demande invalide")
            String applicationNumber,
            HttpServletRequest request) {

        log.info("Téléchargement public du visa: {}", applicationNumber);

        try {
            // 1. Validation du format du numéro de demande
            validateApplicationNumberFormat(applicationNumber);

            // 2. Vérification de l'existence du dossier
            ApplicationTracker tracker = findApplicationTracker(applicationNumber);

            // 3. Vérification du statut du visa (doit être approuvé)
            validateVisaApprovalStatus(tracker, applicationNumber);

            // 4. Génération et retour du PDF
            Resource pdfResource = generateVisaPdf(applicationNumber, request);

            // 5. Log de succès
            log.info("Téléchargement public réussi du visa: {}", applicationNumber);

            return createSuccessResponse(pdfResource, applicationNumber);

        } catch (VisaNotFoundException e) {
            log.warn("Dossier non trouvé: {}", applicationNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse("VISA_NOT_FOUND", e.getMessage()));

        } catch (VisaNotApprovedException e) {
            log.warn("Tentative de téléchargement d'un visa non approuvé: {} - Statut: {}",
                    applicationNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse("VISA_NOT_APPROVED", e.getMessage()));

        } catch (IllegalArgumentException e) {
            log.warn("Paramètre invalide pour le téléchargement du visa: {} - Erreur: {}",
                    applicationNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse("INVALID_PARAMETER", e.getMessage()));

        } catch (Exception e) {
            log.error("Erreur technique lors du téléchargement du visa: {}", applicationNumber, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse("TECHNICAL_ERROR",
                            "Une erreur technique s'est produite. Veuillez réessayer plus tard."));
        }
    }

    /**
     * Valide le format du numéro de demande
     */
    private void validateApplicationNumberFormat(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de demande ne peut pas être vide");
        }

        String trimmed = applicationNumber.trim();
        if (!trimmed.matches("^[A-Z][0-9]{12}$")) {
            throw new IllegalArgumentException(
                "Format de numéro de demande invalide. Format attendu: 1 lettre majuscule + 12 chiffres (ex: A250911000012)");
        }
    }

    /**
     * Recherche et valide l'existence du dossier de demande
     */
    private ApplicationTracker findApplicationTracker(String applicationNumber) {
        ApplicationTracker tracker = applicationTrackerService.getApplicationDetails(applicationNumber);

        if (tracker == null) {
            throw new VisaNotFoundException(
                String.format("Aucun dossier trouvé pour le numéro de demande: %s", applicationNumber));
        }

        return tracker;
    }

    /**
     * Valide que le visa est bien approuvé
     */
    private void validateVisaApprovalStatus(ApplicationTracker tracker, String applicationNumber) {
        String visaStatus = tracker.getVisaStatus();

        if (!APPROVED_STATUS.equals(visaStatus)) {
            throw new VisaNotApprovedException(
                String.format("Le visa avec le numéro %s n'est pas approuvé. Statut actuel: %s. Seuls les visas approuvés (APR) peuvent être téléchargés.",
                applicationNumber, visaStatus != null ? visaStatus : "INCONNU"));
        }
    }

    /**
     * Génère le PDF du visa via le service métier
     */
    private Resource generateVisaPdf(String applicationNumber, HttpServletRequest request) {
        try {
            ResponseEntity<Resource> pdfResponse = approverCommonServiceFacade.downloadApprovedVisaPdf(applicationNumber, request);

            if (pdfResponse == null || pdfResponse.getBody() == null) {
                throw new RuntimeException("Impossible de générer le PDF du visa");
            }

            return pdfResponse.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Crée la réponse de succès avec le fichier PDF
     */
    private ResponseEntity<Resource> createSuccessResponse(Resource pdfResource, String applicationNumber) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    String.format("attachment; filename=\"visa_%s.pdf\"", applicationNumber))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
    }
}
