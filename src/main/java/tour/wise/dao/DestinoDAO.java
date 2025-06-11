package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class DestinoDAO {

    private static final Logger logger = LoggerFactory.getLogger(DestinoDAO.class);

    private static final String SQL_INSERT_DESTINO = """
        INSERT INTO destino (
            fk_perfil_estimado_turistas,
            fk_pais_origem,
            fk_uf_destino,
            fk_uf_entrada,
            permanencia_media
        ) VALUES (?, ?, ?, ?, ?)
    """;

    /**
     * Insere em lote os destinos relacionados a um perfil estimado de turista.
     *
     * @param jdbcTemplate           JdbcTemplate com a conexão configurada
     * @param idPerfil               ID do perfil estimado de turista
     * @param fkPaisOrigem           ID do país de origem
     * @param fkUfEntrada            Sigla da UF de entrada no Brasil
     * @param ufsDestino             Lista de siglas das UFs visitadas (destinos)
     * @param permanenciaMediaDias   Número de dias de permanência média
     */
    public static void insertDestinosLote(
            JdbcTemplate jdbcTemplate,
            int idPerfil,
            int fkPaisOrigem,
            String fkUfEntrada,
            List<String> ufsDestino,
            double permanenciaMediaDias
    ) {
        logger.info("=== INÍCIO DO PROCESSAMENTO DE DESTINOS ===");
        logger.info("idPerfil: {}", idPerfil);
        logger.info("fkPaisOrigem: {}", fkPaisOrigem);
        logger.info("fkUfEntrada: [{}] (length: {})", fkUfEntrada,
                fkUfEntrada != null ? fkUfEntrada.length() : "NULL");
        logger.info("permanenciaMediaDias: {}", permanenciaMediaDias);
        logger.info("Número total de UFs destino: {}", ufsDestino != null ? ufsDestino.size() : "NULL");

        // Validação de entrada
        if (ufsDestino == null || ufsDestino.isEmpty()) {
            logger.error("ERRO: Lista de UFs destino está vazia ou nula!");
            return;
        }

        // Log detalhado de cada UF
        logger.info("=== ANÁLISE DETALHADA DAS UFs DESTINO ===");
        for (int i = 0; i < ufsDestino.size(); i++) {
            String uf = ufsDestino.get(i);
            logger.info("UF[{}]: [{}] - Length: {} - Bytes: {} - Classe: {}",
                    i,
                    uf,
                    uf != null ? uf.length() : "NULL",
                    uf != null ? uf.getBytes().length : "NULL",
                    uf != null ? uf.getClass().getSimpleName() : "NULL");

            if (uf != null) {
                // Log de caracteres individuais
                StringBuilder charDetails = new StringBuilder();
                for (int j = 0; j < uf.length(); j++) {
                    char c = uf.charAt(j);
                    charDetails.append(String.format("'%c'(ASCII:%d) ", c, (int)c));
                }
                logger.info("UF[{}] caracteres: {}", i, charDetails.toString());

                // Verificar se tem caracteres especiais ou invisíveis
                if (uf.length() > 2) {
                    logger.warn("⚠️  UF[{}] SUSPEITA - Comprimento maior que 2: [{}]", i, uf);
                }

                // Verificar espaços em branco
                if (!uf.trim().equals(uf)) {
                    logger.warn("⚠️  UF[{}] SUSPEITA - Contém espaços: [{}] -> trimmed: [{}]",
                            i, uf, uf.trim());
                }

                // Verificar caracteres não ASCII
                for (char c : uf.toCharArray()) {
                    if (c > 127) {
                        logger.warn("⚠️  UF[{}] SUSPEITA - Contém caractere não-ASCII: '{}' (code: {})",
                                i, c, (int)c);
                    }
                }
            }
        }

        // Verificar duplicatas
        Set<String> ufsUnicas = new HashSet<>(ufsDestino);
        if (ufsUnicas.size() != ufsDestino.size()) {
            logger.warn("⚠️  ATENÇÃO: Existem UFs duplicadas na lista!");
            logger.info("UFs originais: {} | UFs únicas: {}", ufsDestino.size(), ufsUnicas.size());
        }

        // Log do cálculo da permanência
        double permanenciaPorUf = permanenciaMediaDias / ufsDestino.size();
        logger.info("Permanência por UF: {} (total: {} / quantidade: {})",
                permanenciaPorUf, permanenciaMediaDias, ufsDestino.size());

        logger.info("=== INICIANDO INSERÇÕES NO BANCO ===");

        int contador = 0;
        for (String ufDestino : ufsDestino) {
            contador++;

            try {
                logger.info("--- Inserção #{} ---", contador);
                logger.info("Parâmetros para inserção:");
                logger.info("  - idPerfil: {}", idPerfil);
                logger.info("  - fkPaisOrigem: {}", fkPaisOrigem);
                logger.info("  - ufDestino: [{}] (length: {})", ufDestino,
                        ufDestino != null ? ufDestino.length() : "NULL");
                logger.info("  - fkUfEntrada: [{}] (length: {})", fkUfEntrada,
                        fkUfEntrada != null ? fkUfEntrada.length() : "NULL");
                logger.info("  - permanenciaPorUf: {}", permanenciaPorUf);

                // Tentativa de inserção
                logger.debug("Executando SQL: {}", SQL_INSERT_DESTINO);

                int rowsAffected = jdbcTemplate.update(SQL_INSERT_DESTINO,
                        idPerfil,
                        fkPaisOrigem,
                        ufDestino,
                        fkUfEntrada,
                        permanenciaPorUf
                );

                logger.info("✅ Inserção #{} bem-sucedida! Rows affected: {}", contador, rowsAffected);

            } catch (Exception e) {
                logger.error("❌ ERRO na inserção #{} com UF destino: [{}]", contador, ufDestino);
                logger.error("Tipo do erro: {}", e.getClass().getSimpleName());
                logger.error("Mensagem do erro: {}", e.getMessage());

                // Log adicional para erros de tamanho
                if (e.getMessage() != null && e.getMessage().toLowerCase().contains("too long")) {
                    logger.error("🔍 ERRO DE TAMANHO DETECTADO!");
                    logger.error("UF problemática: [{}]", ufDestino);
                    logger.error("Tamanho em caracteres: {}", ufDestino != null ? ufDestino.length() : "NULL");
                    logger.error("Tamanho em bytes: {}", ufDestino != null ? ufDestino.getBytes().length : "NULL");

                    if (ufDestino != null) {
                        logger.error("Representação hexadecimal: {}",
                                bytesToHex(ufDestino.getBytes()));
                    }
                }

                // Re-throw para não mascarar o erro original
                throw new RuntimeException("Erro ao inserir destino para UF: " + ufDestino, e);
            }
        }

        logger.info("=== PROCESSAMENTO CONCLUÍDO ===");
        logger.info("Total de inserções realizadas: {}", contador);
    }

    /**
     * Converte bytes para representação hexadecimal
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X ", b));
        }
        return result.toString().trim();
    }

    /**
     * Método adicional para validar UFs antes da inserção
     */
    public static void validarUfs(List<String> ufs) {
        logger.info("=== VALIDAÇÃO DE UFs ===");

        if (ufs == null) {
            logger.error("Lista de UFs é NULL!");
            return;
        }

        Set<String> ufsValidas = Set.of(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
                "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
                "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        );

        for (int i = 0; i < ufs.size(); i++) {
            String uf = ufs.get(i);

            if (uf == null) {
                logger.warn("UF[{}] é NULL!", i);
            } else if (uf.trim().isEmpty()) {
                logger.warn("UF[{}] está vazia!", i);
            } else if (!ufsValidas.contains(uf.trim().toUpperCase())) {
                logger.warn("UF[{}] inválida: [{}]", i, uf);
            } else {
                logger.debug("UF[{}] válida: [{}]", i, uf);
            }
        }
    }
}