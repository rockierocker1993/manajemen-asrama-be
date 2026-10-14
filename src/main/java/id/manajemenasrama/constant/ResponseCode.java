package id.manajemenasrama.constant;

public enum ResponseCode {
    SUCCESS(
            "RC000",
            true,
            "Sukses",
            "Success",
            "Sukses",
            "Success"
    ),
    UKNOWN_ERROR(
            "RC999",
            false,
            "Terjadi kesalahan yang tidak diketahui.",
            "Uknown error occurred.",
            "Error",
            "Error"
    ),
    USERNAME_ALREADY_REGISTERED(
            "RC001",
            false,
            "Username sudah digunakan.",
            "Username already exist.",
            "Error",
            "Error"
    ),
    ROLE_NOT_FOUND(
            "RC002",
            false,
            "Role tidak ditemukan.",
            "Role not found.",
            "Error",
            "Error"
    ),
    INVALID_USERNAME_PASSWORD(
            "RC003",
            false,
            "Username atau password tidak sesuai.",
            "Username or password doesn't match.",
            "Error",
            "Error"
    ),
    USER_NOT_FOUND(
            "RC004",
            false,
            "Pengguna tidak ditemukan.",
            "User not found.",
            "Error",
            "Error"
    ),
    UPDATE_SUCCESS(
            "RC005",
            true,
            "Data berhasil diperbaharui",
            "Data has been updated",
            "Sukses",
            "Success"
    ),
    CREATE_SUCCESS(
            "RC006",
            true,
            "Data berhasil disimpan",
            "Data has been save",
            "Sukses",
            "Success"
    ),
    DELETE_SUCCESS(
            "RC007",
            true,
            "Data berhasil dihapus",
            "Data has been deleted",
            "Sukses",
            "Success"
    ),
    USER_ENABLE_SUCCESS(
            "RC008",
            true,
            "Pengguna berhasil diaktifkan",
            "User has been enabled",
            "Sukses",
            "Success"
    ),
    USER_DISABLE_SUCCESS(
            "RC009",
            true,
            "Pengguna berhasil dinonaktifkan",
            "User has been disabled",
            "Sukses",
            "Success"
    ),
    JADWAL_KEGIATAN_NOT_FOUND(
            "RC010",
            false,
            "Jadwal kegiatan tidak ditemukan.",
            "Jadwal kegiatan not found.",
            "Error",
            "Error"
    ),
    ASRAMA_NOT_FOUND(
            "RC011",
            false,
            "Asrama tidak ditemukan.",
            "Asrama not found.",
            "Error",
            "Error"
    ),
    ASRAMA_ALREADY_EXIST(
            "RC012",
            false,
            "Nama asrama sudah digunakan.",
            "Asrama name already exist.",
            "Error",
            "Error"
    ),
    IJIN_NOT_FOUND(
            "RC013",
            false,
            "Ijin tidak ditemukan.",
            "Ijin not found.",
            "Error",
            "Error"
    ),
    IJIN_APPROVED(
            "RC014",
            true,
            "Ijin disetujui.",
            "Ijin approved.",
            "Info",
            "Info"
    ),
    IJIN_REJECTED(
            "RC014",
            true,
            "Ijin tidak disetujui.",
            "Ijin rejected.",
            "Info",
            "Info"
    ),
    UNAUTHORIZED(
            "RC998",
            false,
            "Unathorized.",
            "Unathorized.",
            "Error",
            "Error"
    ),
    ;

    private ResponseCode(String code, boolean status, String defaultMessageId, String defaultMessageEn, String defaultTitleId, String defaultTitleEn) {
        this.code = code;
        this.status = status;
        this.messageId = defaultMessageId;
        this.messageEn = defaultMessageEn;
        this.titleId = defaultTitleId;
        this.titleEn = defaultTitleEn;
    }

    private String code;
    private boolean status;
    private String messageId;
    private String messageEn;
    private String titleId;
    private String titleEn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public void setMessageEn(String messageEn) {
        this.messageEn = messageEn;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public static ResponseCode findByCode(String code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return null;
    }
}
