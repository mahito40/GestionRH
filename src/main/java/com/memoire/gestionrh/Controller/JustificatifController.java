package com.memoire.gestionrh.Controller;

import com.memoire.gestionrh.DTO.JustificatifDTO;
import com.memoire.gestionrh.Service.JustificatifService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/justificatifs")
public class JustificatifController {

    private final JustificatifService justificatifService;

    public JustificatifController(JustificatifService justificatifService) {
        this.justificatifService = justificatifService;
    }

    @PostMapping
    public JustificatifDTO ajouter(@RequestBody JustificatifDTO dto) {
        return justificatifService.ajouter(dto);
    }

    @GetMapping
    public List<JustificatifDTO> afficherTous() {
        return justificatifService.afficherTous();
    }

    @GetMapping("/{id}")
    public JustificatifDTO afficherParId(@PathVariable UUID id) {
        return justificatifService.afficherParId(id);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable UUID id) {
        justificatifService.supprimer(id);
    }
}
