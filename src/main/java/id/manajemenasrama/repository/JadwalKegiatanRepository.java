package id.manajemenasrama.repository;

import id.manajemenasrama.dto.kegiatan.JadwalKegiatanListByDayResponseDto;
import id.manajemenasrama.dto.kegiatan.JadwalKegiatanListResponseDto;
import id.manajemenasrama.entity.JadwalKegiatan;
import id.manajemenasrama.entity.Kegiatan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JadwalKegiatanRepository extends JpaRepository<JadwalKegiatan, Long> {
    boolean existsByKegiatanAndIdIsNot(Kegiatan kegiatan, Long id);
//
//    @Query("""
//            select new id.manajemenasrama.dto.JadwalKegiatanListResponseDto(
//                jk.id,
//                keg.nama,
//                kat.nama,
//                jk.hari,
//                jk.jamAwal,
//                jk.jamAkhir,
//                jk.tanggalAwalEfektif,
//                jk.tanggalAkhirEfektif,
//                jk.status,
//                usr.fullName
//            )
//            from JadwalKegiatan jk
//            left join jk.kegiatan keg
//            left join keg.kategori kat
//            left join jk.pelaksana usr
//            where (:status is null or jk.status = :status)
//            order by jk.hari, jk.jamAwal
//            """)
//    List<JadwalKegiatanListResponseDto> findAllListByStatus(Boolean status);

    @Query(value = """
            SELECT
                jk.id AS id,
                k.nama AS nama,
                kat.nama AS kategori,
                jk.hari AS hari,
            
                TO_CHAR(jk.jam_awal, 'HH24:MI') AS jamAwal,
                TO_CHAR(jk.jam_akhir, 'HH24:MI') AS jamAkhir,
            
                TO_CHAR(jk.tanggal_awal_efektif, 'DD-MM-YYYY') AS tanggalAwalEfektif,
                TO_CHAR(jk.tanggal_akhir_efektif, 'DD-MM-YYYY') AS tanggalAkhirEfektif,
            
                jk.status AS status,
            
                STRING_AGG(
                    DISTINCT u.full_name,
                    ', '
                    ORDER BY u.full_name
                ) AS jadwalKegiatanUsers,
            
                STRING_AGG(
                    DISTINCT a.nama,
                    ', '
                    ORDER BY a.nama
                ) AS jadwalKegiatanAsrama
            
            FROM jadwal_kegiatan jk
            LEFT JOIN kegiatan k ON k.id = jk.kegiatan_id
            LEFT JOIN kategori kat ON kat.id = k.kategori_id
            LEFT JOIN jadwal_kegiatan_user jku ON jku.jadwal_kegiatan_id = jk.id
            LEFT JOIN users u ON u.id = jku.user_id
            LEFT JOIN jadwal_kegiatan_asrama jka ON jka.jadwal_kegiatan_id = jk.id
            LEFT JOIN asrama a ON a.id = jka.asrama_id
            WHERE (:status IS NULL OR jk.status = :status) and jk.deleted is null
            GROUP BY
                jk.id,
                k.nama,
                kat.nama,
                jk.hari,
                jk.jam_awal,
                jk.jam_akhir,
                jk.tanggal_awal_efektif,
                jk.tanggal_akhir_efektif,
                jk.status
            ORDER BY
                jk.hari,
                jk.jam_awal
            """, nativeQuery = true)
    List<JadwalKegiatanListResponseDto> findAllListByStatus(@Param("status") Boolean status);

    @Query(value = """
                    SELECT
                           jk.id AS id,
                           k.nama AS nama,
                           kat.nama AS kategori,
                           jk.hari AS hari,

                           TO_CHAR(jk.jam_awal, 'HH24:MI') AS jamAwal,
                           TO_CHAR(jk.jam_akhir, 'HH24:MI') AS jamAkhir,

                           TO_CHAR(jk.tanggal_awal_efektif, 'DD-MM-YYYY') AS tanggalAwalEfektif,
                           TO_CHAR(jk.tanggal_akhir_efektif, 'DD-MM-YYYY') AS tanggalAkhirEfektif,

                           jk.status AS status,

                           STRING_AGG(
                               DISTINCT u.full_name,
                               ', '
                               ORDER BY u.full_name
                           ) AS jadwalKegiatanUsers,

                           STRING_AGG(
                               DISTINCT a.nama,
                               ', '
                               ORDER BY a.nama
                           ) AS jadwalKegiatanAsrama

                       FROM jadwal_kegiatan jk
                       LEFT JOIN kegiatan k ON k.id = jk.kegiatan_id
                       LEFT JOIN kategori kat ON kat.id = k.kategori_id
                       LEFT JOIN jadwal_kegiatan_user jku ON jku.jadwal_kegiatan_id = jk.id
                       LEFT JOIN users u ON u.id = jku.user_id
                       LEFT JOIN jadwal_kegiatan_asrama jka ON jka.jadwal_kegiatan_id = jk.id
                       LEFT JOIN asrama a ON a.id = jka.asrama_id
                       WHERE
                       jk.status = true
                       and jk.deleted is null
                       and jk.tanggal_awal_efektif <= now()
                       and jk.tanggal_akhir_efektif > now()
                       and ((u.id is null and jk.tipe_kegiatan = 'UMUM') or u.username = :username)
                       and jk.hari like CONCAT('%', :hari, '%')
                       GROUP BY
                           jk.id,
                           k.nama,
                           kat.nama,
                           jk.hari,
                           jk.jam_awal,
                           jk.jam_akhir,
                           jk.tanggal_awal_efektif,
                           jk.tanggal_akhir_efektif,
                           jk.status
                       ORDER BY
                           jk.hari,
                           jk.jam_awal
            """, nativeQuery = true)
    List<JadwalKegiatanListResponseDto> findActiveTodayByUsernameAndHari(@Param("username") String username, String hari);

    @Query(value = """
                    SELECT
                           jk.id AS id,
                           k.nama AS nama,
                           kat.nama AS kategori,
                           jk.hari AS hari,

                           TO_CHAR(jk.jam_awal, 'HH24:MI') AS jamAwal,
                           TO_CHAR(jk.jam_akhir, 'HH24:MI') AS jamAkhir,

                           TO_CHAR(jk.tanggal_awal_efektif, 'DD-MM-YYYY') AS tanggalAwalEfektif,
                           TO_CHAR(jk.tanggal_akhir_efektif, 'DD-MM-YYYY') AS tanggalAkhirEfektif,

                           k.deskripsi AS deskripsi,
                           jk.tipe_kegiatan AS tipeKegiatan,

                           STRING_AGG(
                               DISTINCT u.full_name,
                               ', '
                               ORDER BY u.full_name
                           ) AS jadwalKegiatanUsers,

                           STRING_AGG(
                               DISTINCT a.nama,
                               ', '
                               ORDER BY a.nama
                           ) AS jadwalKegiatanAsrama

                       FROM jadwal_kegiatan jk
                       LEFT JOIN kegiatan k ON k.id = jk.kegiatan_id
                       LEFT JOIN kategori kat ON kat.id = k.kategori_id
                       LEFT JOIN jadwal_kegiatan_user jku ON jku.jadwal_kegiatan_id = jk.id
                       LEFT JOIN users u ON u.id = jku.user_id
                       LEFT JOIN jadwal_kegiatan_asrama jka ON jka.jadwal_kegiatan_id = jk.id
                       LEFT JOIN asrama a ON a.id = jka.asrama_id
                       WHERE
                       jk.status = true
                       and jk.deleted is null
                       and jk.tanggal_awal_efektif <= now()
                       and jk.tanggal_akhir_efektif > now()
                       and ((u.id is null and jk.tipe_kegiatan = 'UMUM') or u.username = :username)
                       GROUP BY
                           jk.id,
                           k.nama,
                           kat.nama,
                           jk.hari,
                           jk.jam_awal,
                           jk.jam_akhir,
                           jk.tanggal_awal_efektif,
                           jk.tanggal_akhir_efektif,
                           jk.status,
                           k.deskripsi,
                           jk.tipe_kegiatan
                       ORDER BY
                           jk.hari,
                           jk.jam_awal
            """, nativeQuery = true)
    List<JadwalKegiatanListByDayResponseDto> findActiveTodayByUsername(@Param("username") String username);

    @Query(value = """
                    SELECT
                           jk.id AS id,
                           k.nama AS nama,
                           kat.nama AS kategori,
                           jk.hari AS hari,

                           TO_CHAR(jk.jam_awal, 'HH24:MI') AS jamAwal,
                           TO_CHAR(jk.jam_akhir, 'HH24:MI') AS jamAkhir,

                           TO_CHAR(jk.tanggal_awal_efektif, 'DD-MM-YYYY') AS tanggalAwalEfektif,
                           TO_CHAR(jk.tanggal_akhir_efektif, 'DD-MM-YYYY') AS tanggalAkhirEfektif,

                           k.deskripsi AS deskripsi,
                           jk.tipe_kegiatan AS tipeKegiatan,

                           STRING_AGG(
                               DISTINCT u.full_name,
                               ', '
                               ORDER BY u.full_name
                           ) AS jadwalKegiatanUsers,

                           STRING_AGG(
                               DISTINCT a.nama,
                               ', '
                               ORDER BY a.nama
                           ) AS jadwalKegiatanAsrama

                       FROM jadwal_kegiatan jk
                       LEFT JOIN kegiatan k ON k.id = jk.kegiatan_id
                       LEFT JOIN kategori kat ON kat.id = k.kategori_id
                       LEFT JOIN jadwal_kegiatan_user jku ON jku.jadwal_kegiatan_id = jk.id
                       LEFT JOIN users u ON u.id = jku.user_id
                       LEFT JOIN jadwal_kegiatan_asrama jka ON jka.jadwal_kegiatan_id = jk.id
                       LEFT JOIN asrama a ON a.id = jka.asrama_id
                       WHERE
                       jk.status = true
                       and jk.deleted is null
                       and jk.tanggal_awal_efektif <= now()
                       and jk.tanggal_akhir_efektif > now()
                       GROUP BY
                           jk.id,
                           k.nama,
                           kat.nama,
                           jk.hari,
                           jk.jam_awal,
                           jk.jam_akhir,
                           jk.tanggal_awal_efektif,
                           jk.tanggal_akhir_efektif,
                           jk.status,
                           k.deskripsi,
                           jk.tipe_kegiatan
                       ORDER BY
                           jk.hari,
                           jk.jam_awal
            """, nativeQuery = true)
    List<JadwalKegiatanListByDayResponseDto> findActiveToday();
}

