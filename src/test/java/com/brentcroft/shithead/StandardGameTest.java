package com.brentcroft.shithead;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.brentcroft.shithead.commands.ShitheadException;
import com.brentcroft.shithead.jgiven.CardsUtil;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;

public class StandardGameTest
{
    @Test
    public void gameFinishedWhenOnePlayerLeft()
    {
        StandardGame game = new StandardGame();

        List< Player > players = CardsUtil.PLAYERS.getPlayers( 3 );

        players.forEach( game::addPlayer );

        game.dealCards();
        game.detectFirstPlayer();
      
        
        try
        {
            while ( true )
            {

                Player player = game.getGameModel().getCurrentPlayer();

                Discard discard = new Discard(
                        player.getName(),
                        player.chooseCards( game.getGameModel().getSelector() ) );

                game.playerDiscard( discard );
            }
        }
        catch ( ShitheadException e )
        {
            System.out.println( e.getMessage() );
        }        
        
        
        

        assertEquals( 1, game.getGameModel().getPlayers().size() );
    }
}
