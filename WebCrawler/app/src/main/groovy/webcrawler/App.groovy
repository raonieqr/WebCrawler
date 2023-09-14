package webcrawler

import groovyx.net.http.HttpBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import webcrawler.Utils.HtmlTool
import webcrawler.Utils.HttpRequest

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
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

            Object result = HttpRequest.getResponse(urlHistoricVersion)

            HtmlTool.extractAndSaveTable(result, dirPath)

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
