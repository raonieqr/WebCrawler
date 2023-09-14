package webcrawler

import webcrawler.Utils.HtmlTool
import webcrawler.Utils.HttpRequest

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class App {
    static void main(String[] args) {
        try {
            Path currentDirectory = Paths.get(".").toAbsolutePath()
            Path dirPath = currentDirectory.resolve("Download")
            if (!Files.exists(dirPath))
                Files.createDirectory(dirPath)

            String urlHistoricVersion =
                    "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                            "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                            "padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss"

            String urlComponents =
                    "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                        "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                        "padrao-tiss-2013-julho-2023"

            String urlTableAns = "https://www.gov.br/ans/pt-br/assuntos/" +
                "prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                    "padrao-tiss-tabelas-relacionadas"


            Object responseHistoric = HttpRequest
                    .getResponse(urlHistoricVersion)
            HtmlTool.extractAndSaveTable(responseHistoric, dirPath)

            Object responseComponents = HttpRequest.getResponse(urlComponents)
            HtmlTool.downloadComponents(responseComponents, dirPath)

            Object responseTableAns = HttpRequest.getResponse(urlTableAns)
            HtmlTool.downloadTableAns(dirPath, responseTableAns)

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
