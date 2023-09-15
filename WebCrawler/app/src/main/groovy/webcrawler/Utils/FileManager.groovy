package webcrawler.Utils

import java.nio.file.Files
import java.nio.file.Path

class FileManager {

    static void createFolder(Path dirPath) {
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath)
        }
    }

    static ArrayList<String> readEmails(Path currentDirectory) {
        ArrayList<String> emails = []

        File file = new File(currentDirectory.toString(),
                'emails.txt')

        file.withReader { reader ->
            reader.eachLine { line ->
                emails.add(line)
            }
        }
        return emails
    }

}
