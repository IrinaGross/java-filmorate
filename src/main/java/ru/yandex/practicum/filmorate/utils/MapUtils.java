package ru.yandex.practicum.filmorate.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapUtils {
    private MapUtils() {
    }

    public static Set<Long> getOrEmptySet(Map<Long, Set<Long>> storage, Long key) {
        return storage.getOrDefault(key, new HashSet<>());
    }
}
