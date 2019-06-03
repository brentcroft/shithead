package com.brentcroft.shithead;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.brentcroft.shithead.www.JSONRenderer;
import org.junit.Test;

import com.brentcroft.shithead.jgiven.CardsUtil;
import com.brentcroft.shithead.model.Player;

public class StandardGameTest
{
    private static final int MAX_TURN = 300;

    @Test
    public void gameFinishedWhenMinPlayersLeft()
    {
        int minPlayers = 2;


        StandardGame game = new StandardGame();

        game.getGameModel().setMinPlayers( minPlayers );

        List< Player > players = CardsUtil.PLAYERS.getPlayers( 3 );

        players.forEach( game::addPlayer );

        game.dealCards();
        game.detectFirstPlayer();


        while ( !game.getGameModel().isFinished() )
        {
            int turn = game.getGameModel().getTurnNo();

            if ( turn == MAX_TURN )
            {
                throw new RuntimeException(
                        format( "Exceeded max turns: %s %n%s", turn, JSONRenderer.render( game.getGameModel() ) ) );
            }

            Player player = game.getGameModel().getCurrentPlayer();

            game.playerDiscard( player.getDiscard( game.getGameModel().getSelector() ) );
        }

        System.out.println( "LAST PLAYERS: " + game.getGameModel().getPlayers() );
        System.out.println( "MODEL: " + JSONRenderer.render( game.getGameModel() ) );


        assertTrue( game.getGameModel().isFinished() );
    }
}
