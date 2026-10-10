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
        name = "asrama", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_asrama_nama_deleted",
                columnNames = {"nama", "deleted"}
        )
}
)
@SQLRestriction("deleted is null")
public class Asrama extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40)
    private String nama;

}
