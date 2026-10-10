package id.manajemenasrama.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "jadwal_kegiatan"
)
@SQLRestriction("deleted is null")
public class JadwalKegiatan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String hari;//1 = SENIN, 2 = SELASA, 3 = RABU, 4 = KAMIS, 5 = JUMAT, 6 = SABTU, 7 = MINGGU

    @Column(nullable = false)
    private LocalTime jamAwal;

    @Column(nullable = false)
    private LocalTime jamAkhir;

    private LocalDate tanggalAwalEfektif;

    private LocalDate tanggalAkhirEfektif;

    private String tipeKegiatan; // UMUM / ASRAMA

    private boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Kegiatan kegiatan;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "jadwal_kegiatan_user",
            joinColumns = {
                    @JoinColumn(name = "jadwal_kegiatan_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            }
    )
    private List<User> jadwalKegiatanUsers = new ArrayList<>();


    @ManyToMany(targetEntity = Asrama.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "jadwal_kegiatan_asrama",
            joinColumns = {
                    @JoinColumn(name = "jadwal_kegiatan_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "asrama_id")
            }
    )
    private List<Asrama> jadwalKegiatanAsrama = new ArrayList<>();

}
