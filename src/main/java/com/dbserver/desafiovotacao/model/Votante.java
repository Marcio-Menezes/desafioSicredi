
package com.dbserver.desafiovotacao.model;

import com.dbserver.desafiovotacao.enums.VotoEnum;
import java.util.UUID;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "votante")
public class Votante implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;
    @JoinColumn(name = "voto")
    @Enumerated(EnumType.STRING)
    private VotoEnum voto;
}
