package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.CARDS_NOT_DEALT;
import static com.brentcroft.shithead.context.Messages.CURRENT_PLAYER_ALREADY_EXISTS;
import static com.brentcroft.shithead.context.Messages.NO_FIRST_PLAYER;

import java.util.List;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import com.brentcroft.shithead.model.Rules;

public class DetectFirstPlayer implements Command< GameContext >
{

    @Override
    public void action( GameContext context )
    {
        detectFirstPlayer( context.getGameModel() );
    }

    void detectFirstPlayer( GameModel game )
    {
        if ( !game.getDeck().dealt() )
        {
            throw new RuntimeException( CARDS_NOT_DEALT );
        }
        else if ( game.getCurrentPlayer() != null )
        {
            throw new RuntimeException( CURRENT_PLAYER_ALREADY_EXISTS );
        }

        game.setCurrentPlayer( selectFirstPlayer( game.getPlayers(), GameModel.FIRST_PLAYER_VALUE ) );
    }

    Player selectFirstPlayer( List< Player > players, int value )
    {
        if ( value > Rules.SUIT_SIZE )
        {
            throw new RuntimeException( NO_FIRST_PLAYER );
        }

        return players
                .stream()
                .filter( p -> p.hasCard( Player.ROW.HAND, value ) )
                .findFirst()
                .orElseGet( () -> selectFirstPlayer( players, value + 1 ) );
    }
}
