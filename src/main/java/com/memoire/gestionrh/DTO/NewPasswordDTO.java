package com.memoire.gestionrh.DTO;

import lombok.Data;

@Data
public class NewPasswordDTO {
    private String email;
    private String ancienMotDePasse;
    private String nouveauMotDePasse;
}
