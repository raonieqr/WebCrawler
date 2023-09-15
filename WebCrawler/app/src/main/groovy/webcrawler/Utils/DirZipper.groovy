package webcrawler.Utils

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class DirZipper {

    static void zipFolder(String sourceFolderPath, String outputZipFilePath) {
        def sourceFolder = new File(sourceFolderPath)
        def outputFile = new File(outputZipFilePath)
        def buffer = new byte[1024]

        try {
            def zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile))

            addFilesToZip(sourceFolder, sourceFolder, zipOutputStream, buffer)
            zipOutputStream.close()
        } catch (IOException e) {
            println("Error: ao criar o arquivo ZIP: ${e.message}")
        }
    }

    static void addFilesToZip(File baseFolder, File currentFolder, ZipOutputStream zipOutputStream, byte[] buffer) throws IOException {
        currentFolder.eachFile { file ->
            def entryName = currentFolder.toPath().relativize(file.toPath()).toString()

            if (file.isDirectory()) {
                zipOutputStream.putNextEntry(new ZipEntry(entryName + '/'))

                addFilesToZip(baseFolder, file, zipOutputStream, buffer)
            }
            else {
                zipOutputStream.putNextEntry(new ZipEntry(entryName))

                def input = new FileInputStream(file)
                int bytesRead

                while ((bytesRead = input.read(buffer)) != -1)
                    zipOutputStream.write(buffer, 0, bytesRead)
                input.close()
            }
        }
    }

}
