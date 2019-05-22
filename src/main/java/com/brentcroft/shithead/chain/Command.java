package com.brentcroft.shithead.chain;

import static java.lang.String.format;

public interface Command<CONTEXT> {

    void action(CONTEXT context);

    default void notifyAction(Object subject, Object action, Object context )
    {
        System.out.println( format( "[%-8s] %-15s %s", subject == null ? "*" : subject, action, context ) );
    }
}
