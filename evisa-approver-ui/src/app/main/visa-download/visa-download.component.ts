import { Component, Injector, OnInit } from "@angular/core";
import { NgxUiLoaderService } from "ngx-ui-loader";
import { TranslateService } from "@ngx-translate/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from "@angular/common/http";
import { BaseComponent } from "src/app/common/commonComponent";

@Component({
  selector: 'app-visa-download',
  templateUrl: './visa-download.component.html',
  styleUrls: ['./visa-download.component.css']
})
export class VisaDownloadComponent extends BaseComponent implements OnInit {

  // Propriétés pour le téléchargement de visa
  public downloadApplicationNumber: string = '';
  public isDownloading: boolean = false;
  public downloadError: string = '';
  public downloadSuccess: string = '';

  // Propriétés pour la validation du format
  public formatError: boolean = false;

  constructor(
    inj: Injector,
    public translate: TranslateService,
    private ngxLoader: NgxUiLoaderService,
    public http: HttpClient
  ) {
    super(inj);
    translate.use("fr");
    localStorage.setItem("Language", "fr");
    this.setToken("Language", "fr");
  }

  ngOnInit(): void {
    // Initialisation du composant
    console.log("VisaDownloadComponent initialisé");
  }

  /**
   * Valider le numéro de dossier avec format strict
   */
  validateApplicationNumber(event: any) {
    const value = event.target.value;
    this.downloadApplicationNumber = value.trim().toUpperCase();
    this.downloadError = '';
    this.downloadSuccess = '';
    this.formatError = false;

    // Validation du format en temps réel
    if (this.downloadApplicationNumber.length > 0) {
      const formatRegex = /^[A-Z][0-9]{12}$/;
      if (!formatRegex.test(this.downloadApplicationNumber)) {
        this.formatError = true;
        this.downloadError = 'Format invalide. Attendu: 1 lettre + 12 chiffres (ex: A250911000012)';
      }
    }
  }

  /**
   * Effacer le formulaire de téléchargement
   */
  clearDownloadForm() {
    this.downloadApplicationNumber = '';
    this.downloadError = '';
    this.downloadSuccess = '';
    this.formatError = false;
  }

  /**
   * Télécharger le visa approuvé par numéro de dossier - API PUBLIQUE (pas d'authentification)
   */
  downloadApprovedVisa(applicationNumber?: string) {
    const appNumber = applicationNumber || this.downloadApplicationNumber;

    // Validation initiale
    if (!appNumber || appNumber.trim() === '') {
      this.downloadError = this.translate.instant('EVISASEARCH.ApplicationNumberRequired') || 'Numéro de demande requis';
      return;
    }

    // Validation du format
    const formatRegex = /^[A-Z][0-9]{12}$/;
    if (!formatRegex.test(appNumber.trim())) {
      this.downloadError = 'Format invalide. Attendu: 1 lettre majuscule + 12 chiffres (ex: A250911000012)';
      return;
    }

    this.isDownloading = true;
    this.downloadError = '';
    this.downloadSuccess = '';
    this.formatError = false;
    this.ngxLoader.start();

    // Construire l'URL de la nouvelle API publique
    const apiUrl = `${this.commonService._apiUrl}v1/api/downloadApprovedVisaPdf/${appNumber.trim()}`;

    console.log('URL API publique:', apiUrl);

    // Headers simples sans authentification (API publique)
    const headers = new HttpHeaders({
      'Accept': 'application/pdf',
      'Content-Type': 'application/json'
    });

    // Faire l'appel HTTP à l'API publique
    this.http.get(apiUrl, {
      headers: headers,
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        this.handleDownloadSuccess(response, appNumber);
      },
      error: (error: HttpErrorResponse) => {
        this.handleDownloadError(error, appNumber);
      }
    });
  }

  /**
   * Gérer le succès du téléchargement
   */
  private handleDownloadSuccess(response: any, applicationNumber: string) {
    this.isDownloading = false;
    this.ngxLoader.stop();

    // Créer un blob et télécharger le fichier
    const blob = response.body;
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `visa_${applicationNumber}.pdf`;
    link.target = '_blank';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    // Nettoyer l'URL blob
    window.URL.revokeObjectURL(url);

    // Afficher le message de succès
    this.downloadSuccess = 'Téléchargement réussi !';
    this.downloadApplicationNumber = '';

    if (localStorage.getItem("Language") === "en") {
      this.toastr.successToastr("Visa downloaded successfully", "Success");
    } else {
      this.toastr.successToastr("Visa téléchargé avec succès", "Succès");
    }
  }

  /**
   * Gérer les erreurs du téléchargement avec messages spécifiques
   */
  private handleDownloadError(error: HttpErrorResponse, applicationNumber: string) {
    this.isDownloading = false;
    this.ngxLoader.stop();

    console.error('Erreur de téléchargement:', error);

    let errorMessage = '';
    let toastrMessage = '';

    // Gérer les différents codes d'erreur HTTP
    switch (error.status) {
      case 401:
        errorMessage = 'Session expirée. Veuillez vous reconnecter.';
        toastrMessage = 'Session expirée';
        // Rediriger vers la page de login
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
        break;

      case 403:
        // Extraire le message d'erreur détaillé si disponible
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        } else {
          errorMessage = 'Accès refusé. Vous n\'avez pas les droits pour télécharger ce visa.';
        }
        toastrMessage = 'Accès refusé';
        break;

      case 404:
        errorMessage = `Dossier non trouvé pour le numéro: ${applicationNumber}`;
        toastrMessage = 'Dossier non trouvé';
        break;

      case 409:
        // Extraire le message sur le statut du visa
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        } else {
          errorMessage = `Le visa ${applicationNumber} n'est pas approuvé et ne peut pas être téléchargé.`;
        }
        toastrMessage = 'Visa non approuvé';
        break;

      case 400:
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        } else {
          errorMessage = 'Format de numéro de demande invalide.';
        }
        toastrMessage = 'Paramètre invalide';
        break;

      case 500:
        errorMessage = 'Erreur technique du serveur. Veuillez réessayer plus tard.';
        toastrMessage = 'Erreur serveur';
        break;

      case 0:
        errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion.';
        toastrMessage = 'Connexion impossible';
        break;

      default:
        errorMessage = `Erreur inattendue (${error.status}). Veuillez contacter le support.`;
        toastrMessage = 'Erreur inattendue';
        break;
    }

    this.downloadError = errorMessage;

    // Afficher le toast d'erreur
    if (localStorage.getItem("Language") === "en") {
      this.toastr.errorToastr(toastrMessage, "Error");
    } else {
      this.toastr.errorToastr(toastrMessage, "Erreur");
    }
  }

  /**
   * Télécharger le visa depuis la liste (pour utilisation dans d'autres composants)
   */
  downloadVisaFromList(applicationNumber: string) {
    if (applicationNumber && applicationNumber.trim() !== '') {
      this.downloadApprovedVisa(applicationNumber.trim());
    }
  }

  /**
   * Vérifier si le format du numéro est valide
   */
  isValidFormat(): boolean {
    if (!this.downloadApplicationNumber) return true;
    const formatRegex = /^[A-Z][0-9]{12}$/;
    return formatRegex.test(this.downloadApplicationNumber);
  }
}
