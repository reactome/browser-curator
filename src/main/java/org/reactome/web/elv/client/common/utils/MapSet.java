package org.reactome.web.elv.client.common.utils;

import java.util.*;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class MapSet<S,T> {

    protected Map<S, Set<T>> map = new HashMap<S, Set<T>>();

    public void add(S identifier, T elem){
        Set<T> aux = getOrCreate(identifier);
        if(!aux.contains(elem)){
            aux.add(elem);
        }
    }

    public void add(S identifier, Set<T> set){
        Set<T> aux = getOrCreate(identifier);
        aux.addAll(set);
    }

    public void add(S identifier, List<T> list){
        Set<T> aux = getOrCreate(identifier);
        aux.addAll(list);
    }

    public void addAll(MapSet<S,T> map){
        for (S s : map.keySet()) {
            this.add(s, map.getElements(s));
        }
    }

    public Set<T> getElements(S identifier){
        return map.get(identifier);
    }

    public Set<S> getKeys(T element){
        Set<S> rtn = new HashSet<S>();
        for (S s : map.keySet()) {
            if(map.get(s).contains(element)){
                rtn.add(s);
            }
        }
        return rtn;
    }

    public Set<S> getKeys(Set<T> elements){
        Set<S> rtn = new HashSet<S>();
        for (S s : map.keySet()) {
            for (T element : elements) {
                if(map.get(s).contains(element)){
                    rtn.add(s);
                    break;
                }
            }
        }
        return rtn;
    }

    private Set<T> getOrCreate(S identifier){
        Set<T> set;
        if(map.containsKey(identifier)){
            set = map.get(identifier);
        }else{
            set = new HashSet<T>();
            map.put(identifier, set);
        }
        return set;
    }

    public boolean isEmpty(){
        return map.isEmpty();
    }


    public Set<S> keySet(){
        return map.keySet();
    }
}

