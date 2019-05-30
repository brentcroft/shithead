package com.brentcroft.shithead.commands;

import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class ActionNotifier
{
    private static final ActionNotifier NOTIFIER = new ActionNotifier();


    private ActionNotifier()
    {}

    public static ActionNotifier getNotifier()
    {
        return NOTIFIER;
    }

    public void notifyAction(Object subject, Object action, Object context )
    {
        System.out.println( format( "[%-8s] %-15s %s", subject == null ? "*" : subject, action, context ) );
    }
}
