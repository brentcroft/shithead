package com.brentcroft.shithead;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.brentcroft.shithead.model.CardList;
import org.junit.Test;

import com.brentcroft.shithead.commands.ShitheadException;
import com.brentcroft.shithead.jgiven.CardsUtil;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
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
            while ( game.getGameModel().getMinPlayers() <= game.getGameModel().getPlayers().size()  )
            {
                Player player = game.getGameModel().getCurrentPlayer();

                CardList cards = player.chooseCards( game.getGameModel().getSelector() );

                if ( cards.size() == 0)
                {
                    cards = CardList.of( player.getHandCards().get(0) );
                }

                game.playerDiscard( new Discard( player.getName(), CardList.of( cards.get(0) ) ) );
            }
        }
        catch ( ShitheadException e )
        {
            System.out.println( e.getMessage() );
        }        

        assertTrue( game.getGameModel().getMinPlayers() >= game.getGameModel().getPlayers().size() );
    }
}
