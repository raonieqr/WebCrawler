package webcrawler.Utils

import groovyx.net.http.HttpBuilder

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
}
