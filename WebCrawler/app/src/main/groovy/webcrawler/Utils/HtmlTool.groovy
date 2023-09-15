package webcrawler.Utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.nio.file.Path
import java.util.regex.Matcher

class HtmlTool {

    static void extractAndSaveTable(Object response, Path dirPath) {
        Document doc = Jsoup.parse(response.toString())
        Elements rows = doc.select('table tr:gt(0)')

        println('Generating file: historico_versoes_componentes.txt')

        for (Element row : rows) {
            Elements columns = row.select('td')

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
                                'historico_versoes_componentes.txt')

                        file.withWriterAppend { writer ->
                            writer.writeLine("Competência: $competence")
                            writer.writeLine("Publicação: $publication")
                            writer.writeLine("Início de Vigência: $startTerm")
                            writer.writeLine('')
                        }
                    }
                }
            }
        }
        println('File created in: ' + dirPath)
    }

    static void downloadComponents(Object response, Path dirPath) {
        Document doc = Jsoup.parse(response.toString())
        Elements anchors = doc.select('table a')

        ArrayList<String> linksComponents = []
        anchors.each { link ->
            linksComponents.add(link.attr('href'))
        }

        String regexPattern = /([A-Z])\w+(\.[a-zA-Z]+)?/

        linksComponents.each { link ->
            Matcher matcher = (link =~ regexPattern)

            if (matcher) {
                String fileName = matcher[0][0]
                String fileNameDir = fileName.replaceAll(/Padr.+TISS_?/, '')
                        .replaceAll(/_?[0-9]+(.zip|.pdf)/, '')
                        .replaceAll('_', '')

                println('Downloading: ' + fileName + '...')

                HttpRequest.downloadFile(dirPath, fileNameDir, fileName, link)
            }
        }
        println('Downloaded files in: ' + dirPath)
    }

    static void downloadTableAns(Path dirPath, Object responseTableAns) {
        Document doc = Jsoup.parse(responseTableAns.toString())
        String fileName = 'TabelaErrosEnvioAns.xlsx'

        String link = doc
                .select('div#parent-fieldname-text a')
                .attr('href')

        println('Downloading: ' + fileName + '...')

        HttpRequest.downloadFile(dirPath, 'TabelaAns',
                fileName, link)
    }

}
