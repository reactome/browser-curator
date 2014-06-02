package org.reactome.web.elv.client.common.model;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class Pair<A,B> {
    A a;
    B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
