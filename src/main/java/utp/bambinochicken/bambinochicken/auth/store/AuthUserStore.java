package utp.bambinochicken.bambinochicken.auth.store;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUserStore {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public AuthUserStore(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    public Optional<AuthUserRecord> findByCorreo(String correo) {
        Optional<AuthUserRecord> oracle = findByCorreo(oracleJdbcTemplate, correo, true);
        return oracle.isPresent() ? oracle : findByCorreo(mysqlJdbcTemplate, correo, false);
    }

    public Optional<AuthUserRecord> findById(Long idUsuario) {
        Optional<AuthUserRecord> oracle = findById(oracleJdbcTemplate, idUsuario, true);
        return oracle.isPresent() ? oracle : findById(mysqlJdbcTemplate, idUsuario, false);
    }

    private Optional<AuthUserRecord> findByCorreo(JdbcTemplate jdbcTemplate, String correo, boolean oracleSql) {
        String sql = """
                SELECT u.ID_USUARIO, u.CORREO, u.CONTRASENA, u.ESTADO, r.NOMBRE AS ROL,
                       u.NOMBRES, u.APELLIDOS
                  FROM USUARIO u
                  JOIN ROL r ON r.ID_ROL = u.ID_ROL
                 WHERE LOWER(u.CORREO) = LOWER(?)
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUserRecord(jdbcTemplate, rs.getLong("ID_USUARIO"), rs.getString("CORREO"),
                rs.getString("CONTRASENA"), rs.getInt("ESTADO") == 1, rs.getString("ROL"),
                rs.getString("NOMBRES"), rs.getString("APELLIDOS"), oracleSql), correo).stream().findFirst();
    }

    private Optional<AuthUserRecord> findById(JdbcTemplate jdbcTemplate, Long idUsuario, boolean oracleSql) {
        String sql = """
                SELECT u.ID_USUARIO, u.CORREO, u.CONTRASENA, u.ESTADO, r.NOMBRE AS ROL,
                       u.NOMBRES, u.APELLIDOS
                  FROM USUARIO u
                  JOIN ROL r ON r.ID_ROL = u.ID_ROL
                 WHERE u.ID_USUARIO = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUserRecord(jdbcTemplate, rs.getLong("ID_USUARIO"), rs.getString("CORREO"),
                rs.getString("CONTRASENA"), rs.getInt("ESTADO") == 1, rs.getString("ROL"),
                rs.getString("NOMBRES"), rs.getString("APELLIDOS"), oracleSql), idUsuario).stream().findFirst();
    }

    private AuthUserRecord mapUserRecord(
            JdbcTemplate jdbcTemplate,
            Long id,
            String correo,
            String passwordHash,
            boolean activo,
            String rol,
            String nombres,
            String apellidos,
            boolean oracleSql
    ) {
        String nombreCompleto = (nombres == null ? "" : nombres.trim()) +
                ((apellidos == null || apellidos.isBlank()) ? "" : " " + apellidos.trim());
        if (nombreCompleto.isBlank()) {
            nombreCompleto = correo;
        }

        String localSql = oracleSql
                ? "SELECT ID_LOCAL, NOMBRE FROM LOCAL WHERE ESTADO = 1 ORDER BY ID_LOCAL FETCH FIRST 1 ROWS ONLY"
                : "SELECT ID_LOCAL, NOMBRE FROM LOCAL WHERE ESTADO = 1 ORDER BY ID_LOCAL LIMIT 1";

        Optional<LocalData> local = jdbcTemplate.query(localSql,
                (rs, rowNum) -> new LocalData(rs.getLong("ID_LOCAL"), rs.getString("NOMBRE")))
                .stream()
                .findFirst();

        return new AuthUserRecord(
                id,
                correo,
                passwordHash,
                rol,
                activo,
                nombreCompleto,
                local.map(LocalData::idLocal).orElse(1L),
                local.map(LocalData::nombre).orElse("Local Principal")
        );
    }

    private record LocalData(Long idLocal, String nombre) {
    }
}
