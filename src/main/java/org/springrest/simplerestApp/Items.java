package org.springrest.simplerestApp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class Items implements ItemStorage {
    // initiate Users
    private Map<Integer, Item> items = new HashMap<>();

    // fill dummy values for testing
    public Items() {
        items.put(1, new Item(1, "item1"));
        items.put(2, new Item(2, "item2"));
    }

    // this method will return all users
    @Override
    public Flux<Item> getItems() {
        return Flux.fromIterable(this.items.values());

    }

    @Override
    public Mono<Item> getItem(int id) {
        return Mono.justOrEmpty(items.get(id));
    }

    @Override
    public Mono<Void> addItem(Mono<Item> itemMono) {
        //doOnNext: to save the item in items map, thenEmpty: return empty Mono in the case of failure
        return itemMono.doOnNext(item -> {
            items.put(item.getId(), item);
            System.out.format("item: %s is saved with Id: %d", item.getName(), item.getId());
        }).thenEmpty(Mono.empty());
    }

    @Override
    public Mono<Void> updateItem(Mono<Item> itemMono) {
        return itemMono.doOnNext(item -> {
            items.put(item.getId(), item);
            System.out.format("update item with id %d", item.getId());
        }).thenEmpty(Mono.empty());

    }

    @Override
    public Mono<Void> deleteItem(Mono<Item> itemMono) {
        return itemMono.doOnNext(item -> {
            items.remove(item.getId());
            System.out.format("item with id: %d is deleted", item.getId());
        }).thenEmpty(Mono.empty());
    }
}
