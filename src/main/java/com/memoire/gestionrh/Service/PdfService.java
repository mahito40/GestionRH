package com.memoire.gestionrh.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.memoire.gestionrh.Models.Demande;
import com.memoire.gestionrh.Repository.DemandeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfService {

    private final DemandeRepository demandeRepository;

    private static final DateTimeFormatter FMT      = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_LONG = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ── Couleurs ──
    private static final DeviceRgb VERT        = new DeviceRgb(34, 139, 34);
    private static final DeviceRgb VERT_CLAIR  = new DeviceRgb(220, 240, 220);
    private static final DeviceRgb GRIS_CLAIR  = new DeviceRgb(240, 240, 240);
    private static final DeviceRgb NOIR        = new DeviceRgb(0, 0, 0);

    public byte[] genererPdfDemande(UUID demandeId) throws Exception {

        Demande d = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée : " + demandeId));

        // ── Données dynamiques depuis la BDD ──
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

        // ── Chargement du PDF de base (en-tête + pied de page HOMINTEC) ──
        ClassPathResource resource = new ClassPathResource("templates/papier_en_tete.pdf");
        if (!resource.exists()) {
            throw new RuntimeException("Template PDF introuvable : templates/papier_en_tete.pdf");
        }

        log.info("Chargement du template PDF : {}", resource.getFilename());
        InputStream templateStream = resource.getInputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfDocument pdfDoc = new PdfDocument(
                new PdfReader(templateStream),
                new PdfWriter(out));

        PdfPage  page    = pdfDoc.getFirstPage();
        Rectangle size   = page.getPageSize();
        PdfCanvas canvas = new PdfCanvas(page);

        // ── Polices ──
        PdfFont bold   = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont italic = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

        float margin   = 42f;
        float contentW = size.getWidth() - 2 * margin;
        float yStart   = size.getHeight() - 115f; // juste sous la bannière ISO

        try (Canvas c = new Canvas(canvas, size)) {

            // ══════════════════════════════════════════
            // SECTION 1 — TITRE PRINCIPAL
            // ══════════════════════════════════════════
            c.add(new Paragraph("DEMANDE   "
                    + (isAbsence ? "☑" : "☐") + " D'ABSENCE       "
                    + (isConge   ? "☑" : "☐") + " DE CONGES")
                    .setFont(bold).setFontSize(12)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(margin, yStart, contentW));

            c.add(new Paragraph("☐  Régularisation")
                    .setFont(normal).setFontSize(9)
                    .setFixedPosition(margin, yStart - 18, contentW));

            // Ligne séparatrice
            canvas.setStrokeColor(VERT)
                    .setLineWidth(0.8f)
                    .moveTo(margin, yStart - 24)
                    .lineTo(margin + contentW, yStart - 24)
                    .stroke();

            // ══════════════════════════════════════════
            // SECTION 2 — INFOS EMPLOYÉ
            // ══════════════════════════════════════════
            float yInfo = yStart - 42;

            c.add(new Paragraph(
                    "M/Mlle : " + prenom + " " + nom
                    + "                    Service : " + service
                    + "                    Poste : " + poste)
                    .setFont(normal).setFontSize(10)
                    .setFixedPosition(margin, yInfo, contentW));

            c.add(new Paragraph(
                    "Sollicite     "
                    + (isAbsence ? "☑" : "☐") + "  une autorisation d'absence          "
                    + (isConge   ? "☑" : "☐") + "  un congé")
                    .setFont(normal).setFontSize(10)
                    .setFixedPosition(margin, yInfo - 18, contentW));

            c.add(new Paragraph(
                    "b-)  Du : " + debut + "          au : " + fin)
                    .setFont(normal).setFontSize(10)
                    .setFixedPosition(margin, yInfo - 36, contentW));

            c.add(new Paragraph("Motif : " + motif)
                    .setFont(normal).setFontSize(10)
                    .setFixedPosition(margin, yInfo - 54, contentW));

            c.add(new Paragraph(
                    "Nom du remplaçant : M ........................................................")
                    .setFont(normal).setFontSize(10)
                    .setFixedPosition(margin, yInfo - 72, contentW));

            // ══════════════════════════════════════════
            // SECTION 3 — CASES À COCHER
            // ══════════════════════════════════════════
            float yCases = yInfo - 95;

            Table cases = new Table(UnitValue.createPercentArray(new float[]{4.5f, 1f, 1f}))
                    .setWidth(contentW);
            cases.addCell(cellNoBorder("Absence à déduire du droit aux congés", normal, 9));
            cases.addCell(cellNoBorder("☐  Oui", normal, 9));
            cases.addCell(cellNoBorder("☐  Non", normal, 9));
            cases.addCell(cellNoBorder("Absence à déduire de la paie", normal, 9));
            cases.addCell(cellNoBorder("☐  Oui", normal, 9));
            cases.addCell(cellNoBorder("☐  Non", normal, 9));
            cases.addCell(cellNoBorder("Demande réglementaire", normal, 9));
            cases.addCell(cellNoBorder("☐  Oui", normal, 9));
            cases.addCell(cellNoBorder("☐  Non", normal, 9));
            c.add(cases.setFixedPosition(margin, yCases - 38, contentW));

            c.add(new Paragraph(
                    "Date de la demande : " + dateD
                    + "                    Signature de l'agent : ............................................")
                    .setFont(normal).setFontSize(9)
                    .setFixedPosition(margin, yCases - 60, contentW));

            // Ligne info
            canvas.setStrokeColor(ColorConstants.LIGHT_GRAY)
                    .setLineWidth(0.5f)
                    .moveTo(margin, yCases - 68)
                    .lineTo(margin + contentW, yCases - 68)
                    .stroke();

            c.add(new Paragraph(
                    "Tout collaborateur qui s'absente doit fournir les pièces motivant "
                    + "sa demande à son dépôt ou dès la reprise de son poste.")
                    .setFont(italic).setFontSize(8)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setFixedPosition(margin, yCases - 80, contentW));

            // ══════════════════════════════════════════
            // SECTION 4 — TABLE ACCORD / POINT CONGÉS
            // ══════════════════════════════════════════
            float yTable = yCases - 95;

            Table sigTable = new Table(
                    UnitValue.createPercentArray(new float[]{2.5f, 1.8f, 2f, 1.8f}))
                    .setWidth(contentW);

            // En-têtes
            sigTable.addHeaderCell(enteteCell("Accord / info direction", bold, GRIS_CLAIR));
            sigTable.addHeaderCell(enteteCell("Date et validation", bold, GRIS_CLAIR));
            sigTable.addHeaderCell(enteteCell("Point congés", bold, GRIS_CLAIR));
            sigTable.addHeaderCell(enteteCell("", bold, GRIS_CLAIR));

            // ── Ligne 1 : Responsable service ──
            ajouterLigneValidee(sigTable, "Responsable service / Manager", normal, bold);
            ajouterLignePointConge(sigTable, "Début de collaboration :", normal);

            // ── Ligne 2 : RH ──
            ajouterLigneValidee(sigTable, "Service des Ressources Humaines", normal, bold);
            ajouterLignePointConge(sigTable, "Nb de jours consommés :", normal);

            // ── Ligne 3 : Directeur Technique ──
            ajouterLigneValidee(sigTable,
                    "Directeur Technique\n☐  Pour info congés", normal, bold);
            ajouterLignePointConge(sigTable, "Date des derniers congés :", normal);

            // ── Ligne 4 : Directrice Administrative ──
            ajouterLigneValidee(sigTable,
                    "Directrice Administrative et Financière\n☐  Pour info congés",
                    normal, bold);

            // Cellule point congés fusionnée
            sigTable.addCell(new Cell(1, 2)
                    .add(new Paragraph(
                            "Nb de jours disponibles :\n\n\n"
                            + "Service des Ressources Humaines\n"
                            + "(date, nom/prénom et signature)")
                            .setFont(normal).setFontSize(8))
                    .setMinHeight(55));

            c.add(sigTable.setFixedPosition(margin, yTable - 200, contentW));

            // ══════════════════════════════════════════
            // SECTION 5 — CADRE ACCORD DIRECTEUR GÉNÉRAL
            // ══════════════════════════════════════════
            float yDG = yTable - 230;

            // Bordure du cadre DG
            canvas.setStrokeColor(VERT)
                    .setLineWidth(1.5f)
                    .rectangle(margin, yDG - 75, contentW, 82)
                    .stroke();

            // Titre accord DG
            c.add(new Paragraph(
                    "Accord du Directeur Général          "
                    + (isAbsence ? "☑" : "☐") + "  Absence          "
                    + (isConge   ? "☑" : "☐") + "  Congés")
                    .setFont(bold).setFontSize(11)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(margin, yDG, contentW));

            // NB
            c.add(new Paragraph(
                    "NB : les demandes de congés sont obligatoirement validées "
                    + "par un titre de congés signé du DG.")
                    .setFont(italic).setFontSize(9)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(margin, yDG - 20, contentW));

            // Ligne séparatrice dans le cadre
            canvas.setStrokeColor(VERT)
                    .setLineWidth(0.5f)
                    .moveTo(margin + 10, yDG - 32)
                    .lineTo(margin + contentW - 10, yDG - 32)
                    .stroke();

            // Nom du DG
            c.add(new Paragraph("KOSSI Yaovi Dakato")
                    .setFont(bold).setFontSize(12)
                    .setFontColor(VERT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(margin, yDG - 58, contentW));

            // Sous-titre DG
            c.add(new Paragraph("Directeur Général")
                    .setFont(italic).setFontSize(9)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedPosition(margin, yDG - 70, contentW));
        }

        pdfDoc.close();
        return out.toByteArray();
    }

    // ══════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════

    /**
     * Ligne validée avec ✓ vert — s'affiche car le processus est entièrement approuvé
     */
    private void ajouterLigneValidee(Table table, String label,
            PdfFont normal, PdfFont bold) {

        // Colonne label
        table.addCell(new Cell()
                .add(new Paragraph(label)
                        .setFont(normal).setFontSize(9))
                .setMinHeight(50)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        // Colonne ✓ VALIDÉ sur fond vert clair
        table.addCell(new Cell()
                .add(new Paragraph("✓  VALIDÉ")
                        .setFont(bold).setFontSize(11)
                        .setFontColor(VERT))
                .setBackgroundColor(VERT_CLAIR)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMinHeight(50));
    }

    /**
     * Ligne point congés (colonne droite de la table)
     */
    private void ajouterLignePointConge(Table table, String label, PdfFont font) {
        table.addCell(new Cell()
                .add(new Paragraph(label).setFont(font).setFontSize(9))
                .setMinHeight(50)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        table.addCell(new Cell()
                .add(new Paragraph("..............").setFont(font).setFontSize(9))
                .setMinHeight(50)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));
    }

    /**
     * Cellule d'en-tête avec fond coloré
     */
    private Cell enteteCell(String text, PdfFont font, DeviceRgb bg) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(font).setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(bg)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    /**
     * Cellule sans bordure pour les checkboxes
     */
    private Cell cellNoBorder(String text, PdfFont font, int fontSize) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(fontSize))
                .setBorder(Border.NO_BORDER);
    }
}