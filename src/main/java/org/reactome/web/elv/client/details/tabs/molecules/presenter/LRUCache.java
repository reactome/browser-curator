package org.reactome.web.elv.client.details.tabs.molecules.presenter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private int MAX_SIZE;

    public LRUCache() {
        MAX_SIZE = 5;
    }
    public LRUCache(int size) {
        this.MAX_SIZE = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }
}
