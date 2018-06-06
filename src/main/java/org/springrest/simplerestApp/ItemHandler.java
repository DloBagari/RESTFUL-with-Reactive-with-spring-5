package org.springrest.simplerestApp;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ItemHandler {
    private final ItemStorage itemStorage;

    public ItemHandler(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    //get all items in reactive
    public Mono<ServerResponse> getItems(ServerRequest request) {
        Flux<Item> items = itemStorage.getItems();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(items,Item.class);
    }

    //handle a GET method, which request an item name by the item id.
    public Mono<ServerResponse> getItem(ServerRequest request) {
        //extract info from request object
        int itemId = Integer.parseInt(request.pathVariable("id"));
        //build 'not found' object
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Item> itemMono = itemStorage.getItem(itemId);
        return itemMono.flatMap(item -> ServerResponse.ok()
                .contentType(APPLICATION_JSON).body(BodyInserters.fromObject(item))).switchIfEmpty(notFound);
    }

    //add item through the handler
    public Mono<ServerResponse> addItem(ServerRequest request) {
        //create Mono<Item> from the request body. extract request into Mono
        Mono<Item> item = request.bodyToMono(Item.class);
        System.out.println("handler**********");
        return ServerResponse.ok().build(itemStorage.addItem(item));
    }

    //update item
    public Mono<ServerResponse> updateItem(ServerRequest request) {
        Mono<Item> itemMono = request.bodyToMono(Item.class);
        return ServerResponse.ok().build(itemStorage.updateItem(itemMono));
    }

    //delete item
    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        Mono<Item> itemMono = request.bodyToMono(Item.class);
        return ServerResponse.ok().build(itemStorage.deleteItem(itemMono));
    }
}
