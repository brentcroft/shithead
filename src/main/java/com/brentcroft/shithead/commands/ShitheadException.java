package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.context.GameContext;

public class ShitheadException extends RuntimeException
{
    private GameContext context;

    public ShitheadException( String msg )
    {
        super( msg );
    }

    public ShitheadException( GameContext context )
    {
        super( "" );
        this.context = context;
    }

}
