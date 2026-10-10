package id.manajemenasrama.repository;

import id.manajemenasrama.dto.kegiatan.KegiatanListResponseDto;
import id.manajemenasrama.entity.Kategori;
import id.manajemenasrama.entity.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface KegiatanRepository extends JpaRepository<Kegiatan, Long> {

    Optional<Kegiatan> findByNamaAndKategori(String nama, Kategori kategori);

    @Query("""
            select new id.manajemenasrama.dto.kegiatan.KegiatanListResponseDto(
                k.id,
                k.nama,
                k.deskripsi,
                kat.nama as kategori
            )
            from Kegiatan k
            left join k.kategori kat
            order by k.created asc
            """)
    List<KegiatanListResponseDto> findAllKegiatan();


}

