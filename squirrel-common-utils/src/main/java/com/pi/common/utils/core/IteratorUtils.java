package com.pi.common.utils.core;

import com.google.common.collect.Iterables;
import com.pi.common.utils.mapper.bean.BeanMapper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class IteratorUtils extends CollectionUtils {

    /**
     If the specified collection is null, returns an immutable empty list. Else, returns the specified collection.
     */
    public static <T> Collection<T> emptyIfNull(final Collection<T> nullable) {
        return nullable != null ? nullable : Collections.emptyList();
    }

    /**
     If the specified list is null, returns an immutable empty list. Else, returns the specified list.
     */
    public static <T> List<T> emptyIfNull(final List<T> nullable) {
        return nullable != null ? nullable : Collections.emptyList();
    }

    public static <T> void executeByBatch(List<T> items, int batchSize, Consumer<List<T>> consumer) {
        for (int i = 0; i < items.size(); i += batchSize) {
            List<T> sublist = items.subList(i, Math.min(i + batchSize, items.size()));
            consumer.accept(sublist);
        }
    }

    public static <T, R> List<R> extractToList(final Collection<T> collection, final Function<T, R> mapper) {
        return collection == null ? null : collection.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T, K> List<K> extractToList(List<T> list, Class<K> classType, MapperFacade mapperFacade) {
        return isEmpty(list) ? Collections.emptyList() : extractToList(list, o -> {
            if (mapperFacade == null) {
                return BeanMapper.map(o, classType);
            }
            return mapperFacade.map(o, classType);
        });
    }

    public static <T, K, V> Map<K, V> extractToMap(final Collection<T> collection, final Function<T, K> keyMapper,
            final Function<T, V> valueMapper) {
        return collection == null ? null : collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <K, V> Map<K, V> extractToMap(final Collection<V> collection, final Function<V, K> keyMapper) {
        return extractToMap(collection, keyMapper, Function.identity());
    }

    public static <T, R> Set<R> extractToSet(final Collection<T> collection, final Function<T, R> mapper) {
        return collection == null ? null : collection.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T> T getFirstOrNull(final Iterable<T> items) {
        if (items != null) {
            Iterator<T> iterator = items.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }

    public static <T, TList extends List<T>> T getLastOrNull(final TList items) {
        if (isNotEmpty(items)) {
            return items.get(items.size() - 1);
        }
        return null;
    }

    public static <K, V> Map<K, List<V>> groupingBy(final Collection<V> collection, final Function<V, K> classifier) {
        return collection == null ? null : collection.stream().collect(Collectors.groupingBy(classifier));
    }

    public static boolean isEmpty(final Iterable<?> iterable) {
        return iterable == null || Iterables.isEmpty(iterable);
    }

    public static boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> Set<T> singletonOrNull(T nullable) {
        return nullable != null ? Collections.singleton(nullable) : null;
    }

    public static int size(final Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    public static int size(final Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     If the specified collection is null, returns a stream from an empty collection. Else, returns the specified collection's
     stream.
     */
    public static <T> Stream<T> stream(final Collection<T> nullable) {
        return isNotEmpty(nullable) ? nullable.stream() : Stream.empty();
    }

}
