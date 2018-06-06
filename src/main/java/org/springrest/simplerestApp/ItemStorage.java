package org.springrest.simplerestApp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemStorage {
    Flux<Item> getItems();
    Mono<Item> getItem(int id);
    Mono<Void> addItem(Mono<Item> item);
    Mono<Void> updateItem(Mono<Item> itemMono);
    Mono<Void> deleteItem(Mono<Item> itemMono);
}
