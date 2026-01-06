package com.hypercube.evisa.applicant.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hypercube.evisa.applicant.api.service.ApplicantCommonServiceFacade;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Contrôleur REST pour exposer l'endpoint webhook Stripe
 * Délègue le traitement au service ApplicantCommonServiceFacadevf
 *
 * @author Generated
 */
@RestController
@RequestMapping("/api/stripe/webhook")
@Slf4j
public class StripeWebhookResource {

    @Autowired
    private ApplicantCommonServiceFacade applicantCommonServiceFacade;

    /**
     * Endpoint webhook pour recevoir les événements Stripe
     * IMPORTANT: Utilise byte[] pour préserver le payload exact (requis pour la signature)
     *
     * @param payloadBytes Payload brut en bytes de Stripe
     * @param sigHeader Signature Stripe pour vérification
     * @return ResponseEntity avec statut
     */
    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody byte[] payloadBytes,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        log.info("🎯 Réception webhook Stripe - Taille payload: {} bytes", payloadBytes.length);

        // Convertir les bytes en String avec UTF-8
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);

        log.info("🎯 Délégation webhook Stripe vers service principal");
        return applicantCommonServiceFacade.handleStripeWebhook(payload, sigHeader);
    }
}
