package com.brentcroft.shithead.chain;

public interface Command<CONTEXT> {

    void action(CONTEXT context);


}
