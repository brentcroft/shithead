package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

import java.util.List;

public class DetectFirstPlayerCommand implements Command<GameContext> {

    public static final int FIRST_PLAYER_VALUE = 3;

    @Override
    public void action(GameContext context) {
        detectFirstPlayer(context.getGameModel());
    }


    private void detectFirstPlayer(GameModel game)
    {
        if ( !game.getDeck().dealt() )
        {
            throw new RuntimeException( Messages.CARDS_NOT_DEALT );
        }
        if (game.getCurrentPlayer() != null)
        {
            throw new RuntimeException( Messages.CURRENT_PLAYER_ALREADY_EXISTS );
        }

        game.setCurrentPlayer(selectFirstPlayer( game.getPlayers(), FIRST_PLAYER_VALUE ) );
    }


    private Player selectFirstPlayer(List<Player> players, int value )
    {
        if ( value > Cards.SUIT_SIZE )
        {
            throw new RuntimeException( "No first player" );
        }

        return players
                .stream()
                .filter( p -> p.hasCard( Player.ROW.HAND, value ) )
                .findFirst()
                .orElseGet( () -> selectFirstPlayer( players, value + 1 ) );
    }


}
