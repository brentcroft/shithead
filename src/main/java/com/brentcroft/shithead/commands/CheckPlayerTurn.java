package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.NOT_YOUR_TURN;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.PlayerContext;

public class CheckPlayerTurn implements Command< PlayerContext >
{
    @Override
    public void action( PlayerContext context )
    {
        if ( context.getPlayer() != context.getGameModel().getCurrentPlayer() )
        {
            throw new RuntimeException( NOT_YOUR_TURN );
        }
    }
}
