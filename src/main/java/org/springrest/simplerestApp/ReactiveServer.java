package org.springrest.simplerestApp;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.ipc.netty.http.server.HttpServer;

/**
 * CRUD operations with reactive support, (Spring reactive support is not yet stable)
 * RESTFUL
 */
public class ReactiveServer {
    public static final String HOST = "localhost";
    public static final int PORT = 8081;

    public static void main(String[] args) throws InterruptedException, IOException {
        ReactiveServer server = new ReactiveServer();
        server.startReactorServer();

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public void startReactorServer() throws InterruptedException {
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(HOST, PORT);
        server.newHandler(adapter).block();
    }

    public RouterFunction<ServerResponse> routingFunction() {
        ItemStorage repository = new Items();
        ItemHandler handler = new ItemHandler(repository);
        //adding REST API paths, and handler for CRUD operations
        return nest(
                path("/item"),
                nest(
                     accept(MediaType.ALL),
                     route(GET("/"), handler::getItems)
                )
                .andRoute(GET("/{id}"), handler::getItem)
                .andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::addItem)
                .andRoute(PUT("/").and(contentType(APPLICATION_JSON)), handler::updateItem)
                .andRoute(DELETE("/").and(contentType(APPLICATION_JSON)),handler::deleteItem)

        );
    }
}




