package id.manajemenasrama.repository;

import id.manajemenasrama.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    Optional<Kategori> findByNama(String nama);

}

