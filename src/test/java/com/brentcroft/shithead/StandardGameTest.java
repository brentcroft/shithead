package com.brentcroft.shithead;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.brentcroft.shithead.jgiven.CardsUtil;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import org.junit.Test;

public class StandardGameTest
{
    @Test
    public void gameFinishedWhenOnePlayerLeft()
    {
        StandardGame game = new StandardGame();

        List< Player > players = CardsUtil.PLAYERS.getPlayers( 3 );

        players.forEach( game::addPlayer );

        game.deal();
        game.detectFirstPlayer();
        game.play();

        assertEquals( 1, game.getGameModel().getPlayers().size() );
    }
}
