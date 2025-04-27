package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.Chegada_Turistas_Internacionais_Brasil_Anual_DAO;
import tour.wise.dao.Chegada_Turistas_Internacionais_Brasil_MensalDAO;
import tour.wise.dao.Fonte_DAO;
import tour.wise.dao.Pais_DAO;
import tour.wise.dao.Unidade_Federativa_Brasil_DAO;
import tour.wise.model.Pais;
import tour.wise.model.Unidade_Federativa_Brasil;
import tour.wise.model.Chegada_Turistas_Internacionais_Brasil_Mensal;

import tour.wise.service.Service;


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


public class Chegada_Turistas_Internacionais_Brasil_Mensal_ETL {

    Service service = new Service();

    public void extractTransformLoad(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types, String orgao_emissor, String edicao, String titulo_edicao, String url_fonte, JdbcTemplate connection) throws IOException {

        // EXTRACT
        List<List<Object>> data = extract(fileName, sheetNumber, header, colluns, types);

        // TRANSFORM

        List<Chegada_Turistas_Internacionais_Brasil_Mensal> chegadas_turistas_internacionais_brasil = transform(data, orgao_emissor, edicao);

        System.out.println();
        System.out.println("Chegadas");

        for (Chegada_Turistas_Internacionais_Brasil_Mensal chegada_turistas_internacionais_brasil : chegadas_turistas_internacionais_brasil) {
            System.out.println(chegada_turistas_internacionais_brasil);
        }

        load(connection, orgao_emissor, edicao, titulo_edicao, url_fonte, chegadas_turistas_internacionais_brasil);
    }

    public List<List<Object>> extract(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types) {

        System.out.println("[INÍCIO] Extração de dados iniciada.");
        System.out.println("[INFO] Arquivo: " + fileName);
        System.out.println("[INFO] Planilha (sheet): " + sheetNumber);
        System.out.println("[INFO] Linha de cabeçalho: " + header);
        System.out.println("[INFO] Quantidade de colunas esperadas: " + colluns);
        System.out.println("[INFO] Tipos esperados: " + types);

        List<List<Object>> data = null;

        try {
            data = service.extract(fileName, sheetNumber, header, colluns, types);

            System.out.println("[SUCESSO] ETL finalizado com sucesso. Total de registros extraídos: " + (data != null ? data.size() : 0));
        } catch (Exception e) {
            System.out.println("[ERRO] Falha na extração dos dados: " + e.getMessage());
            e.printStackTrace();
        }

        return data;
    }


    public List<Chegada_Turistas_Internacionais_Brasil_Mensal> transform(List<List<Object>> data, String fonte, String edicao) {

        System.out.println("[INÍCIO] Transformação dos dados iniciada.");
        System.out.println("[INFO] Fonte: " + fonte + ", Edição: " + edicao);
        System.out.println("[INFO] Total de registros brutos recebidos: " + (data != null ? data.size() : 0));

        List<Chegada_Turistas_Internacionais_Brasil_Mensal> chegadas_turistas_internacionais_brasil = new ArrayList<>();

        int linha = 0;
        for (List<Object> datum : data) {
            try {
                linha++;
                Pais pais_origem = new Pais(datum.get(2).toString());
                Unidade_Federativa_Brasil destino = new Unidade_Federativa_Brasil(datum.get(4).toString());
                String via = datum.get(6).toString();
                Integer ano = Double.valueOf(datum.get(10).toString()).intValue();
                Integer mes = Double.valueOf(datum.get(8).toString()).intValue();
                Integer chegadasNum = Double.valueOf(datum.get(11).toString()).intValue();

                Chegada_Turistas_Internacionais_Brasil_Mensal chegada = new Chegada_Turistas_Internacionais_Brasil_Mensal(
                        destino, pais_origem, via, ano, mes, chegadasNum, fonte, edicao
                );

                chegadas_turistas_internacionais_brasil.add(chegada);

                System.out.println("[TRANSFORMADO] Linha " + linha + " -> " + chegada);
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao transformar a linha " + linha + ": " + datum);
                e.printStackTrace();
            }
        }

        System.out.println("[FIM] Transformação concluída. Total de registros convertidos: " + chegadas_turistas_internacionais_brasil.size());

        return chegadas_turistas_internacionais_brasil;
    }


