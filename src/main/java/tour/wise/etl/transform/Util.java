package tour.wise.etl.transform;

import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.ListaDestinosDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.Service;
import tour.wise.etl.extract.S3ExcelReader;

import java.util.*;
import java.util.stream.Collectors;


public class Util {
    S3ExcelReader leitor;
    Service service = new Service(leitor);

    public  Integer transformAno(List<List<List<Object>>> data, Integer index) {
        // Apenas acessa e converte o valor necessário
        return service.parseToInteger(data.get(index).get(0).get(1).toString());
    }

    public  List<GeneroDTO> transformListGenero(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<GeneroDTO> generoDTO = new ArrayList<>(2);

        // Itera diretamente sobre os índices conhecidos (0 e 1)
        for (int i = 0; i < 2; i++) {
            generoDTO.add(
                    new GeneroDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return generoDTO;
    }

    public  List<FaixaEtariaDTO> transformListFaixaEtaria(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<FaixaEtariaDTO> faixa_etariaDTO = new ArrayList<>(6);

        // Itera sobre os 6 primeiros elementos conhecidos
        for (int i = 0; i < 6; i++) {
            faixa_etariaDTO.add(
                    new FaixaEtariaDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return faixa_etariaDTO;
    }

    public List<MotivoViagemDTO> transformListMotivosViagem(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<MotivoViagemDTO> motivos = new ArrayList<>(3);

        // Itera sobre os 3 primeiros elementos conhecidos
        for (int i = 0; i < 3; i++) {
            motivos.add(
                    new MotivoViagemDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return motivos;
    }

    public List<MotivacaoViagemLazerDTO> transformListMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<MotivacaoViagemLazerDTO> motivacoes_viagem_lazer = new ArrayList<>(6);

        // Itera sobre os 6 primeiros elementos conhecidos
        for (int i = 0; i <= 5; i++) {
            motivacoes_viagem_lazer.add(
                    new MotivacaoViagemLazerDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return motivacoes_viagem_lazer;
    }

    public  List<ComposicaoGrupoViagemDTO> transformListComposicoesGrupo(List<List<List<Object>>> composicoesGrupoData, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<ComposicaoGrupoViagemDTO> composicoes = new ArrayList<>(composicoesGrupoData.get(index).size());

        // Itera diretamente sobre os dados
        for (List<Object> composicaoGrupoData : composicoesGrupoData.get(index)) {
            composicoes.add(createComposicaoGrupo(composicaoGrupoData));
        }

        return composicoes;
    }

    public ComposicaoGrupoViagemDTO createComposicaoGrupo(List<Object> values) {
        // Cria o DTO diretamente a partir dos valores fornecidos
        return new ComposicaoGrupoViagemDTO(
                values.get(0).toString(),
                Double.parseDouble(values.get(1).toString())
        );
    }

    public List<GastoMedioPerCapitaMotivoDTO> transformListGastosMedioMotivo(List<List<List<Object>>> gastosMedioMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<GastoMedioPerCapitaMotivoDTO> gastos = new ArrayList<>(gastosMedioMotivoData.get(index).size());

        for (List<Object> gastoMedioMotivoData : gastosMedioMotivoData.get(index)) {
            String motivo = gastoMedioMotivoData.get(0).toString();
            double valor = Double.parseDouble(gastoMedioMotivoData.get(1).toString());
            gastos.add(new GastoMedioPerCapitaMotivoDTO(motivo, valor));
        }

        return gastos;
    }

    public List<PermanenciaMediaDTO> transformListPermanenciaMediaMotivo(List<List<List<Object>>> permanenciasMediaMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<PermanenciaMediaDTO> permanencias = new ArrayList<>(permanenciasMediaMotivoData.get(index).size());

        for (List<Object> permanenciaMediaMotivoData : permanenciasMediaMotivoData.get(index)) {
            String motivo = permanenciaMediaMotivoData.get(0).toString();
            double valor = Double.parseDouble(permanenciaMediaMotivoData.get(1).toString());
            permanencias.add(new PermanenciaMediaDTO(motivo, valor));
        }

        return permanencias;
    }

    public List<FonteInformacaoDTO> transformListFontesInformacao(List<List<List<Object>>> fontesInformacaoData, int index) {
        List<FonteInformacaoDTO> fontes = new ArrayList<>(fontesInformacaoData.get(index).size());

        for (List<Object> fonteInformacaoData : fontesInformacaoData.get(index)) {
            String descricao = fonteInformacaoData.get(0).toString();
            double valor = Double.parseDouble(fonteInformacaoData.get(1).toString());

            fontes.add(new FonteInformacaoDTO(descricao, valor));
        }

        return fontes;
    }

    public List<UtilizacaoAgenciaViagemDTO> transformListUtilizacaoAgenciaViagem(List<List<List<Object>>> utilizacoesAgenciaViagemData, int index) {
        List<UtilizacaoAgenciaViagemDTO> agencias = new ArrayList<>(utilizacoesAgenciaViagemData.get(index).size());

        for (List<Object> utilizacaoAgenciaViagemData : utilizacoesAgenciaViagemData.get(index)) {
            String descricao = utilizacaoAgenciaViagemData.get(0).toString();
            String valorStr = utilizacaoAgenciaViagemData.get(1).toString().trim();

            Double valor = valorStr.isEmpty() ? 0.0 : Double.parseDouble(valorStr);
            // double valor = Double.parseDouble(utilizacaoAgenciaViagemData.get(1).toString());
            agencias.add(new UtilizacaoAgenciaViagemDTO(descricao, valor));
        }

        return agencias;
    }

    public List<DestinosMaisVisitadosPorMotivoDTO> transformListDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
        // Definir os motivos diretamente
        String[] motivos = { "Lazer", "Negócios, eventos e convenções", "Outros motivos" };

        // Inicializa a lista de DestinosMaisVisitadosPorMotivoDTO
        List<DestinosMaisVisitadosPorMotivoDTO> destinosPorMotivo = new ArrayList<>(motivos.length);

        // Processa cada motivo e cria o DTO correspondente
        for (int i = 0; i < motivos.length; i++) {
            destinosPorMotivo.add(
                    new DestinosMaisVisitadosPorMotivoDTO(
                            motivos[i],
                            createListDestinosMaisVisitados(data.get(index + i))
                    )
            );
        }

        return destinosPorMotivo;
    }

    public List<DestinoMaisVisitadoDTO> createListDestinosMaisVisitados(List<List<Object>> destinosData) {
        // Usamos um mapa para armazenar destinos e suas porcentagens, evitando a duplicação
        Map<String, Double> destinosMap = new HashMap<>();

        // Processa cada destino e adiciona/atualiza o valor no mapa
        for (List<Object> destinoData : destinosData) {
            if (destinoData.size() < 2)
            {
                System.out.println("Linha com dados incompletos: "+ destinoData);
                continue;
            }
            String[] partes = destinoData.get(0).toString().split(" - ");
            if (partes.length < 2)
            {
                System.out.println("Formato inesperado: "+ destinoData.get(0));
                continue;
            }

            String nomeDestino = partes[1];
            String valorStr = destinoData.get(1).toString().trim();
            double valor = valorStr.isEmpty() ? 0.0: Double.parseDouble(valorStr);
            // String nomeDestino = destinoData.get(0).toString().split(" - ")[1];
            // double valor = Double.parseDouble(destinoData.get(1).toString());

            destinosMap.merge(nomeDestino, valor, Double::sum);
        }

        // Cria a lista de DTOs a partir do mapa
        List<DestinoMaisVisitadoDTO> destinos = new ArrayList<>(destinosMap.size());
        for (Map.Entry<String, Double> entry : destinosMap.entrySet()) {
            destinos.add(new DestinoMaisVisitadoDTO(entry.getKey(), entry.getValue()));
        }

        // Calcula o total e adiciona "Outros estados" se necessário
        double total = destinos.stream().mapToDouble(DestinoMaisVisitadoDTO::getPorcentagem).sum();
        double outrosValor = 100.0 - total;
        if (outrosValor > 0) {
            destinos.add(new DestinoMaisVisitadoDTO("Outros estados", outrosValor));
        }

        return destinos;
    }

    public String extractNomePais(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }

    public String trasnformEstado(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }

    public List<PaisOrigemDTO> trasnformListPaisesOrigem(List<List<List<Object>>> data, int index) {
        return data.get(index).stream()
                .map(this::createPaisOrigem)
                .collect(Collectors.toList());
    }

    public PaisOrigemDTO createPaisOrigem(List<Object> row) {
        return new PaisOrigemDTO(
                row.get(0).toString(),
                Double.parseDouble(row.get(1).toString())
        );
    }

    public List<PerfilDTO>createPerfisCombinations(FichaSinteseBrasilDTO fichaSinteseBrasilDTO){
        List<PerfilDTO> perfisFichaSinteseBrasilDTO = new ArrayList<>();

        for (GeneroDTO generoDTO : fichaSinteseBrasilDTO.getGeneroDTO()) {

            for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseBrasilDTO.getFaixaEtariaDTO()) {

                for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseBrasilDTO.getUtilizacaoAgenciaViagemDTO()) {

                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseBrasilDTO.getComposicaoGruposViagem()) {

                        for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseBrasilDTO.getFontesInformacao()) {

                            for (MotivoViagemDTO motivoDTO : fichaSinteseBrasilDTO.getMotivos()) {

                                DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO = fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo().stream()
                                        .filter(d -> d.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                        .findFirst()
                                        .orElse(null);

                                List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosDTOCombinations = createDestinosMaisVisitadosCombinations(destinosMaisVisitadosPorMotivoDTO);

                                for (List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination : destinosMaisVisitadosDTOCombinations) {

                                    GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO = fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo().stream()
                                            .filter(g -> g.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                            .findFirst()
                                            .orElse(null);

                                    PermanenciaMediaDTO permanenciaMediaMotivoDTO = fichaSinteseBrasilDTO.getPermanenciaMediaDTO().stream()
                                            .filter(p -> p.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                            .findFirst()
                                            .orElse(null);

                                    List<MotivacaoViagemLazerDTO> motivacoesLazer = "Lazer".equalsIgnoreCase(motivoDTO.getMotivo())
                                            ? fichaSinteseBrasilDTO.getMotivacoesViagemLazer()
                                            : Collections.emptyList();

                                    ListaDestinosDTO listaDestinosDTO = createListaDestinosDTO(destinosMaisVisitadosDTOCombination, permanenciaMediaMotivoDTO.getDias());
                                    Double taxaTuristasDestino = (destinosMaisVisitadosDTOCombination.getFirst().getPorcentagem() > 0) ? destinosMaisVisitadosDTOCombination.getFirst().getPorcentagem() : 100/fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo().size();
                                    String genero = (generoDTO.getPorcentagem() > 0) ? generoDTO.getGenero() : null;
                                    Double gerenoPorcentagem = (generoDTO.getPorcentagem() > 0) ? generoDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getGeneroDTO().size();
                                    String faixaEtaria = (faixaEtariaDTO.getPorcentagem() > 0) ? faixaEtariaDTO.getFaixa_etaria() : null;
                                    Double faixaEtariaPorcentagem = (faixaEtariaDTO.getPorcentagem() > 0) ? faixaEtariaDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getFaixaEtariaDTO().size();
                                    String utilizacaoAgenciaViagem = (utilizacaoAgenciaViagemDTO.getPorcentagem() > 0) ? utilizacaoAgenciaViagemDTO.getTipo() : null;
                                    Double utilizacaoAgenciaViagemPorcentagem = (utilizacaoAgenciaViagemDTO.getPorcentagem() > 0) ? utilizacaoAgenciaViagemDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getUtilizacaoAgenciaViagemDTO().size();
                                    String composicaogrupoViagem =  (composicaoGrupoViagemDTO.getPorcentagem() > 0) ? composicaoGrupoViagemDTO.getComposicao() : null;
                                    Double composicaogrupoViagemPorcentagem = (composicaoGrupoViagemDTO.getPorcentagem() > 0) ? composicaoGrupoViagemDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getComposicaoGruposViagem().size();
                                    String fonteInformacao = (fonteInformacaoDTO.getPorcentagem() > 0) ? fonteInformacaoDTO.getFonte() : null;
                                    Double fonteInformacaoPorcentagem = (fonteInformacaoDTO.getPorcentagem() > 0) ? fonteInformacaoDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getFontesInformacao().size();
                                    String motivo = (motivoDTO.getPorcentagem() > 0) ? motivoDTO.getMotivo() : null;
                                    Double motivoPorcentagem = (motivoDTO.getPorcentagem() > 0) ? motivoDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getMotivos().size();

                                    Double taxaTuristas = gerenoPorcentagem/100 *
                                            faixaEtariaPorcentagem/100 *
                                            utilizacaoAgenciaViagemPorcentagem/100 *
                                            fonteInformacaoPorcentagem/100 *
                                            composicaogrupoViagemPorcentagem/100 *
                                            motivoPorcentagem/100 *
                                            taxaTuristasDestino/100;

                                    if(!motivacoesLazer.isEmpty()){

                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSinteseBrasilDTO.getMotivacoesViagemLazer()) {

                                            String motivacaoViagemLazer = (motivacaoViagemLazerDTO.getPorcentagem() > 0) ? motivacaoViagemLazerDTO.getMotivacao() : null;
                                            Double motivacaoViagemLazerPorcentagem = (motivacaoViagemLazerDTO.getPorcentagem() > 0) ? motivacaoViagemLazerDTO.getPorcentagem() : 100/fichaSinteseBrasilDTO.getMotivacoesViagemLazer().size();

                                            taxaTuristas *= motivacaoViagemLazerPorcentagem/100;

                                            perfisFichaSinteseBrasilDTO.add(
                                                    new PerfilDTO(
                                                            taxaTuristas,
                                                            fichaSinteseBrasilDTO.getAno(),
                                                            genero,
                                                            faixaEtaria,
                                                            composicaogrupoViagem,
                                                            fonteInformacao,
                                                            utilizacaoAgenciaViagem,
                                                            motivo,
                                                            motivacaoViagemLazer,
                                                            gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                            listaDestinosDTO

                                                    )
                                            );

                                        }

                                    }else{

                                        perfisFichaSinteseBrasilDTO.add(
                                                new PerfilDTO(
                                                        taxaTuristas,
                                                        fichaSinteseBrasilDTO.getAno(),
                                                        genero,
                                                        faixaEtaria,
                                                        composicaogrupoViagem,
                                                        fonteInformacao,
                                                        utilizacaoAgenciaViagem,
                                                        motivo,
                                                        null,
                                                        gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                        listaDestinosDTO

                                                )
                                        );

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return perfisFichaSinteseBrasilDTO;

    };

    public ListaDestinosDTO createListaDestinosDTO(List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination, Double permanenciaMediaMotivoDTO){
        List<DestinoDTO> destinosDTO = new ArrayList<>();

        for (DestinoMaisVisitadoDTO destinoMaisVisitadoDTO : destinosMaisVisitadosDTOCombination) {

            destinosDTO.add(new DestinoDTO(destinoMaisVisitadoDTO.getDestino()));

        }

        return new ListaDestinosDTO(destinosDTO, permanenciaMediaMotivoDTO);
    }

    public List<List<DestinoMaisVisitadoDTO>> createDestinosMaisVisitadosCombinations(
            DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO) {

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations = new ArrayList<>();

        cretateConbinations(destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado(), destinosMaisVisitadosCombinations);

        return destinosMaisVisitadosCombinations;
    }

    public void cretateConbinations(
            List<DestinoMaisVisitadoDTO> destinosMaisVisitados,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations) {

        int totalDestinos = destinosMaisVisitados.size();

        for (int tamanhoCombinacao = 1; tamanhoCombinacao <= totalDestinos; tamanhoCombinacao++) {
            combineDestinos(destinosMaisVisitados, destinosMaisVisitadosCombinations, tamanhoCombinacao, new ArrayList<>(), 0);
        }
    }

    public void combineDestinos(
            List<DestinoMaisVisitadoDTO> destinosMaisVisitados,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations,
            int tamanhoCombinacao,
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            int inicio) {

        if (combinacaoAtual.size() == tamanhoCombinacao) {
            adicionarCombinacaoComPorcentagem(combinacaoAtual, destinosMaisVisitadosCombinations);
            return;
        }

        for (int i = inicio; i < destinosMaisVisitados.size(); i++) {
            combinacaoAtual.add(destinosMaisVisitados.get(i));
            combineDestinos(destinosMaisVisitados, destinosMaisVisitadosCombinations, tamanhoCombinacao, combinacaoAtual, i + 1);
            combinacaoAtual.remove(combinacaoAtual.size() - 1);
        }
    }

    public void adicionarCombinacaoComPorcentagem(
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations) {

        // Calcula a porcentagem acumulada fora do lambda
        double porcentagemAcumulada = 1.0;
        for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
            porcentagemAcumulada *= d.getPorcentagem() / 100.0;
        }

        // Cria nova lista de destinos com a mesma porcentagem acumulada
        List<DestinoMaisVisitadoDTO> novaCombinacao = new ArrayList<>();
        for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
            novaCombinacao.add(new DestinoMaisVisitadoDTO(d.getDestino(), porcentagemAcumulada * 100));
        }

        destinosMaisVisitadosCombinations.add(novaCombinacao);
    }




}
