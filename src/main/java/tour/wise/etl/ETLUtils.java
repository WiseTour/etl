package tour.wise.etl;

import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.model.Pais;

import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ETLUtils {
    private static final int ANO_MINIMO = 1989; // PRIMEIRA VERSÃO DISPONÍVEL DO DOCUMENTO
    private static final int ANO_ATUAL = Year.now().getValue();

    public static List<Integer> extrairTodosAnosValidos(String texto) {
        Set<Integer> anos = new TreeSet<>();

        // Captura anos únicos e intervalos
        Pattern pattern = Pattern.compile("(?<!\\d)(\\d{4})(?:-(\\d{4}))?(?!\\d)");
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            try {
                int anoInicio = Integer.parseInt(matcher.group(1));
                int anoFim = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : anoInicio;

                if (anoInicio <= anoFim && anoInicio >= ANO_MINIMO && anoFim <= ANO_ATUAL) {
                    for (int ano = anoInicio; ano <= anoFim; ano++) {
                        anos.add(ano);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignora entradas malformadas
            }
        }

        return new ArrayList<>(anos);
    }

    public static String extrairUltimoAno(String texto) {
        List<Integer> anos = extrairTodosAnosValidos(texto);
        return anos.isEmpty() ? null : Collections.max(anos).toString();
    }

    public static String validarAnoNoIntervalo(String texto, String ano) {
        int anoParaValidar;

        try {
            anoParaValidar = Integer.parseInt(ano);
        } catch (NumberFormatException e) {
            return null;
        }

        if (anoParaValidar < ANO_MINIMO || anoParaValidar > ANO_ATUAL) {
            return null;
        }

        List<Integer> anos = extrairTodosAnosValidos(texto);
        return anos.contains(anoParaValidar) ? ano : null;
    }

    public static List<Pais> listarPaisesNaoCadastrados(
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas,
            List<Pais> paisesExistentes
    ) {
        // Mapa para pesquisa rápida: nomes existentes em minúsculo
        Set<String> nomesPaisesExistentes = paisesExistentes.stream()
                .map(p -> p.getNomePais().toLowerCase())
                .collect(Collectors.toSet());

        // Usar lista para acumular e evitar streams intermediários
        List<Pais> paisesNovos = new ArrayList<>();

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadas) {
            String paisOrigem = chegada.getPaisOrigem();
            if (paisOrigem != null) {
                String paisOrigemLower = paisOrigem.toLowerCase();
                if (!nomesPaisesExistentes.contains(paisOrigemLower)) {
                    paisesNovos.add(new Pais(paisOrigem));
                    // Opcional: adicionar ao set para evitar repetição de mesmo nome
                    nomesPaisesExistentes.add(paisOrigemLower);
                }
            }
        }
        return paisesNovos;
    }

    public static boolean possuiVersaoIndisponivel(String texto) {
        if (texto == null) return false;

        // Remove acentos e normaliza
        String normalizado = java.text.Normalizer
                .normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("\\s+", " ") // normaliza espaços
                .trim();

        return normalizado.contains("(versao atual indisponivel)");
    }
}
