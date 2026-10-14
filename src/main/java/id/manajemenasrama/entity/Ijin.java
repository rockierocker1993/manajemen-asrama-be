package id.manajemenasrama.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "ijin"
)
@SQLRestriction("deleted is null")
public class Ijin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaSantri;

    private LocalDate tanggalIjin;

    private String tipeIjin;// IJIN / SAKIT

    private String alasan;

    private String status; // PENDING / APPROVED / REJECTED

}
