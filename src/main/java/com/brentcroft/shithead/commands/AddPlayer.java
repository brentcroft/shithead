package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.CARDS_ALREADY_DEALT;
import static com.brentcroft.shithead.context.Messages.PLAYER_ALREADY_EXISTS;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

public class AddPlayer implements Command< PlayerContext >
{
    @Override
    public void action( PlayerContext context )
    {
        addPlayer( context.getGameModel(), context.getPlayer() );
    }

    void addPlayer( GameModel gameModel, Player player )
    {
        if ( gameModel.getDeck().dealt() )
        {
            throw new RuntimeException( CARDS_ALREADY_DEALT );
        }
        else if ( gameModel.hasPlayer( player.getName() ) )
        {
            throw new RuntimeException( PLAYER_ALREADY_EXISTS );
        }

        gameModel.getPlayers().add( player );
    }
}
