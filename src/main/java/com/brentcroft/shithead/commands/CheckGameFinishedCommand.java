package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;

import static java.lang.String.format;

public class CheckGameFinishedCommand implements Command<GameContext> {
    @Override
    public void action(GameContext context) {
        checkGameFinished( context.getGameModel() );
    }


    protected void checkGameFinished(GameModel gameModel)
    {
        if ( gameModel.getPlayers().size() < 2 )
        {
            throw new ShitheadException(
                    format( "[%s] is the shithead! %s", gameModel.getCurrentPlayer(),  gameModel.getCurrentPlayer().cardsText() ) );
        }
    }
}
