package utp.bambinochicken.bambinochicken.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "AUTH_SESSION")
public class AuthSessionJpaEntity {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CORREO", nullable = false, length = 120)
    private String correo;

    @Column(name = "ROL", nullable = false, length = 60)
    private String rol;

    @Column(name = "ACCESS_TOKEN", nullable = false, unique = true, length = 255)
    private String accessToken;

    @Column(name = "REFRESH_TOKEN", nullable = false, unique = true, length = 255)
    private String refreshToken;

    @Column(name = "ISSUED_AT", nullable = false)
    private Instant issuedAt;

    @Column(name = "ACCESS_EXPIRES_AT", nullable = false)
    private Instant accessExpiresAt;

    @Column(name = "REFRESH_EXPIRES_AT", nullable = false)
    private Instant refreshExpiresAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getAccessExpiresAt() {
        return accessExpiresAt;
    }

    public void setAccessExpiresAt(Instant accessExpiresAt) {
        this.accessExpiresAt = accessExpiresAt;
    }

    public Instant getRefreshExpiresAt() {
        return refreshExpiresAt;
    }

    public void setRefreshExpiresAt(Instant refreshExpiresAt) {
        this.refreshExpiresAt = refreshExpiresAt;
    }
}
