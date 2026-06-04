package com.memoire.gestionrh.DTO;

import lombok.Data;
import java.util.UUID;

@Data
public class ConversationRequest {
    private UUID expediteurId;
    private UUID destinataireId;
}
