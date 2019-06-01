package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;

public class CheckGameFinished implements Command< GameContext >
{
    @Override
    public void action( GameContext context )
    {
        context.setFinished( checkGameFinished( context.getGameModel() ) );
    }

    boolean checkGameFinished( GameModel gameModel )
    {
        return gameModel.getPlayers().size() <= gameModel.getMinPlayers();
    }
}
