package webcrawler

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
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
import java.util.regex.Matcher

class App {
    static void main(String[] args) {
        try {
            Path currentDirectory = Paths.get(".").toAbsolutePath()
            Path dirPath = currentDirectory.resolve("Download")
            if (!Files.exists(dirPath))
                Files.createDirectory(dirPath)

//            String urlHistoricVersion =
//                    "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
//                            "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
//                            "padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss"
//
//            Object response = HttpRequest.getResponse(urlHistoricVersion)
//
//            HtmlTool.extractAndSaveTable(response, dirPath)

            String urlComponentes =
                    "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                        "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                        "padrao-tiss-2013-julho-2023"

            Object response = HttpRequest.getResponse(urlComponentes)
            Document doc = Jsoup.parse(response.toString())
            Elements anchors = doc.select("table a")

            ArrayList<String> linksComponents = new ArrayList<>()
            anchors.each {link ->
                linksComponents.add(link.attr("href"))
            }

            String regexPattern = /([A-Z])\w+(\.[a-zA-Z]+)?/
            linksComponents.each { link ->
                Matcher matcher = (link =~ regexPattern)
                if (matcher) {
                    String fileName = matcher[0][0]
                    String fileNameDir = fileName.replaceAll(/Padr.+TISS_?/, "")
                    .replaceAll(/_?[0-9]+(.zip|.pdf)/, "")
                    .replaceAll("_", "")
                    File dir = new File(dirPath.toString(), fileNameDir)
                    dir.mkdirs()

                    File file = HttpBuilder.configure {
                        request.uri = link
                    }.get {
                        Download.toFile(delegate, new File(dir, fileName))
                    }
                    println(fileName)
                }
            }

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
