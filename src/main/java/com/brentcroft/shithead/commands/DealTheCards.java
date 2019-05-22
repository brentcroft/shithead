package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.CARDS_ALREADY_DEALT;

import java.util.stream.IntStream;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

public class DealTheCards implements Command< GameContext >
{

    @Override
    public void action( GameContext context )
    {
        deal( context.getGameModel() );
    }

    void deal( GameModel gameModel )
    {
        if ( gameModel.getDeck().dealt() )
        {
            throw new RuntimeException( CARDS_ALREADY_DEALT );
        }

        Player.ROW.forEach(
                row -> {
                    IntStream
                            .range( 0, GameModel.NUM_CARDS_COLUMNS )
                            .forEachOrdered(
                                    column -> gameModel
                                            .getPlayers()
                                            .forEach( player -> player.addCard(
                                                    row,
                                                    gameModel.getDeck().next() ) ) );
                } );
    }

}