    public void load(JdbcTemplate connection, String orgao_emissor, String edicao, String titulo_edicao, String url_fonte, List<Chegada_Turistas_Internacionais_Brasil_Mensal> chegadas) {

        System.out.println("Iniciando carregamento de chegadas para a fonte: '" + titulo_edicao + "'...");

        Pais_DAO pais_dao = new Pais_DAO(connection);
        Fonte_DAO fonte_dao = new Fonte_DAO(connection);
        Unidade_Federativa_Brasil_DAO unidade_federativa_brasil_dao = new Unidade_Federativa_Brasil_DAO(connection);
        Chegada_Turistas_Internacionais_Brasil_MensalDAO chegada_turistas_internacionais_brasil_Mensal_dao = new Chegada_Turistas_Internacionais_Brasil_MensalDAO(connection);

        fonte_dao.insertFonteIgnore(
                titulo_edicao,
                edicao,
                orgao_emissor,
                url_fonte
        );

        Integer fonteId = fonte_dao.getFonteId(titulo_edicao);

        if (fonteId == null) {
            System.out.println("Fonte não encontrada no banco. Interrompendo operação.");
            return;
        }

        System.out.println("Fonte encontrada. ID: " + fonteId);
        System.out.println("Iniciando processamento de " + chegadas.size() + " registros...");

        int inseridos = 0;
        int ignorados = 0;

        for (Chegada_Turistas_Internacionais_Brasil_Mensal chegada : chegadas) {
            String nomePais = chegada.getPais_origem() != null ? chegada.getPais_origem().getPais() : null;
            String unidade_federativa_brasil = chegada.getDestino() != null ? chegada.getDestino().getUnidade_federativa() : null;
            Integer mes = chegada.getMes();
            Integer ano = chegada.getAno();
            Integer qtdChegadas = chegada.getChegadas();
            String via = chegada.getVia_acesso();

            boolean dadosIncompletos = false;

            if (nomePais == null || nomePais.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: país nulo ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (unidade_federativa_brasil == null || unidade_federativa_brasil.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: destino nulo ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (mes == null || mes < 1 || mes > 12) {
                System.out.println("[AVISO] Dados incompletos: mês inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (ano == null || ano < 1900 || ano > 2100) {
                System.out.println("[AVISO] Dados incompletos: ano inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (qtdChegadas == null || qtdChegadas < 0) {
                System.out.println("[AVISO] Dados incompletos: número de chegadas inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (via == null || via.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: via de acesso nula ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }

            if (dadosIncompletos) {
                ignorados++;
                continue;
            }


            Integer paisId = pais_dao.getPaisId(nomePais);

            if(paisId == null){
                pais_dao.insertPais(nomePais);
                paisId = pais_dao.getPaisId(nomePais);
            }

            String unidade_federativa_brasil_sigla = unidade_federativa_brasil_dao.getSiglaPorNome(chegada.getDestino().getUnidade_federativa());

            if (unidade_federativa_brasil_sigla == null) {
                System.out.println("Unidade Federativa não encontrada no banco. Interrompendo operação.");
                return;
            }

            System.out.println("Unidade Federativa encontrada. SIGLA: " + unidade_federativa_brasil_sigla);


            if (chegada_turistas_internacionais_brasil_Mensal_dao.isChegadaExistente(chegada, paisId)) {
                System.out.println("[INFO] Chegada já cadastrada: " +
                        chegada.getAno() + "/" + chegada.getMes() +
                        " - UF: " + chegada.getDestino().getSigla() +
                        " - País: " + nomePais);
                ignorados++;
                continue;
            }

            chegada_turistas_internacionais_brasil_Mensal_dao.insertChegada(chegada, fonteId, paisId, unidade_federativa_brasil_sigla);
            System.out.println("[INSERIDO] Chegada inserida com sucesso: " +
                    chegada.getAno() + "/" + chegada.getMes() +
                    " - UF: " + chegada.getDestino().getUnidade_federativa() +
                    " - País: " + nomePais);
            inseridos++;
        }

        Chegada_Turistas_Internacionais_Brasil_Anual_DAO chegada_turistas_internacionais_brasil_anual_dao = new Chegada_Turistas_Internacionais_Brasil_Anual_DAO(connection);

        chegada_turistas_internacionais_brasil_anual_dao.insertChegadasAnuais();

        System.out.println("Processamento concluído.");
        System.out.println("Total de registros processados: " + chegadas.size());
        System.out.println("Inseridos: " + inseridos);
        System.out.println("Ignorados (já existentes ou inválidos): " + ignorados);
    }





}


