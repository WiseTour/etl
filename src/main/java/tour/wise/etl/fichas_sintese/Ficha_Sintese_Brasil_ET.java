package tour.wise.etl.fichas_sintese;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.dto.ficha_sintese_brasil.*;
import tour.wise.service.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tour.wise.service.Service.loadWorkbook;

public class Ficha_Sintese_Brasil_ET {

    Service service = new Service();
    Workbook workbook;

    public List<Ficha_Sintese_Brasil> extractTransform( String fileName, Integer startCollun, Integer endCollun) throws IOException {
        ZipSecureFile.setMinInflateRatio(0.0001);

        workbook = loadWorkbook(fileName);

        List<List<List<List<Object>>>> data = new ArrayList<>();
        for(Integer j = startCollun; j <= endCollun; j++){
            data.add(extract(
                    workbook,
                    1,
                    List.of(1, 3+j),
                    List.of(10, 12+j),
                    List.of("string", "numeric")));

        }

        workbook.close();

        // TRANSFORM

        List<Ficha_Sintese_Brasil> fichas_sintese_brasil = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichas_sintese_brasil.add(transform(datum));
            System.out.println(datum);
        }

        return fichas_sintese_brasil;

    }


    public List<List<List<Object>>> extract(Workbook workbook, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5}, // ano
                new int[]{7, 9}, // motivo
                new int[]{11, 16}, // motivação viagem lazer
                new int[]{28, 32}, // composição do grupo turístico
                new int[]{34, 36}, // Gasto médio per capita dia no Brasil
                new int[]{39, 41}, // Permanência média no Brasil
                new int[]{45, 49}, // Destinos mais visitados a lazer
                new int[]{51, 55}, // Destinos mais visitados a negócios, eventos e convenções
                new int[]{57, 61}, // Destinos mais visitados utros motivos
                new int[]{64, 71}, // Fonte de informação
                new int[]{73, 75} // Utilização de agência de viagem
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of("Brasil")));

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            leftColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }

        // Parâmetros das seções a serem lidas
        ranges = List.of(
                new int[]{23, 24}, // Gênero
                new int[]{26, 31} // faixa etária
        );

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            rightColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }


        for (List<List<Object>> datum : data) {
            System.out.println(datum);
        }

        System.out.println("\nETL finalizado com sucesso.");

        return data;
    }

    public Ficha_Sintese_Brasil transform(List<List<List<Object>>> data) {
        return new Ficha_Sintese_Brasil(
                transformAno(data, 1),
                transformGenero(data, 12),
                transformFaixaEtaria(data, 13),
                transformComposicoesGrupo(data, 4),
                transformFontesInformacao(data, 10),
                transformUtilizacaoAgenciaViagem(data, 11),
                transformMotivosViagem(data, 2),
                transformMotivacaoViagemLazer(data, 3),
                transformGastosMedioMotivo(data, 5),
                transformPermanenciaMediaMotivo(data, 6),
                transformDestinosMaisVisitadosPorMotivo(data, 7)


        );
    }

    protected Integer transformAno(List<List<List<Object>>> data, Integer index) {
        return service.parseToInteger(data.get(index).getFirst().get(1).toString());
    }

    protected List<Genero> transformGenero(List<List<List<Object>>> data, Integer index) {
        List<Genero> genero = new ArrayList<>();


        for (int i = 0; i < 2; i++) {
            genero.add(
                    new Genero(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return genero;
    }

    protected List<Faixa_Etaria> transformFaixaEtaria(List<List<List<Object>>> data, Integer index) {
        List<Faixa_Etaria> faixa_etaria = new ArrayList<>();


        for (int i = 0; i < 6; i++) {
            faixa_etaria.add(
                    new Faixa_Etaria(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return faixa_etaria;
    }


    protected List<Motivo_Viagem> transformMotivosViagem(List<List<List<Object>>> data, Integer index) {
        List<Motivo_Viagem> motivos = new ArrayList<>();


        for (int i = 0; i < 3; i++) {
            motivos.add(
                    new Motivo_Viagem(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return motivos;
    }



    protected List<Motivacao_Viagem_Lazer> transformMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
        List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            motivacoes_viagem_lazer.add(
                    new Motivacao_Viagem_Lazer(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )

            );
        }

        return motivacoes_viagem_lazer;
    }


    protected List<Composicao_Grupo_Viagem> transformComposicoesGrupo(List<List<List<Object>>> data, Integer index) {
        return data.get(index).stream()
                .map(this::createComposicaoGrupo)
                .collect(Collectors.toList());
    }

    protected Composicao_Grupo_Viagem createComposicaoGrupo(List<Object> values) {
        return new Composicao_Grupo_Viagem(
                values.get(0).toString(),
                Double.parseDouble(values.get(1).toString())
        );
    }

    protected List<Gasto_Medio_Per_Capita_Brasil_Motivo> transformGastosMedioMotivo(
            List<List<List<Object>>> data,
            Integer index
    ) {
        return data.get(index).stream()
                .map(entry -> new Gasto_Medio_Per_Capita_Brasil_Motivo(
                        entry.get(0).toString(),
                        Double.parseDouble(entry.get(1).toString())))
                .collect(Collectors.toList());
    }

    protected List<Permanencia_Media> transformPermanenciaMediaMotivo(
            List<List<List<Object>>> data,
            Integer index
    ) {
        return data.get(index).stream()
                .map(entry -> new Permanencia_Media(
                        entry.get(0).toString(),
                        Double.parseDouble(entry.get(1).toString())))
                .collect(Collectors.toList());
    }


    protected List<Destinos_Mais_Visitados_Por_Motivo> transformDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
        String[] motivos = {
                "Lazer",
                "Negócios, eventos e convenções",
                "Outros motivos"
        };

        List<Destinos_Mais_Visitados_Por_Motivo> destinosPorMotivo = new ArrayList<>();

        for (int i = 0; i < motivos.length; i++) {
            destinosPorMotivo.add(
                    new Destinos_Mais_Visitados_Por_Motivo(
                            motivos[i],
                            createDestinos(data.get(index + i))
                    )
            );
        }

        return destinosPorMotivo;
    }

    protected List<Destino_Mais_Visitado> createDestinos(List<List<Object>> destinoData) {
        return destinoData.stream()
                .map(entry -> new Destino_Mais_Visitado(
                        entry.get(0).toString().split(" - ")[1],
                        Double.parseDouble(entry.get(1).toString())
                ))
                .collect(Collectors.toList());
    }


    protected List<Fonte_Informacao> transformFontesInformacao(List<List<List<Object>>> data, int index) {
        return data.get(index).stream()
                .map(entry -> new Fonte_Informacao(
                        entry.get(0).toString(),
                        Double.parseDouble(entry.get(1).toString())))
                .collect(Collectors.toList());
    }

    protected List<Utilizacao_Agencia_Viagem> transformUtilizacaoAgenciaViagem(List<List<List<Object>>> data, int index) {
        return data.get(index).stream()
                .map(entry -> new Utilizacao_Agencia_Viagem(
                        entry.get(0).toString(),
                        Double.parseDouble(entry.get(1).toString())))
                .collect(Collectors.toList());
    }




}
