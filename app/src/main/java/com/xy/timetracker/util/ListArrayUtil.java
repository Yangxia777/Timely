package com.xy.timetracker.util;

import java.util.List;

/**
 * utility to transform from primitives to box object or vice versa.
 */
public class ListArrayUtil {
    public static long[] toPrimitives(Long... objects) {

        long[] primitives = new long[objects.length];
        for (int i = 0; i < objects.length; i++) {
            primitives[i] = objects[i];
        }

        return primitives;
    }

    public static long[] toPrimitives(List<Long> list)  {
        long[] primitives = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            primitives[i] = list.get(i);
        }
        return primitives;
    }

    public static void addPrimitivesToList(List<Long> list, long... objects) {
        for (long object : objects) {
            list.add(object);
        }
    }
}
