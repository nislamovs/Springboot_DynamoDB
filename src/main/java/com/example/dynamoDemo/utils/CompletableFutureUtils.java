package com.example.dynamoDemo.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CompletableFutureUtils {

  public static BiConsumer<Object, ? super Throwable> doOnError(
      Consumer<? super Throwable> action) {
    return ((o, throwable) -> {
      if (throwable != null) {
        action.accept(throwable);
      }
    });
  }

  public static <T> BiConsumer<? super T, ? super Throwable> acceptRegardless(
      Consumer<? super T> action) {
    return (o, throwable) -> action.accept(o);
  }

  public static <T> BiConsumer<? super T, ? super Throwable> runRegardless(
      Runnable action) {
    return ((o, throwable) -> action.run());
  }

  public static CompletableFuture<List<?>> reduce(List<CompletableFuture<?>> futures) {
    return CompletableFuture
        .allOf(futures.toArray(new CompletableFuture[0]))
        .thenApply(f -> futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList()));
  }
}
