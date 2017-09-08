package rx.netty.http.router;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static rx.netty.http.router.internal.HttpMappingsHelper.getPathVariables;
import static rx.netty.http.router.internal.HttpMappingsHelper.match;

/**
 * Class which holds route mappings and handlers for them
 */
@Slf4j
public class HttpRouter implements RequestHandler<ByteBuf, ByteBuf> {

    /**
     * Map of mappings to their handlers
     */
    private final Map<String, Map<HttpMethod, HttpRequestHandler<ByteBuf, ByteBuf>>> routesMap = new HashMap<>();

    /**
     * @param path       mapping path
     * @param httpMethod http method
     * @param handler    handler for {@code path} and {@code httpMethod}
     * @return current object for cascading creation
     */
    public HttpRouter addRouteHandler(String path, HttpMethod httpMethod, HttpRequestHandler<ByteBuf, ByteBuf> handler) {
        routesMap
                .computeIfAbsent(path, key -> new HashMap<>())
                .put(httpMethod, handler);
        return this;
    }

    @Override
    public Observable<Void> handle(io.reactivex.netty.protocol.http.server.HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
        log.debug("Requested uri : {}", request.getUri());
        Set<Map.Entry<String, Map<HttpMethod, HttpRequestHandler<ByteBuf, ByteBuf>>>> entrySet = routesMap.entrySet();
        for (Map.Entry<String, Map<HttpMethod, HttpRequestHandler<ByteBuf, ByteBuf>>> entry : entrySet) {
            String requestMappingPath = entry.getKey();
            String requestUri = request.getDecodedPath();
            if (match(requestMappingPath, requestUri) && entry.getValue().containsKey(request.getHttpMethod())) {
                log.debug("Found needed handler for requested uri : {}, http method : {} and mapping : {}", requestUri, request.getHttpMethod(), requestMappingPath);
                return entry
                        .getValue()
                        .get(request.getHttpMethod())
                        .handle(
                                new HttpServerRequest<>(
                                        request,
                                        getPathVariables(requestMappingPath, requestUri)
                                ),
                                response
                        );
            }
        }
        return Observable.empty();
    }
}
