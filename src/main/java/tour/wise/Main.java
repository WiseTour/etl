package tour.wise;


import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.Data_Base;
import tour.wise.dao.Fonte_DAO;
import tour.wise.dao.Pais_DAO;
import tour.wise.dao.Unidade_Federativa_Brasil_DAO;
import tour.wise.etl.Chegada_Turistas_Internacionais_Brasil_Mensal_ETL;

import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {

        Data_Base data_base = new Data_Base();

        JdbcTemplate connection = data_base.getConnection();


//        Perfil_Estimado_Turistas_ETL relatorioTurismoBrasilEtl = new Perfil_Estimado_Turistas_ETL();
//
//        List<Perfil_Estimado_Turistas> perfies = relatorioTurismoBrasilEtl.extractTransform("Ministerio do Turismo","Ministerio do Turismo");
//
//        Integer totalChegadas = 100000000;
//
//        Integer total_chegadas = 0;
//
//        for (Perfil_Estimado_Turistas perfil : perfies) {
//
//            total_chegadas += perfil.getTotal_chegadas();
//            System.out.println();
//            System.out.println(perfil);
//        }
//
//        System.out.println();
//        System.out.println("Total de perfies = " + perfies.size());
//        System.out.println();
//        System.out.println("Total de chegadas = " + totalChegadas);
//        System.out.println();
//        System.out.println("Total de chegadas para os estados da lista = " + total_chegadas);
//        System.out.println();
//        System.out.println("Total de chegadas para de outros estados = " + (totalChegadas - total_chegadas));



        Chegada_Turistas_Internacionais_Brasil_Mensal_ETL chegada_turistas_internacionais_brasil_Mensal_etl = new Chegada_Turistas_Internacionais_Brasil_Mensal_ETL();

        chegada_turistas_internacionais_brasil_Mensal_etl.extractTransformLoad(
                "../../database/dados-originais/chegada_turistas_ministerio_turismo/chegadas_2019.xlsx",
                0,
                0,
                12,
                List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric"),
                "Ministério do Turismo",
                "2019",
                "2019",
                "https://dados.gov.br/dados/conjuntos-dados/estimativas-de-chegadas-de-turistas-internacionais-ao-brasil"
                ,connection

        );







    }
}
