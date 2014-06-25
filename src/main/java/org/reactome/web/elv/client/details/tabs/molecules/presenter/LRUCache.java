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
    public LRUCache(int MAX_SIZE) {
        super(MAX_SIZE + 1, 1.0f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }
}
