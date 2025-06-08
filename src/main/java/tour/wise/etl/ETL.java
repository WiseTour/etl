package tour.wise.etl;

import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.etl.extract.Extract;
import tour.wise.etl.load.Load;
import tour.wise.etl.transform.Transform;
import tour.wise.util.DataBaseConnection;
import tour.wise.util.Event;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.model.*;
import tour.wise.util.S3;
import tour.wise.util.Slack;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ETL {

    public void exe(
            String fileNameChegadas,
            String fileNameFichaSintesePais,
            String fileNameFichaSinteseBrasil,
            String fileNameFichaSinteseEstado,
            String orgaoEmissor
    ) throws Exception {

        try {
            // 1. INÍCIO DO PROCESSO
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.INCIALIZACAO.getId()),
                    "Iniciando o processo de ETL...",
                    "gear"
            );

            String edicaoChegadas = fileNameChegadas.substring(fileNameChegadas.length() - 4);
            String anoPais = fileNameFichaSintesePais.substring(fileNameFichaSintesePais.length() - 4);
            String anoBrasil = fileNameFichaSinteseBrasil.substring(fileNameFichaSinteseBrasil.length() - 4);
            String anoEstado = fileNameFichaSinteseEstado.substring(fileNameFichaSinteseEstado.length() - 4);

            if (!anoPais.equals(anoBrasil) || !anoPais.equals(anoEstado)) {
                String mensagemErro = "Os arquivos de ficha síntese têm anos diferentes: "
                        + "País = " + anoPais + ", Brasil = " + anoBrasil + ", Estado = " + anoEstado;

                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        mensagemErro,
                        false
                );

                throw new IllegalArgumentException(mensagemErro);
            }

            String edicaoFichasSinteses = anoBrasil;

            if (!edicaoChegadas.equals(edicaoFichasSinteses)) {
                String mensagemErro = "A edição dos dados de chegada (" + edicaoChegadas + ") "
                        + "é diferente da edição das fichas síntese (" + edicaoFichasSinteses + ").";

                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        mensagemErro,
                        false
                );

                throw new IllegalArgumentException(mensagemErro);
            }


            // 2.0 EXTRAÇÃO DOS DADOS DE CHEGADAS

            // 2.1 CARREGAMENTO DA FONTE DAS CHEGADAS NA TABELA ORIGEM_DADOS
            try {
                Load.loadOrigemDados(new OrigemDados("Chegadas " + edicaoChegadas, edicaoChegadas, orgaoEmissor));
                DataBaseConnection.getConnection().commit();

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Fonte de dados de chegadas de " + edicaoChegadas + " registrada com sucesso.",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Falha ao registrar a fonte de dados de chegadas de" + edicaoChegadas + ".",
                        e,
                        false
                );
            }

            // 2.1.2 RECUPERAÇÃO DO ID DA FONTE
            Integer fkFonteChegadas = 0;
            try {
                Integer id = OrigemDadosDAO.findByTitulo("Chegadas " + edicaoChegadas).getIdOrigemDados();
                fkFonteChegadas = (id == null) ? 0 : id;

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Identificador da fonte de dados recuperado com sucesso.",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Erro ao recuperar o identificador da fonte de dados.",
                        e,
                        false
                );
            }

            // 2.2 ABERTURA DO ARQUIVO EXCEL
            Workbook workbook = null;
            try {
                workbook = S3.readFile(fileNameChegadas);

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Arquivo Excel de chegadas de" + edicaoChegadas + "  aberto com sucesso!",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Erro ao abrir o arquivo Excel de chegadas de" + edicaoChegadas + ".",
                        e,
                        false
                );
            }

            // 2.3 EXTRAÇÃO DOS DADOS DE CHEGADAS INICIADA
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.INCIALIZACAO.getId()),
                    "Iniciando processo de extração dos dados de chegadas de" + edicaoChegadas + "...",
                    "mag"
            );

            List<List<Object>> chegadasTuristasInternacionaisBrasilMensalData = new ArrayList<>();

            try {
                chegadasTuristasInternacionaisBrasilMensalData = Extract.extractChegadasTuristasInternacionaisBrasilMensalData(
                        workbook,
                        fkFonteChegadas,
                        "Chegada_Turistas_Internacionais_Brasil_Mensal",
                        fileNameChegadas,
                        0,
                        0,
                        12,
                        List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
                );

                // 2.4 EXTRAÇÃO DOS DADOS DE CHEGADA FINALIZADA
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Processo de extração dos dados de chegadas de " + edicaoChegadas + " finalizados com sucesso!",
                        "white_check_mark"
                );

            } catch (Exception e) {

                // 2.3.1 FALHA NA EXTRAÇÃO DOS DADOS DE CHEGADA
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Falha ao extrair os dados de chegadas de" + edicaoChegadas + ".",
                        e,
                        true,
                        "white_check_mark"
                );
            }

            // 3.0 TRANSFORMAÇÃO DOS DADOS DE CHEGADAS

            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.TRANSFORMACAO.getId()),
                    "Iniciando processo de tranformação dos dados de chegadas de " + edicaoChegadas + "...",
                    true,
                    "arrows_counterclockwise"
            );
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO = new ArrayList<>();

            try {
                chegadasTuristasInternacionaisBrasilMensalDTO =
                        Transform.transformChegadasTuristasInternacionaisBrasilMensal(
                                chegadasTuristasInternacionaisBrasilMensalData,
                                orgaoEmissor,
                                edicaoChegadas);

                chegadasTuristasInternacionaisBrasilMensalData.clear();

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.TRANSFORMACAO.getId()),
                        "Dados de chegadas de " + edicaoChegadas + " transformados com sucesso!",
                        true,
                        "page_facing_up"
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Falha ao transformados os dados de chegadas de" + edicaoChegadas + ".",
                        e,
                        false
                );
            }

            // 4.0 CARREGAMENTO DOS PAÍSES PRESENTES NA BASE CHEGADAS NO BANCO, CASO AINDA NÃO PERSISTAM
            List<Pais> paises = new ArrayList<>();

            try {
                // 4.1 LISTAR PAÍSES QUE AINDA NÃO ESTÃO NO BANCO
                paises = listarPaisesNaoCadastrados(
                        chegadasTuristasInternacionaisBrasilMensalDTO,
                        PaisDAO.findAll()
                );

                // 4.2 INSERIR PAÍSES NOVOS
                if (!paises.isEmpty()) {
                    for (Pais pais : paises) {
                        Load.loadPais(pais);
                    }
                    DataBaseConnection.getConnection().commit();
                }

                paises.clear();
                // 4.3 SUCESSO NO CARREGAMENTO DOS PAÍSES
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                        "Carregamento dos países novos finalizado com sucesso!",
                        false
                );

            } catch (Exception e) {
                // 4.3.1 ERRO NO CARREGAMENTO DOS PAÍSES
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                        "Erro ao carregar países não cadastrados no banco.",
                        e,
                        false
                );
            }

            // 5.0 PROCESSAMENTO DAS FICHAS SÍNTESE
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.EXTRACAO.getId()),
                    "Iniciando o processamento das Fichas Sínteses de " + edicaoFichasSinteses + "...",
                    "gear"
            );

            // 5.1 CARREGAMENTO DA FONTE DAS FICHAS SÍNTESES NA TABELA ORIGEM_DADOS
            try {
                Load.loadOrigemDados(new OrigemDados("Fichas Síntese " + edicaoFichasSinteses, edicaoFichasSinteses, orgaoEmissor));
                DataBaseConnection.getConnection().commit();

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Fonte de dados das Fichas Síntese de " + edicaoFichasSinteses + " registrada com sucesso.",
                        false
                );
            } catch (Exception e) {
                // 5.1.1 ERRO NO CARREGAMENTO DA FONTE DAS FICHAS SÍNTESES NA TABELA ORIGEM_DADOS
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.CARREGAMENTO.getId()),
                        "Falha ao registrar a fonte de dados das Fichas Síntese de" + edicaoFichasSinteses + ".",
                        e,
                        false
                );
            }

            // 5.2 PROCESSAMENTO DA FICHA SÍNTESE BRASIL
            // 5.2.1 ABERTURA DO ARQUIVO EXCEL
            workbook = null;
            try {
                workbook = S3.readFile(fileNameFichaSinteseBrasil);

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Arquivo Excel de Ficha Síntese Brasil de" + edicaoFichasSinteses + "  aberto com sucesso!",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Erro ao abrir o arquivo Excel de Ficha Síntese Brasil de" + edicaoFichasSinteses + ".",
                        e,
                        false
                );
            }

            FichaSinteseBrasilDTO fichaSinteseBrasilDTO = null;
            List<List<List<Object>>> fichaSinteseBrasilData = null;

            // 5.2.2 EXTRAÇÃO DA FICHA SÍNTESE BRASIL
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.INCIALIZACAO.getId()),
                    "Iniciando processo de extração dos dados da Ficha Síntese Brasil de" + edicaoFichasSinteses + "...",
                    "mag"
            );

            try {
                fichaSinteseBrasilData = Extract.extractFichaSinteseBrasilData(
                        workbook,
                        fileNameFichaSinteseBrasil,
                        1,
                        List.of(1, 7),
                        List.of(10, 16),
                        List.of("string", "numeric")
                );

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                        "Extração da Ficha Síntese Brasil de " + edicaoFichasSinteses + " concluída com sucesso!",
                        true,
                        "white_check_mark"
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                        "Erro ao extrair a Ficha Síntese Brasil de " + edicaoFichasSinteses + ".",
                        e,
                        true,
                        "x"
                );
            }

            // 5.2.3 FECHAMENTO DO ARQUIVO FICHA SÍNTESE BRASIL

            try {
                workbook.close();
            } catch (IOException e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                        "Erro ao fechar o arquivo de Ficha Síntese Brasil: " + fileNameFichaSinteseBrasil,
                        e,
                        false
                );
            }

            // 5.2.4 TRANSFORMAÇÃO DA FICHA SÍNTESE BRASIL
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.TRANSFORMACAO.getId()),
                    "Iniciando processo de tranformação dos dados da e Ficha Síntese Brasil " + edicaoFichasSinteses + "...",
                    true,
                    "arrows_counterclockwise"
            );
            try {
                fichaSinteseBrasilDTO = Transform.transformFichaSinteseBrasil(fichaSinteseBrasilData);

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Transformação da Ficha Síntese Brasil de " + edicaoFichasSinteses + " concluída com sucesso!",
                        true,
                        "white_check_mark"
                );

            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Erro ao transformar a Ficha Síntese Brasil de " + edicaoFichasSinteses + ".",
                        e,
                        true,
                        "x"
                );
            }

            // 5.3 PROCESSAMENTO DA FICHA SÍNTESE POR PAÍS

            // 5.3.1 ABERTURA DO ARQUIVO EXCEL
            try {
                workbook = S3.readFile(fileNameFichaSintesePais);
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Arquivo Excel de Ficha Síntese por País de" + edicaoFichasSinteses + "  aberto com sucesso!",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Erro ao abrir o arquivo Excel de Ficha Síntese por País de" + edicaoFichasSinteses + ".",
                        e,
                        false
                );
                throw new RuntimeException(e);
            }

            // 5.3.2 EXTRAÇÃO DAS FICHAS SÍNTESE POR PAÍS
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.INCIALIZACAO.getId()),
                    "Iniciando processo de extração dos dados da Ficha Síntese por País de" + edicaoFichasSinteses + "...",
                    "mag"
            );

            List<List<List<List<Object>>>> todasFichasSintesePaisData = new ArrayList<>();
            boolean extracaoSucesso = true;
            for (int indicePlanilha = 1; indicePlanilha < Service.getSheetNumber(workbook); indicePlanilha++) {
                try {
                    List<List<List<Object>>> fichaSintesePaisData = Extract.extractFichasSintesePaisData(
                            workbook,
                            fileNameFichaSintesePais,
                            indicePlanilha,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric")
                    );

                    todasFichasSintesePaisData.add(fichaSintesePaisData);

                    Event.registerEvent(
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                            "Extração da Ficha Síntese País da planilha " + indicePlanilha + " de " + edicaoFichasSinteses + " concluída com sucesso!",
                            false
                    );
                } catch (Exception e) {
                    extracaoSucesso = false;

                    Event.registerEvent(
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                            "Erro ao extrair Ficha Síntese País da planilha " + indicePlanilha + " de " + edicaoFichasSinteses + ".",
                            e,
                            false
                    );
                }
            }

            if (extracaoSucesso) {
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                        "Extração de todas as planilhas da Ficha Síntese País " + edicaoChegadas + " concluída com sucesso!",
                        true,
                        "white_check_mark"
                );
            } else {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                        "Houve erro(s) durante a extração das planilhas da Ficha Síntese País " + edicaoChegadas + ".",
                        true,
                        "x"
                );
            }

            // 5.3.3 FECHAMENTO DO ARQUIVO FICHA SÍNTESE POR PAÍS

            try {
                workbook.close();
            } catch (IOException e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                        "Erro ao fechar o arquivo de Ficha Síntese por País: " + fileNameFichaSintesePais,
                        e,
                        false
                );
            }

            // 5.3.4 TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR PAÍS

            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.TRANSFORMACAO.getId()),
                    "Iniciando processo de tranformação dos dados da e Ficha Síntese por País " + edicaoFichasSinteses + "...",
                    true,
                    "arrows_counterclockwise"
            );

            List<FichaSintesePaisDTO> fichasSintesePaisDTO = new ArrayList<>();
            boolean transformacaoSucesso = true;
            for (List<List<List<Object>>> fichaSintesePaisData : todasFichasSintesePaisData) {
                try {
                    FichaSintesePaisDTO dto = Transform.transformFichasSintesePais(fichaSintesePaisData);
                    fichasSintesePaisDTO.add(dto);

                    Event.registerEvent(
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            "Transformação da Ficha Síntese País concluída com sucesso!",
                            false
                    );
                } catch (Exception e) {
                    transformacaoSucesso = false;

                    Event.registerEvent(
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            "Erro ao transformar Ficha Síntese País.",
                            e,
                            false
                    );
                }
            }

            if (transformacaoSucesso) {
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Transformação de todas as planilhas da Ficha Síntese País concluída com sucesso!",
                        true,
                        "white_check_mark"
                );
                todasFichasSintesePaisData.clear();
            } else {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Houve erro(s) durante a transformação das planilhas da Ficha Síntese País.",
                        true,
                        "x"
                );
            }

            // 5.4 PROCESSAMENTO DA FICHA SÍNTESE POR ESTADO

            // 5.4.1 ABERTURA DO ARQUIVO EXCEL
            try {
                workbook = S3.readFile(fileNameFichaSinteseEstado);
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Arquivo Excel de Ficha Síntese por Estado de" + edicaoFichasSinteses + "  aberto com sucesso!",
                        false
                );
            } catch (Exception e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(),
                                EEtapa.EXTRACAO.getId()),
                        "Erro ao abrir o arquivo Excel de Ficha Síntese por Estado de" + edicaoFichasSinteses + ".",
                        e,
                        false
                );
                throw new RuntimeException(e);
            }


            // 5.4.2 EXTRAÇÃO DAS FICHAS SÍNTESE POR ESTADO
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(), EEtapa.INCIALIZACAO.getId()),
                    "Iniciando processo de extração dos dados da Ficha Síntese por Estado de " + edicaoFichasSinteses + "...",
                    "mag"
            );

            List<List<List<List<Object>>>> todasFichasSinteseEstadoData = new ArrayList<>();
            boolean extracaoEstadoSucesso = true;

            for (int indicePlanilha = 1; indicePlanilha < Service.getSheetNumber(workbook); indicePlanilha++) {
                try {
                    List<List<List<Object>>> fichaSinteseEstadoData = Extract.extractFichasSinteseEstadoData(
                            workbook,
                            fileNameFichaSinteseEstado,
                            indicePlanilha,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric")
                    );

                    todasFichasSinteseEstadoData.add(fichaSinteseEstadoData);

                    Event.registerEvent(
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                            "Extração da Ficha Síntese Estado da planilha " + indicePlanilha + " de " + edicaoFichasSinteses + " concluída com sucesso!",
                            false
                    );
                } catch (Exception e) {
                    extracaoEstadoSucesso = false;

                    Event.registerEvent(
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                            "Erro ao extrair Ficha Síntese Estado da planilha " + indicePlanilha + " de " + edicaoFichasSinteses + ".",
                            e,
                            false
                    );
                }
            }

            if (extracaoEstadoSucesso) {
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                        "Extração de todas as planilhas da Ficha Síntese Estado " + edicaoFichasSinteses + " concluída com sucesso!",
                        true,
                        "white_check_mark"
                );
            } else {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                        "Houve erro(s) durante a extração das planilhas da Ficha Síntese Estado " + edicaoFichasSinteses + ".",
                        true,
                        "x"
                );
            }

            // 5.4.3 FECHAMENTO DO ARQUIVO FICHA SÍNTESE POR ESTADO
            try {
                workbook.close();
            } catch (IOException e) {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                        "Erro ao fechar o arquivo de Ficha Síntese por Estado: " + fileNameFichaSinteseEstado,
                        e,
                        false
                );
            }

            // 5.4.4 TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR ESTADO
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Iniciando processo de transformação dos dados da Ficha Síntese por Estado " + edicaoFichasSinteses + "...",
                    true,
                    "arrows_counterclockwise"
            );

            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = new ArrayList<>();
            boolean transformacaoEstadoSucesso = true;

            for (List<List<List<Object>>> fichaSinteseEstadoData : todasFichasSinteseEstadoData) {
                try {
                    FichaSinteseEstadoDTO dto = Transform.transformFichasSinteseEstado(fichaSinteseEstadoData);
                    fichasSinteseEstadoDTO.add(dto);

                    Event.registerEvent(
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            "Transformação da Ficha Síntese Estado concluída com sucesso!",
                            false
                    );
                } catch (Exception e) {
                    transformacaoEstadoSucesso = false;

                    Event.registerEvent(
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            "Erro ao transformar Ficha Síntese Estado.",
                            e,
                            false
                    );
                }
            }

            if (transformacaoEstadoSucesso) {
                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Transformação de todas as planilhas da Ficha Síntese Estado concluída com sucesso!",
                        true,
                        "white_check_mark"
                );
                todasFichasSinteseEstadoData.clear();
            } else {
                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Houve erro(s) durante a transformação das planilhas da Ficha Síntese Estado.",
                        true,
                        "x"
                );
            }

            // 5.4.4 TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR ESTADO
            Event.registerEvent(
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Processamento das Fichas Sínteses de " + edicaoFichasSinteses + " concluída com sucesso!",
                    true,
                    "clipboard"
            );

            // 6.0 PREPARAÇÃO DOS MAPAS PARA ACESSO RÁPIDO A PAÍSES E UFs
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(), EEtapa.INCIALIZACAO.getId()),
                    "Iniciando preparação dos mapas para acesso rápido a países e UFs...",
                    false
            );

            Map<String, Integer> mapaPaises = new HashMap<>();
            Map<String, String> mapaUfs = new HashMap<>();

            boolean preparacaoMapasSucesso = true;

            try {
                paises = PaisDAO.findAll();
                List<UnidadeFederativaBrasil> ufs = UnidadeFederativaBrasilDAO.findAll();

                mapaPaises = paises.stream()
                        .collect(Collectors.toMap(p -> p.getNomePais().toLowerCase(), Pais::getIdPais));

                mapaUfs = ufs.stream()
                        .collect(Collectors.toMap(uf -> uf.getUnidadeFederativa().toLowerCase(), UnidadeFederativaBrasil::getSigla));

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.INCIALIZACAO.getId()),
                        "Mapas de países e UFs preparados com sucesso!",
                        false
                );

            } catch (Exception e) {
                preparacaoMapasSucesso = false;

                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        "Erro ao preparar mapas para acesso rápido a países e UFs.",
                        e,
                        false
                );
            }

            List<Object[]> batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<String> fkUfsDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int batchMax = 10000;

            // 7.0 PROCESSAMENTO DE PERFIS
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Iniciando cruzamento dos dados das Chegadas com os das Fichas Sínteses para transformação dos perfis de turistas e inserção no banco...",
                    true,
                    "busts_in_silhouette"
            );

            boolean processamentoPerfisSucesso = true;

            try {
                Iterator<ChegadaTuristasInternacionaisBrasilMensalDTO> iterator = chegadasTuristasInternacionaisBrasilMensalDTO.iterator();

                while (iterator.hasNext()) {
                    ChegadaTuristasInternacionaisBrasilMensalDTO chegada = iterator.next();

                    // TRANSFORMAÇÃO
                    List<PerfilDTO> perfisEstimadosTuristas = Transform.transformPerfis(
                            chegada,
                            chegadasTuristasInternacionaisBrasilMensalDTO,
                            fichasSinteseEstadoDTO,
                            fichasSintesePaisDTO,
                            fichaSinteseBrasilDTO
                    );

                    for (PerfilDTO perfil : perfisEstimadosTuristas) {
                        int fkPais = mapaPaises.getOrDefault(perfil.getPaisesOrigem().toLowerCase(), -1);
                        String fkUf = mapaUfs.getOrDefault(perfil.getEstadoEntrada().toLowerCase(), null);

                        if (fkUf == null || fkPais == -1) {
                            continue;
                        }

                        Object[] params = new Object[]{
                                fkPais,
                                fkUf,
                                perfil.getAno(),
                                perfil.getMes(),
                                perfil.getQuantidadeTuristas(),
                                perfil.getGeneroDTO(),
                                perfil.getFaixaEtariaDTO(),
                                perfil.getViaAcesso(),
                                perfil.getComposicaoGruposViagem(),
                                perfil.getFonteInformacao(),
                                perfil.getMotivo(),
                                perfil.getMotivacaoViagemLazer(),
                                perfil.getGastosMedioPerCapitaMotivo()
                        };

                        batchArgs.add(params);
                        fkPaisesDoLote.add(fkPais);
                        fkUfsDoLote.add(fkUf);

                        if (batchArgs.size() >= batchMax) {
                            Event.registerEvent(
                                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                                    batchArgs.size() + " perfis criados com sucesso!",
                                    false
                            );
                            Event.registerEvent(
                                    new Log(ELogCategoria.INFO.getId(), EEtapa.CARREGAMENTO.getId()),
                                    "Inserindo lote de perfis de turistas no banco...",
                                    false
                            );

                            try {
                                Load.loadPerfis(
                                        batchArgs,
                                        fkPaisesDoLote,
                                        fkUfsDoLote,
                                        batchFonteArgs,
                                        "Fichas Síntese " + edicaoFichasSinteses
                                );

                                Event.registerEvent(
                                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                                        "Perfis de turistas carregados com sucesso no banco.",
                                        false
                                );

                            } catch (Exception e) {
                                Event.registerEvent(
                                        new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                                        "Erro ao carregar perfis de turistas no banco.",
                                        e,
                                        false
                                );
                            }

                            DataBaseConnection.getConnection().commit();

                            batchArgs.clear();
                            fkPaisesDoLote.clear();
                            fkUfsDoLote.clear();
                            batchFonteArgs.clear();

                            Event.registerEvent(
                                    new Log(ELogCategoria.INFO.getId(), EEtapa.TRANSFORMACAO.getId()),
                                    " Gerando mais perfis de turistas...",
                                    false
                            );
                        }
                    }

                    iterator.remove();
                }

                if (!batchArgs.isEmpty()) {
                    Event.registerEvent(
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            batchArgs.size() + " perfis criados com sucesso!",
                            false
                    );
                    Event.registerEvent(
                            new Log(ELogCategoria.INFO.getId(), EEtapa.CARREGAMENTO.getId()),
                            "Inserindo últimos perfis de turistas no banco...",
                            false
                    );

                    try {
                        Load.loadPerfis(
                                batchArgs,
                                fkPaisesDoLote,
                                fkUfsDoLote,
                                batchFonteArgs,
                                "Fichas Síntese " + edicaoFichasSinteses
                        );

                        Event.registerEvent(
                                new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                                "Perfis de turistas carregados com sucesso no banco.",
                                true,
                                "white_check_mark"
                        );

                    } catch (Exception e) {
                        Event.registerEvent(
                                new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                                "Erro ao carregar perfis de turistas no banco.",
                                e,
                                true,
                                "x"
                        );
                    }

                    DataBaseConnection.getConnection().commit();

                    batchArgs.clear();
                    fkPaisesDoLote.clear();
                    fkUfsDoLote.clear();
                    batchFonteArgs.clear();
                }

                Event.registerEvent(
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Criação e inserção dos perfis finalizada com sucesso!",
                        true,
                        "white_check_mark"
                );

                DataBaseConnection.getConnection().close();

            } catch (Exception e) {
                processamentoPerfisSucesso = false;

                Event.registerEvent(
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        "Erro durante o processamento dos perfis de turistas.",
                        e,
                        true,
                        "x"
                );
            }

            // 8. FIM DO PROCESSO ETL
            Event.registerEvent(
                    new Log(ELogCategoria.INFO.getId(),
                            EEtapa.INCIALIZACAO.getId()),
                    "Fim do processo de ETL.",
                    "gear"
            );


            List<String> webhooks = ConfiguracaoSlackDAO.findWebhooksByEtapa(EEtapa.FINALIZACAO.getId());
            if (!webhooks.isEmpty()) {
                for (String webhook : webhooks) {
                    Slack.sendNotification(webhook, "Sua dashboard está atualizada! Entre e confira já! " + "tada");
                }
            }


        } catch (Exception e) {
            Event.registerEvent(
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Erro durante o processamento dos perfis de turistas.",
                    e,
                    true,
                    "x"
            );
        }
    }


    static List<Pais> listarPaisesNaoCadastrados(
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


}
