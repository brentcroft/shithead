package com.brentcroft.shithead.commands;

public class ShitheadException extends RuntimeException
{
    private static final long serialVersionUID = 3331832350377422869L;

    public ShitheadException( String msg )
    {
        super( msg );
    }
}
