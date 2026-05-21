package com.memoire.gestionrh.DTO;

import java.util.UUID;

public class JustificatifDTO {

    private UUID id;

    private String path;

    private String description;

    private UUID demandeId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getDemandeId() {
        return demandeId;
    }

    public void setDemandeId(UUID demandeId) {
        this.demandeId = demandeId;
    }
}