package tour.wise;


import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.DataBase;
import tour.wise.etl.PerfilEstimadoTuristasETL;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        DataBase data_base = new DataBase();


        JdbcTemplate connection = data_base.getConnection();

        PerfilEstimadoTuristasETL relatorioTurismoBrasilEtl = new PerfilEstimadoTuristasETL();
        relatorioTurismoBrasilEtl.exe(
                "https://www.gov.br/turismo/pt-br/acesso-a-informacao/acoes-e-programas/observatorio/demanda-turistica/demanda-turistica-internacional-1)",
                "https://dados.gov.br/dados/conjuntos-dados/estimativas-de-chegadas-de-turistas-internacionais-ao-brasil",
                "../database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/05 - Ficha Síntese Países 2015-2019_DIVULGAÇÃO.xlsx",
                "../database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/01 - Ficha Síntese Brasil - 2015-2019_DIVULGAÇÃO.xlsx",
                "../database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/06 - Ficha Síntese UF 2015-2019_DIVULGAÇÃO.xlsx",
                "../database/data/chegada_turistas_ministerio_turismo/chegadas_2019.xlsx"
        );



    }
}
