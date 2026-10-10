package id.manajemenasrama.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "kegiatan"
)
@SQLRestriction("deleted is null")
public class Kegiatan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String nama;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @ManyToOne(fetch = FetchType.LAZY)
    private Kategori kategori;//IBADAH, PENDIDIKAN, OLAH RAGA

}
