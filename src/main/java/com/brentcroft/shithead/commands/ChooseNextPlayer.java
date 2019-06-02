package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import com.brentcroft.shithead.www.JSONRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class ChooseNextPlayer implements Command< GameContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();

    @Override
    public void action( GameContext context )
    {
        chooseNextPlayer( context.getGameModel() );
    }

    void chooseNextPlayer( GameModel gameModel )
    {
        boolean[] foundLastPlayer = { false };

        final Player[] currentPlayer = { gameModel.getCurrentPlayer() };

        gameModel
                .getPlayers()
                .stream()
                .forEach( p -> {
                    if ( p == currentPlayer[ 0 ] )
                    {
                        foundLastPlayer[ 0 ] = true;
                        gameModel.getLastPlayer().push( currentPlayer[ 0 ] );
                        currentPlayer[ 0 ] = null;
                    }
                    else if ( foundLastPlayer[ 0 ] )
                    {
                        currentPlayer[ 0 ] = p;
                        foundLastPlayer[ 0 ] = false;
                    }
                } );

        if ( currentPlayer[ 0 ] == null )
        {
            currentPlayer[ 0 ] = gameModel.getPlayers().get( 0 );
        }

        gameModel.setCurrentPlayer( currentPlayer[ 0 ] );

        if ( Objects.nonNull( notifier ) )
        {
            // String cpJson = JSONRenderer.render(currentPlayer[0]);
            notifier.notifyAction( currentPlayer[ 0 ], "is the next player", "" );
        }
    }
}
