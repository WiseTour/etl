package tour.wise;

import tour.wise.etl.ETL;
import tour.wise.etl.extract.S3ExcelReader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String bucketName = "s3-lab-phelipe";
        String caminhoChegadaTurista2019 = "chegadas_2019.xlsx";
        String caminhoArquivoFichaSinteseBrasil = "01 - Ficha Síntese Brasil - 2015-2019_DIVULGAÇÃO.xlsx";
        String caminhoArquivoFichaSintesePaises = "05 - Ficha Síntese Países 2015-2019_DIVULGAÇÃO.xlsx";
        String caminhoArquivoFichaSinteseEstado = "06 - Ficha Síntese UF 2015-2019_DIVULGAÇÃO.xlsx";

        /*String caminhoFichaSintesePaises = "demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/05 - Ficha Síntese Países 2015-2019_DIVULGAÇÃO.xlsx";
        String caminhoFichaSinteseBrasil = "demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/01 - Ficha Síntese Brasil - 2015-2019_DIVULGAÇÃO.xlsx";
        String caminhoFichaSinteseEstado = "demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/06 - Ficha Síntese UF 2015-2019_DIVULGAÇÃO.xlsx";*/

        S3ExcelReader reader = new S3ExcelReader(bucketName);
        reader.lerExcelDireto(caminhoChegadaTurista2019);
        reader.lerExcelDireto(caminhoArquivoFichaSinteseBrasil);;
        reader.lerExcelDireto(caminhoArquivoFichaSintesePaises);;
        reader.lerExcelDireto(caminhoArquivoFichaSinteseEstado);
    

        ETL etl = new ETL(bucketName, caminhoChegadaTurista2019, caminhoArquivoFichaSinteseBrasil, caminhoArquivoFichaSintesePaises, caminhoArquivoFichaSinteseEstado);

        etl.exe(
                caminhoChegadaTurista2019,
                "Chegadas 2019",
                "https://www.gov.br/turismo/pt-br/acesso-a-informacao/acoes-e-programas/observatorio/demanda-turistica/demanda-turistica-internacional-1)",
                "Ministério do Turismo",
                "2019",
                caminhoArquivoFichaSintesePaises,
                caminhoArquivoFichaSinteseBrasil,
                caminhoArquivoFichaSinteseEstado,
                "Fichas Síntese 2019",
                "https://dados.gov.br/dados/conjuntos-dados/estimativas-de-chegadas-de-turistas-internacionais-ao-brasil",
                "Ministério do Turismo",
                "2019"
        );


        /*
        ETL etl = new ETL();

        etl.exe(
                "/home/phelipe-bruione/Desktop/ProjetoPi/2sem/database/data/chegada_turistas_ministerio_turismo/chegadas_2019.xlsx",
                "Chegadas 2019",
                "https://www.gov.br/turismo/pt-br/acesso-a-informacao/acoes-e-programas/observatorio/demanda-turistica/demanda-turistica-internacional-1)",
                "Ministério do Turismo",
                "2019",
                "/home/phelipe-bruione/Desktop/ProjetoPi/2sem/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/05 - Ficha Síntese Países 2015-2019_DIVULGAÇÃO.xlsx",
                "/home/phelipe-bruione/Desktop/ProjetoPi/2sem/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/01 - Ficha Síntese Brasil - 2015-2019_DIVULGAÇÃO.xlsx",
                "/home/phelipe-bruione/Desktop/ProjetoPi/2sem/database/data/demanda-turistica-internacional/demanda_turistica_internacional_-_fichas_sinteses_2015-2019/06 - Ficha Síntese UF 2015-2019_DIVULGAÇÃO.xlsx",
                "Fichas Síntese 2019",
                "https://dados.gov.br/dados/conjuntos-dados/estimativas-de-chegadas-de-turistas-internacionais-ao-brasil",
                "Ministério do Turismo",
                "2019"
        );
         */

    }
}
