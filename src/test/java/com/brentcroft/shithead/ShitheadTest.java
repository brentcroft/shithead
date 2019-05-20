package com.brentcroft.shithead;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ShitheadTest
{
    class PlayersFixture
    {
        List< Player > players = Arrays
                .asList(
                        new Player( "Apple" ),
                        new Player( "Orange" ),
                        new Player( "Lemon" ),
                        new Player( "Pear" ),
                        new Player( "Grape" ),
                        new Player( "Plum" ) 
                        );

        public List< Player > getPlayers()
        {
            return getPlayers( players.size() );
        }

        public List< Player > getPlayers( int numPlayers )
        {
            return players.subList( 0, numPlayers );
        }
    }

    PlayersFixture playersFixture;

    @Before
    public void createFixtures()
    {
        playersFixture = new PlayersFixture();
    }




    @Test
    public void gameFinishedWhenOnePlayerLeft()
    {
        Game game = new Game();
        List< Player > players = playersFixture.getPlayers( 3 );

        players.forEach( game::addPlayer );

        game.deal();
        game.play();

        assertEquals( 1, game.numPlayers() );
    }
}
