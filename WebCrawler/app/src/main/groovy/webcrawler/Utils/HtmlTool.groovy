package webcrawler.Utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.nio.file.Path

class HtmlTool {
    static void extractAndSaveTable(Object response, Path dirPath) {
        Document doc = Jsoup.parse(response.toString())

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
    }
}
