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
    name = "role", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_role_name_deleted",
                columnNames = {"name", "deleted"}
        )
}
)
@SQLRestriction("deleted is null")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

}
