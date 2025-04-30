package tour.wise;


import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.Data_Base;
import tour.wise.etl.Perfil_Estimado_Turistas_ETL;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        Data_Base data_base = new Data_Base();


        JdbcTemplate connection = data_base.getConnection();

        Perfil_Estimado_Turistas_ETL relatorioTurismoBrasilEtl = new Perfil_Estimado_Turistas_ETL();
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
