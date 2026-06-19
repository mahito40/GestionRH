package com.memoire.gestionrh.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.Models.HistoriqueValidation;
import com.memoire.gestionrh.Repository.DemandeRepository;
import com.memoire.gestionrh.Repository.HistoriqueValidationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfService {

    private final DemandeRepository              demandeRepository;
    private final HistoriqueValidationRepository  historiqueValidationRepository;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Couleurs ──
    private static final DeviceRgb VERT       = new DeviceRgb(34, 139, 34);
    private static final DeviceRgb VERT_CLAIR = new DeviceRgb(220, 240, 220);
    private static final DeviceRgb GRIS_CLAIR = new DeviceRgb(245, 245, 245);

    // ── Dimensions page A4 ──
    private static final float PAGE_W    = 595.32f;
    private static final float MARGIN    = 42f;
    private static final float CONTENT_W = PAGE_W - 2 * MARGIN; // 511.32 pts
    private static final float Y_START   = 748f;  // juste sous bandeau ISO

    public byte[] genererPdfDemande(UUID demandeId) throws Exception {

        // ── Charger la demande ──
        Demande d = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée : " + demandeId));

        // ── Récupérer la validation RH ──
        List<HistoriqueValidation> validations =
                historiqueValidationRepository.findByDemandeId(demandeId);
        HistoriqueValidation valRH = validations.stream()
                .filter(v -> v.getValidateur() != null
                        && v.getValidateur().getRole() != null
                        && v.getValidateur().getRole().getNom().equalsIgnoreCase("RH"))
                .max(Comparator.comparing(HistoriqueValidation::getCreatedAt))
                .orElse(null);

        // ── Données demande ──
        String nom     = d.getUtilisateur().getNom();
        String prenom  = d.getUtilisateur().getPrenom();
        String service = d.getUtilisateur().getService() != null
                       ? d.getUtilisateur().getService().getNom() : "";
        String poste   = d.getUtilisateur().getPoste() != null
                       ? d.getUtilisateur().getPoste() : "";
        String type    = d.getTypeDemande() != null ? d.getTypeDemande().name() : "";
        String debut   = d.getDateDebut()   != null ? d.getDateDebut().format(FMT) : "";
        String fin     = d.getDateFin()     != null ? d.getDateFin().format(FMT)   : "";
        String motif   = d.getMotif()       != null ? d.getMotif() : "";
        String dateD   = d.getDateDemande() != null ? d.getDateDemande().format(FMT) : "";

        boolean isConge   = type.toLowerCase().contains("conge");
        boolean isAbsence = type.toLowerCase().contains("absence");

        // ── Données RH ──
        String nomRemplacant    = "";
        String debutCollab      = "";
        String joursConsommes   = "";
        String dernierConge     = "";
        String joursDisponibles = "";
        String observation      = "";
        boolean cbConges        = false;
        boolean cbPaie          = false;
        boolean cbReglem        = false;

        if (valRH != null) {
            nomRemplacant    = valRH.getNomRemplacant()        != null ? valRH.getNomRemplacant() : "";
            debutCollab      = valRH.getDebutCollaboration()   != null ? valRH.getDebutCollaboration().format(FMT) : "";
            joursConsommes   = valRH.getNombreJoursConsommes() != null ? valRH.getNombreJoursConsommes().toString() : "";
            dernierConge     = valRH.getDateDerniersConges()   != null ? valRH.getDateDerniersConges().format(FMT) : "";
            joursDisponibles = valRH.getNombreJoursDisponibles() != null ? valRH.getNombreJoursDisponibles().toString() : "";
            observation      = valRH.getObservation()          != null ? valRH.getObservation() : "";
            cbConges         = valRH.isAbsenceDeduireConges();
            cbPaie           = valRH.isAbsenceDeduirePaie();
            cbReglem         = valRH.isDemandeReglementaire();
        }

        // ── Chargement template ──
        ClassPathResource res = new ClassPathResource("templates/papier_en_tete.pdf");
        if (!res.exists()) throw new RuntimeException("Template introuvable");

        InputStream          tplStream = res.getInputStream();
        ByteArrayOutputStream out      = new ByteArrayOutputStream();

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(tplStream), new PdfWriter(out));
        PdfPage     page   = pdfDoc.getFirstPage();
        Rectangle   size   = page.getPageSize();
        PdfCanvas   cv     = new PdfCanvas(page);

        PdfFont bold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

        try (Canvas c = new Canvas(cv, size)) {

            float y = Y_START;

            // ══════════════════════════════════════
            // 1. TITRE
            // ══════════════════════════════════════
            c.add(new Paragraph(
                    "DEMANDE      "
                    + cb(isAbsence) + " D'ABSENCE        "
                    + cb(isConge)   + " DE CONGES")
                    .setFont(bold).setFontSize(13)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(MARGIN, y - 14, CONTENT_W));

            y -= 20;
            c.add(new Paragraph(cb(false) + "  Régularisation")
                    .setFont(normal).setFontSize(9)
                    .setFixedPosition(MARGIN, y - 10, CONTENT_W));

            // Ligne séparatrice verte
            y -= 16;
            cv.setStrokeColor(VERT).setLineWidth(1f)
              .moveTo(MARGIN, y).lineTo(MARGIN + CONTENT_W, y).stroke();

            // ══════════════════════════════════════
            // 2. INFOS EMPLOYÉ
            // ══════════════════════════════════════
            y -= 16;
            c.add(line("M/Mlle : " + prenom + " " + nom
                    + "        Service : " + service
                    + "        Poste : " + poste,
                    normal, 10, y));

            y -= 16;
            c.add(line("Sollicite    "
                    + cb(isAbsence) + "  une autorisation d'absence          "
                    + cb(isConge)   + "  un congé",
                    normal, 10, y));

            y -= 16;
            c.add(line("a-)  Le :  ............................  de  ............  heures  à  ............ heures",
                    normal, 9, y));

            y -= 14;
            c.add(line("b-)  Du : " + debut + "          au : " + fin,
                    normal, 10, y));

            y -= 16;
            c.add(line("Motif : " + motif, normal, 10, y));

            y -= 16;
            c.add(line("Nom du remplaçant : M  " + (nomRemplacant.isEmpty() ? "............................................" : nomRemplacant),
                    normal, 10, y));

            // ══════════════════════════════════════
            // 3. CASES À COCHER
            // ══════════════════════════════════════
            y -= 20;

            // Tableau 3 colonnes : label | Oui | Non
            Table cases = new Table(UnitValue.createPercentArray(new float[]{6f, 1.2f, 1.2f}))
                    .setWidth(CONTENT_W);

            ligneCase(cases, "Absence à déduire du droit aux congés", cbConges, normal, 9);
            ligneCase(cases, "Absence à déduire de la paie",          cbPaie,   normal, 9);
            ligneCase(cases, "Demande réglementaire",                  cbReglem, normal, 9);

            c.add(cases.setFixedPosition(MARGIN, y - 50, CONTENT_W));
            y -= 56;

            // Date + signature
            c.add(line("Date de la demande : " + dateD
                    + "                    Signature de l'agent : ............................................",
                    normal, 9, y));

            // Ligne grise
            y -= 14;
            cv.setStrokeColor(ColorConstants.LIGHT_GRAY).setLineWidth(0.5f)
              .moveTo(MARGIN, y).lineTo(MARGIN + CONTENT_W, y).stroke();

            y -= 12;
            c.add(new Paragraph(
                    "Tout collaborateur qui s'absente doit fournir les pièces motivant "
                    + "sa demande à son dépôt ou dès la reprise de son poste.")
                    .setFont(italic).setFontSize(8)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setFixedPosition(MARGIN, y - 14, CONTENT_W));

            // ══════════════════════════════════════
            // 4. TABLE ACCORD / POINT CONGÉS
            // ══════════════════════════════════════
            y -= 30;
            float tableH = 220f; // hauteur totale table 4 lignes

            // Table 4 colonnes
            Table sigTable = new Table(
                    UnitValue.createPercentArray(new float[]{2.8f, 1.6f, 2.4f, 1.4f}))
                    .setWidth(CONTENT_W);

            // En-têtes
            sigTable.addHeaderCell(hCell("Accord/info direction",   bold, GRIS_CLAIR));
            sigTable.addHeaderCell(hCell("Date et signature",        bold, GRIS_CLAIR));
            sigTable.addHeaderCell(hCell("Point congés",             bold, GRIS_CLAIR));
            sigTable.addHeaderCell(hCell("",                         bold, GRIS_CLAIR));

            // Ligne 1 : Responsable
            ligneValidee(sigTable, "Responsable service / Manager", normal, bold, italic);
            lignePointConge(sigTable, "Début de collaboration :", debutCollab, normal);

            // Ligne 2 : RH
            ligneValidee(sigTable, "Service des Ressources Humaines", normal, bold, italic);
            lignePointConge(sigTable, "Nb de jours consommés :", joursConsommes, normal);

            // Ligne 3 : Directeur Technique
            ligneValidee(sigTable, "Directeur Technique\n" + cb(false) + "  Pour info congés",
                    normal, bold, italic);
            lignePointConge(sigTable, "Date des derniers congés :", dernierConge, normal);

            // Ligne 4 : Directrice Administrative
            ligneValidee(sigTable,
                    "Directrice Administrative et Financière\n" + cb(false) + "  Pour info congés",
                    normal, bold, italic);

            // Cellule point congés fusionnée (col 3+4)
            String signatureRH = valRH != null && valRH.getValidateur() != null
                    ? valRH.getValidateur().getPrenom() + " " + valRH.getValidateur().getNom()
                    : "";
            String dateValRH = valRH != null && valRH.getDateValidation() != null
                    ? valRH.getDateValidation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "";

            sigTable.addCell(new Cell(1, 2)
                    .add(new Paragraph(
                            "Nb de jours disponibles : " + joursDisponibles
                            + (!observation.isEmpty() ? "\nObservation : " + observation : "")
                            + "\n\nService des Ressources Humaines\n"
                            + (signatureRH.isEmpty() ? "(date, nom/prénom et signature)"
                               : dateValRH + " - " + signatureRH))
                            .setFont(normal).setFontSize(8))
                    .setMinHeight(52)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            c.add(sigTable.setFixedPosition(MARGIN, y - tableH, CONTENT_W));
            y -= (tableH + 14);

            // ══════════════════════════════════════
            // 5. CADRE ACCORD DG
            // ══════════════════════════════════════
            float dgH = 80f;
            float dgY = y - dgH;

            // Bordure verte
            cv.setStrokeColor(VERT).setLineWidth(1.5f)
              .rectangle(MARGIN, dgY, CONTENT_W, dgH).stroke();

            // Titre
            c.add(new Paragraph(
                    "Accord du Directeur Général          "
                    + cb(isAbsence) + "  Absence          "
                    + cb(isConge)   + "  Congés")
                    .setFont(bold).setFontSize(11)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(MARGIN, dgY + dgH - 18, CONTENT_W));

            // NB
            c.add(new Paragraph(
                    "NB : les demandes de congés sont obligatoirement validées "
                    + "par un titre de congés signé du DG.")
                    .setFont(italic).setFontSize(8.5f)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(MARGIN, dgY + dgH - 35, CONTENT_W));

            // Ligne dans cadre
            cv.setStrokeColor(VERT).setLineWidth(0.5f)
              .moveTo(MARGIN + 10, dgY + dgH - 40)
              .lineTo(MARGIN + CONTENT_W - 10, dgY + dgH - 40).stroke();

            // Nom DG
            c.add(new Paragraph("KOSSI Yaovi Dakato")
                    .setFont(bold).setFontSize(13)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(MARGIN, dgY + 20, CONTENT_W));

            c.add(new Paragraph("Directeur Général")
                    .setFont(italic).setFontSize(9)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(MARGIN, dgY + 8, CONTENT_W));
        }

        pdfDoc.close();
        return out.toByteArray();
    }

    // ══════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════

    /** Case cochée ☑ ou vide ☐ */
    private String cb(boolean checked) {
        return checked ? "☑" : "☐";
    }

    /** Paragraphe positionné sur une ligne */
    private Paragraph line(String text, PdfFont font, float size, float y) {
        return new Paragraph(text)
                .setFont(font).setFontSize(size)
                .setFixedPosition(MARGIN, y - 12, CONTENT_W);
    }

    /** Ligne dans le tableau des cases à cocher */
    private void ligneCase(Table table, String label, boolean checked, PdfFont font, int size) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setFont(font).setFontSize(size))
                .setBorder(Border.NO_BORDER).setPaddingBottom(2));
        table.addCell(new Cell()
                .add(new Paragraph((checked ? "☑" : "☐") + "  Oui")
                        .setFont(font).setFontSize(size)
                        .setFontColor(checked ? VERT : ColorConstants.BLACK))
                .setBorder(Border.NO_BORDER).setPaddingBottom(2));
        table.addCell(new Cell()
                .add(new Paragraph((checked ? "☐" : "☑") + "  Non")
                        .setFont(font).setFontSize(size))
                .setBorder(Border.NO_BORDER).setPaddingBottom(2));
    }

    /** Ligne validée (col 1+2 gauche de la table accord) */
    private void ligneValidee(Table table, String label,
            PdfFont normal, PdfFont bold, PdfFont italic) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setFont(normal).setFontSize(9))
                .setMinHeight(52).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell()
                .add(new Paragraph("✓  VALIDÉ")
                        .setFont(bold).setFontSize(11).setFontColor(VERT))
                .setBackgroundColor(VERT_CLAIR)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMinHeight(52));
    }

    /** Ligne point congés (col 3+4 droite de la table) */
    private void lignePointConge(Table table, String label, String valeur, PdfFont font) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setFont(font).setFontSize(9))
                .setMinHeight(52).setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell()
                .add(new Paragraph(valeur.isEmpty() ? "" : valeur)
                        .setFont(font).setFontSize(9))
                .setMinHeight(52).setVerticalAlignment(VerticalAlignment.MIDDLE));
    }

    /** Cellule d'en-tête */
    private Cell hCell(String text, PdfFont font, DeviceRgb bg) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(bg)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }
}