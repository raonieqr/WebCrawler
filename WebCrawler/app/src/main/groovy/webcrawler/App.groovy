package webcrawler

import groovyx.net.http.HttpBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

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

            CompletableFuture future = HttpBuilder.configure {
                request.uri = 'https://www.gov.br/ans/pt-br/assuntos/prestadores/padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss'
            }.getAsync()

            Object result = future.get()
            Document doc = Jsoup.parse(result.toString())

            Elements rows = doc.select("table tr:gt(0)")

            for (Element row : rows) {
                Elements columns = row.select("td")

                if (columns.size() >= 3) {
                    String competence = columns.get(0).text()
                    String publication = columns.get(1).text()
                    String startTerm = columns.get(2).text()

                    int competenceSize = competence.length()

                    if (competenceSize >= 4) {
                        String lastCharacters = competence
                                .substring(competenceSize - 4)
                        int year = Integer.parseInt(lastCharacters)
                        if (year >= 2016) {
                            File file = new File(dirPath.toString(),
                                    "historico_versoes_componentes.txt")
                            file.withWriterAppend { writer ->
                                writer.writeLine("Competência: $competence")
                                writer.writeLine("Publicação: $publication")
                                writer.writeLine("Início de Vigência: $startTerm")
                                writer.writeLine("")
                            }

                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
