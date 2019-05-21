package com.brentcroft.shithead.chain;

public interface Guard<CONTEXT> {

    boolean isSatisfied(CONTEXT context);

}
