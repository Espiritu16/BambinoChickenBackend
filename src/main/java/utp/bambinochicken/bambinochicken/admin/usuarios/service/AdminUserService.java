package utp.bambinochicken.bambinochicken.admin.usuarios.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminRoleResponse;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminUserResponse;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminUserUpsertRequest;
import utp.bambinochicken.bambinochicken.auth.store.AuthUserStore;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AdminUserService {

    private static final String FALLBACK_AUDIT_USER = "SISTEMA";

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserStore authUserStore;

    public AdminUserService(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate,
            PasswordEncoder passwordEncoder,
            AuthUserStore authUserStore
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.authUserStore = authUserStore;
    }

    public List<AdminUserResponse> searchUsers(String query) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (normalizedQuery.isBlank()) {
            String sqlAll = """
                    SELECT u.ID_USUARIO, u.NOMBRES, u.APELLIDOS, u.CORREO, u.ESTADO, u.ID_ROL,
                           r.NOMBRE AS ROL_NOMBRE, u.CREADO_EN, u.ACTUALIZADO_EN
                    FROM USUARIO u
                    JOIN ROL r ON r.ID_ROL = u.ID_ROL
                    ORDER BY u.ID_USUARIO DESC
                    """;
            return oracleJdbcTemplate.query(sqlAll, userRowMapper());
        }

        String like = "%" + normalizedQuery + "%";
        String sql = """
                SELECT u.ID_USUARIO, u.NOMBRES, u.APELLIDOS, u.CORREO, u.ESTADO, u.ID_ROL,
                       r.NOMBRE AS ROL_NOMBRE, u.CREADO_EN, u.ACTUALIZADO_EN
                FROM USUARIO u
                JOIN ROL r ON r.ID_ROL = u.ID_ROL
                WHERE LOWER(u.NOMBRES) LIKE ?
                   OR LOWER(NVL(u.APELLIDOS, '')) LIKE ?
                   OR LOWER(u.CORREO) LIKE ?
                ORDER BY u.ID_USUARIO DESC
                """;

        return oracleJdbcTemplate.query(sql, userRowMapper(), like, like, like);
    }

    public List<AdminRoleResponse> listRoles() {
        return oracleJdbcTemplate.query(
                "SELECT ID_ROL, NOMBRE FROM ROL ORDER BY ID_ROL",
                (rs, rowNum) -> new AdminRoleResponse(rs.getLong("ID_ROL"), rs.getString("NOMBRE"))
        );
    }

    public AdminUserResponse createUser(AdminUserUpsertRequest request) {
        String correo = normalizeEmail(request.correo());
        String rawPassword = request.contrasena() == null ? "" : request.contrasena().trim();
        if (rawPassword.length() < 6) {
            throw DomainException.validation("Contrasena debe tener al menos 6 caracteres");
        }
        validateRole(request.idRol());
        validateEmailNotUsed(correo, null);

        long nextId = nextUserId();
        Timestamp now = Timestamp.from(OffsetDateTime.now().toInstant());
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String auditActor = resolveAuditActor();

        try {
            insertUser(mysqlJdbcTemplate, nextId, request, correo, encodedPassword, now, auditActor);
            insertUser(oracleJdbcTemplate, nextId, request, correo, encodedPassword, now, auditActor);
        } catch (RuntimeException ex) {
            mysqlJdbcTemplate.update("DELETE FROM USUARIO WHERE ID_USUARIO = ?", nextId);
            throw ex;
        }

        return findUserById(nextId)
                .orElseThrow(() -> DomainException.badRequest("No se pudo crear el usuario"));
    }

    public AdminUserResponse updateUser(Long idUsuario, AdminUserUpsertRequest request) {
        AdminUserRow previous = findUserRow(idUsuario)
                .orElseThrow(() -> DomainException.badRequest("Usuario no existe"));

        String correo = normalizeEmail(request.correo());
        validateRole(request.idRol());
        validateEmailNotUsed(correo, idUsuario);

        Timestamp now = Timestamp.from(OffsetDateTime.now().toInstant());
        String auditActor = resolveAuditActor();
        String passwordToUse = request.contrasena() == null || request.contrasena().isBlank()
                ? previous.contrasena
                : passwordEncoder.encode(request.contrasena());

        try {
            updateUser(mysqlJdbcTemplate, idUsuario, request, correo, passwordToUse, now, auditActor);
            updateUser(oracleJdbcTemplate, idUsuario, request, correo, passwordToUse, now, auditActor);
        } catch (RuntimeException ex) {
            // best-effort rollback on mysql to previous state
            rollbackUser(mysqlJdbcTemplate, previous);
            throw ex;
        }

        return findUserById(idUsuario)
                .orElseThrow(() -> DomainException.badRequest("No se pudo actualizar el usuario"));
    }

    public AdminUserResponse inactivateUser(Long idUsuario) {
        AdminUserRow previous = findUserRow(idUsuario)
                .orElseThrow(() -> DomainException.badRequest("Usuario no existe"));

        Timestamp now = Timestamp.from(OffsetDateTime.now().toInstant());
        String auditActor = resolveAuditActor();

        try {
            inactivateUser(mysqlJdbcTemplate, idUsuario, now, auditActor);
            inactivateUser(oracleJdbcTemplate, idUsuario, now, auditActor);
        } catch (RuntimeException ex) {
            rollbackUser(mysqlJdbcTemplate, previous);
            throw ex;
        }

        return findUserById(idUsuario)
                .orElseThrow(() -> DomainException.badRequest("No se pudo inactivar el usuario"));
    }

    private void validateRole(Long idRol) {
        Integer exists = oracleJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ROL WHERE ID_ROL = ?",
                Integer.class,
                idRol
        );
        if (exists == null || exists == 0) {
            throw DomainException.validation("Rol no valido");
        }
    }

    private void validateEmailNotUsed(String correo, Long excludeId) {
        Integer oracleCount;
        Integer mysqlCount;

        if (excludeId == null) {
            oracleCount = oracleJdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM USUARIO WHERE LOWER(CORREO) = ?",
                    Integer.class,
                    correo
            );
            mysqlCount = mysqlJdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM USUARIO WHERE LOWER(CORREO) = ?",
                    Integer.class,
                    correo
            );
        } else {
            oracleCount = oracleJdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM USUARIO WHERE LOWER(CORREO) = ? AND ID_USUARIO <> ?",
                    Integer.class,
                    correo,
                    excludeId
            );
            mysqlCount = mysqlJdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM USUARIO WHERE LOWER(CORREO) = ? AND ID_USUARIO <> ?",
                    Integer.class,
                    correo,
                    excludeId
            );
        }

        if ((oracleCount != null && oracleCount > 0) || (mysqlCount != null && mysqlCount > 0)) {
            throw DomainException.conflict("El correo ya esta registrado");
        }
    }

    private long nextUserId() {
        Long maxOracle = oracleJdbcTemplate.queryForObject("SELECT NVL(MAX(ID_USUARIO), 0) FROM USUARIO", Long.class);
        Long maxMysql = mysqlJdbcTemplate.queryForObject("SELECT IFNULL(MAX(ID_USUARIO), 0) FROM USUARIO", Long.class);

        long oracle = maxOracle == null ? 0L : maxOracle;
        long mysql = maxMysql == null ? 0L : maxMysql;

        return Math.max(oracle, mysql) + 1;
    }

    private void insertUser(
            JdbcTemplate jdbcTemplate,
            long idUsuario,
            AdminUserUpsertRequest request,
            String correo,
            String password,
            Timestamp now,
            String auditActor
    ) {
        jdbcTemplate.update(
                """
                        INSERT INTO USUARIO (
                            ID_USUARIO, NOMBRES, APELLIDOS, CORREO, CONTRASENA, ESTADO, ID_ROL,
                            CREADO_EN, ACTUALIZADO_EN, CREADO_POR, ACTUALIZADO_POR
                        ) VALUES (?, ?, ?, ?, ?, 1, ?, ?, ?, ?, ?)
                        """,
                idUsuario,
                request.nombres().trim(),
                nullableTrim(request.apellidos()),
                correo,
                password,
                request.idRol(),
                now,
                now,
                auditActor,
                auditActor
        );
    }

    private void updateUser(
            JdbcTemplate jdbcTemplate,
            long idUsuario,
            AdminUserUpsertRequest request,
            String correo,
            String password,
            Timestamp now,
            String auditActor
    ) {
        jdbcTemplate.update(
                """
                        UPDATE USUARIO
                           SET NOMBRES = ?,
                               APELLIDOS = ?,
                               CORREO = ?,
                               CONTRASENA = ?,
                               ID_ROL = ?,
                               ACTUALIZADO_EN = ?,
                               ACTUALIZADO_POR = ?
                         WHERE ID_USUARIO = ?
                        """,
                request.nombres().trim(),
                nullableTrim(request.apellidos()),
                correo,
                password,
                request.idRol(),
                now,
                auditActor,
                idUsuario
        );
    }

    private void inactivateUser(JdbcTemplate jdbcTemplate, long idUsuario, Timestamp now, String auditActor) {
        jdbcTemplate.update(
                """
                        UPDATE USUARIO
                           SET ESTADO = 0,
                               ACTUALIZADO_EN = ?,
                               ACTUALIZADO_POR = ?
                         WHERE ID_USUARIO = ?
                        """,
                now,
                auditActor,
                idUsuario
        );
    }

    private Optional<AdminUserResponse> findUserById(long idUsuario) {
        String sql = """
                SELECT u.ID_USUARIO, u.NOMBRES, u.APELLIDOS, u.CORREO, u.ESTADO, u.ID_ROL,
                       r.NOMBRE AS ROL_NOMBRE, u.CREADO_EN, u.ACTUALIZADO_EN
                FROM USUARIO u
                JOIN ROL r ON r.ID_ROL = u.ID_ROL
                WHERE u.ID_USUARIO = ?
                """;
        return oracleJdbcTemplate.query(sql, userRowMapper(), idUsuario).stream().findFirst();
    }

    private Optional<AdminUserRow> findUserRow(long idUsuario) {
        String sql = """
                SELECT ID_USUARIO, NOMBRES, APELLIDOS, CORREO, CONTRASENA, ESTADO, ID_ROL,
                       CREADO_EN, ACTUALIZADO_EN, CREADO_POR, ACTUALIZADO_POR
                FROM USUARIO
                WHERE ID_USUARIO = ?
                """;
        return oracleJdbcTemplate.query(sql, adminUserRowMapper(), idUsuario).stream().findFirst();
    }

    private void rollbackUser(JdbcTemplate jdbcTemplate, AdminUserRow previous) {
        jdbcTemplate.update(
                """
                        UPDATE USUARIO
                           SET NOMBRES = ?,
                               APELLIDOS = ?,
                               CORREO = ?,
                               CONTRASENA = ?,
                               ESTADO = ?,
                               ID_ROL = ?,
                               CREADO_EN = ?,
                               ACTUALIZADO_EN = ?,
                               CREADO_POR = ?,
                               ACTUALIZADO_POR = ?
                         WHERE ID_USUARIO = ?
                        """,
                previous.nombres,
                previous.apellidos,
                previous.correo,
                previous.contrasena,
                previous.estado,
                previous.idRol,
                previous.creadoEn,
                previous.actualizadoEn,
                previous.creadoPor,
                previous.actualizadoPor,
                previous.idUsuario
        );
    }

    private String normalizeEmail(String correo) {
        return correo.trim().toLowerCase(Locale.ROOT);
    }

    private String nullableTrim(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String resolveAuditActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return FALLBACK_AUDIT_USER;
        }

        String correo = authentication.getName();
        if (correo == null || correo.isBlank()) {
            return FALLBACK_AUDIT_USER;
        }

        return authUserStore.findByCorreo(correo)
                .map(user -> user.nombre() == null ? "" : user.nombre().trim())
                .filter(nombre -> !nombre.isBlank())
                .orElse(correo);
    }

    private RowMapper<AdminUserResponse> userRowMapper() {
        return (rs, rowNum) -> new AdminUserResponse(
                rs.getLong("ID_USUARIO"),
                rs.getString("NOMBRES"),
                rs.getString("APELLIDOS"),
                rs.getString("CORREO"),
                rs.getInt("ESTADO"),
                rs.getLong("ID_ROL"),
                rs.getString("ROL_NOMBRE"),
                toOffsetDateTime(rs, "CREADO_EN"),
                toOffsetDateTime(rs, "ACTUALIZADO_EN")
        );
    }

    private RowMapper<AdminUserRow> adminUserRowMapper() {
        return (rs, rowNum) -> {
            AdminUserRow row = new AdminUserRow();
            row.idUsuario = rs.getLong("ID_USUARIO");
            row.nombres = rs.getString("NOMBRES");
            row.apellidos = rs.getString("APELLIDOS");
            row.correo = rs.getString("CORREO");
            row.contrasena = rs.getString("CONTRASENA");
            row.estado = rs.getInt("ESTADO");
            row.idRol = rs.getLong("ID_ROL");
            row.creadoEn = rs.getTimestamp("CREADO_EN");
            row.actualizadoEn = rs.getTimestamp("ACTUALIZADO_EN");
            row.creadoPor = rs.getString("CREADO_POR");
            row.actualizadoPor = rs.getString("ACTUALIZADO_POR");
            return row;
        };
    }

    private OffsetDateTime toOffsetDateTime(ResultSet rs, String column) throws java.sql.SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp.toInstant().atOffset(ZoneOffset.UTC);
    }

    private static final class AdminUserRow {
        private long idUsuario;
        private String nombres;
        private String apellidos;
        private String correo;
        private String contrasena;
        private int estado;
        private long idRol;
        private Timestamp creadoEn;
        private Timestamp actualizadoEn;
        private String creadoPor;
        private String actualizadoPor;
    }
}
