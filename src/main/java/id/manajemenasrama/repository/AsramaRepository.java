package id.manajemenasrama.repository;

import id.manajemenasrama.entity.Asrama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsramaRepository extends JpaRepository<Asrama, Long> {
	Optional<Asrama> findByNamaIgnoreCase(String nama);

	boolean existsByNamaIgnoreCaseAndIdIsNot(String nama, Long id);
}

