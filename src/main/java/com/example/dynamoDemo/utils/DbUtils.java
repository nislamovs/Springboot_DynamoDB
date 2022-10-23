package com.example.dynamoDemo.utils;

import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DbUtils<T> {

    public static BiConsumer<Object, ? super Throwable> doOnError(
            Consumer<? super Throwable> action) {
        return ((o, throwable) -> {
            if (throwable != null) {
                action.accept(throwable);
            }
        });
    }

    public String getLastEvaluatedKey(Page<T> page) {
        return page.lastEvaluatedKey() == null ? null : page.lastEvaluatedKey().get("Id").s();
    }
}
