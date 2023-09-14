package webcrawler.Utils

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download

import java.nio.file.Path
import java.util.concurrent.CompletableFuture

class HttpRequest {
    static Object getResponse(String uriPage) {
        CompletableFuture<Object> responseContent = new CompletableFuture<>()

        try {
            responseContent = HttpBuilder.configure {
                request.uri = uriPage
            }.getAsync()

        } catch (Exception e) {
            responseContent.completeExceptionally(e)
        }

        return responseContent.get()
    }

    static void downloadFile(Path dirPath, String fileNameDir,
                             String fileName, String link) {
        File dir = new File(dirPath.toString(), fileNameDir)
        dir.mkdirs()

        HttpBuilder.configure {
            request.uri = link
        }.get {
            Download.toFile(delegate, new File(dir, fileName))
        }
    }
}
