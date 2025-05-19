package tour.wise;

import tour.wise.etl.ETL;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ETL etl = new ETL();

        etl.exe(
                "C:/Users/leosa/OneDrive/Área de Trabalho/WiseTour/database/data/chegada_turistas_ministerio_turismo/chegadas_2019.xlsx",
                "Chegadas 2019",
                "https://www.gov.br/turismo/pt-br/acesso-a-informacao/acoes-e-programas/observatorio/demanda-turistica/demanda-turistica-internacional-1)",
                "Ministério do Turismo",
                "2019",
                "C:/Users/leosa/OneDrive/Área de Trabalho/WiseTour/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/05 - Ficha Síntese Países 2015-2019_DIVULGAÇÃO.xlsx",
                "C:/Users/leosa/OneDrive/Área de Trabalho/WiseTour/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/01 - Ficha Síntese Brasil - 2015-2019_DIVULGAÇÃO.xlsx",
                "C:/Users/leosa/OneDrive/Área de Trabalho/WiseTour/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/06 - Ficha Síntese UF 2015-2019_DIVULGAÇÃO.xlsx",
                "Fichas Síntese 2019",
                "https://dados.gov.br/dados/conjuntos-dados/estimativas-de-chegadas-de-turistas-internacionais-ao-brasil",
                "Ministério do Turismo",
                "2019"
        );

    }


}
