# RxNettyHttpRouter: Http routing for RxNetty

RxNettyHttpRouter is extension for [RxNetty](https://github.com/ReactiveX/RxNetty) with routing support

## Getting started

The first step is to add library to your project:

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile "com.github.evgeniyJ:RxNettyHttpRouter:0.0.2"
}
```

The second is to write the **Hello World** program:

```java
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;
import rx.netty.http.router.HttpRouter;

public class HelloWorld {

    public static void main(String[] args) {
        HttpRouter router = new HttpRouter()
                .addRouteHandler(
                        "/hello",
                        HttpMethod.GET,
                        (req, resp) -> resp.writeString(Observable.just("Hello World!"))
                );
        HttpServer
                .newServer(8080)
                .start(router)
                .awaitShutdown();
    }
}
```

And visit : http://localhost:8080/hello