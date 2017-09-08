package rx.netty.http.router;

import lombok.Data;

import java.util.Map;

/**
 * Extension for {@link io.reactivex.netty.protocol.http.server.HttpServerRequest}
 */
@Data
public class HttpServerRequest<I> {

    private final io.reactivex.netty.protocol.http.server.HttpServerRequest<I> request;
    private final Map<String, String> pathVariables;
}
