package com.kodyhusky.cmcontrol.utils;

import java.util.Map;

/**
 * Used to store a key and value pair.
 * 
 * @author xkodyhuskyx
 * 
 * @param <K> Object
 * @param <V> Object
 */
public class Pair<K,V> implements Map.Entry<K,V> {
    
    private final K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
