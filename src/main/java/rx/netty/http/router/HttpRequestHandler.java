package rx.netty.http.router;

import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import rx.Observable;

/**
 * Extension for {@link io.reactivex.netty.protocol.http.server.RequestHandler}
 */
@FunctionalInterface
public interface HttpRequestHandler<I, O> {

    /**
     * Provides a request and response pair to process.
     *
     * @param request  Http request to process.
     * @param response Http response to populate after processing the request.
     * @return An {@link Observable} that represents the processing of the request. Subscribing to this should start
     * the request processing and unsubscribing should cancel the processing.
     */
    Observable<Void> handle(HttpServerRequest<I> request, HttpServerResponse<O> response);
}
