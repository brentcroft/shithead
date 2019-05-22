package com.brentcroft.shithead.commands;

import static java.lang.String.format;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;

public class CheckGameFinished implements Command< GameContext >
{
    @Override
    public void action( GameContext context )
    {
        checkGameFinished( context.getGameModel() );
    }

    void checkGameFinished( GameModel gameModel )
    {
        if ( gameModel.getPlayers().size() < 2 )
        {
            throw new ShitheadException(
                    format( "[%s] is the shithead! %s",
                            gameModel.getCurrentPlayer(),
                            gameModel.getCurrentPlayer().cardsText() ) );
        }
    }
}
