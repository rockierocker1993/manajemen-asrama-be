package id.manajemenasrama.repository;

import id.manajemenasrama.entity.Ijin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IjinRepository extends JpaRepository<Ijin, Long>, JpaSpecificationExecutor<Ijin> {
}

