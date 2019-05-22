package com.brentcroft.shithead.commands;

import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

@Component
public class ChoosePreviousPlayer implements Command< GameContext >
{

    @Override
    public void action( GameContext context )
    {
        choosePreviousPlayer( context.getGameModel() );
    }

    void choosePreviousPlayer( GameModel gameModel )
    {
        Player player = gameModel.getLastPlayer().pop();

        gameModel.setCurrentPlayer( player );

        notifyAction( player, "goes again", "" );
    }
}
