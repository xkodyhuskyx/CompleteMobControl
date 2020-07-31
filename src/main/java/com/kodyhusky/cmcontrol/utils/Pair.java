/*
 * Copyright (C) 2012-2020 Jeffery Hancock (xkodyhuskyx)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kodyhusky.cmcontrol.utils;

import java.util.Map.Entry;

/**
 * An object that pairs a single key to a single value.
 *
 * @author xkodyhuskyx
 * @since CompleteMobControl v3.0.0
 * @see Map.Entry
 *
 * @param <K> the type of key maintained by this pair
 * @param <V> the paired value type
 */
public class Pair<K, V> implements Entry<K, V> {

    private final K key;
    private V value;

    /**
     * A key and value pair.
     *
     * @param key new value to be stored in this pair
     * @param value new value to be stored in this pair
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key corresponding to this pair.
     *
     * @return the key corresponding to this pair
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Returns the value corresponding to this pair.
     *
     * @return the value corresponding to this pair
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Replaces the value corresponding to this pair with the specified value.
     *
     * @param value new value to be stored in this pair
     * @return old value corresponding to the pair
     */
    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
