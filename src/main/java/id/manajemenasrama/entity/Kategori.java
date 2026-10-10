package id.manajemenasrama.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "kategori", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_kategori_nama_deleted",
                columnNames = {"nama", "deleted"}
        )
}
)
@SQLRestriction("deleted is null")
public class Kategori extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String nama;

}
