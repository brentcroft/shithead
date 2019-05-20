package com.brentcroft.shithead.jgiven;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.brentcroft.shithead.AbstractGame;
import com.brentcroft.shithead.Cards;
import com.brentcroft.shithead.Play;
import com.brentcroft.shithead.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

public class ThenSomeOutcome extends Stage< ThenSomeOutcome >
{
    @ScenarioState
    RuntimeException actionException;

    @ScenarioState
    Player player;

    @ScenarioState
    Play play;


    public ThenSomeOutcome cards_already_dealt_exception()
    {
        assertNotNull( actionException );
        assertEquals( AbstractGame.CARDS_ALREADY_DEALT, actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome no_more_cards_exception()
    {
        assertNotNull( actionException );
        assertEquals( Cards.NO_MORE_CARDS, actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome not_your_turn_exception()
    {
        assertNotNull( actionException );
        assertEquals(
                format(
                        AbstractGame.NOT_YOUR_TURN,
                        player ),
                actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome invalid_play_exception()
    {
        assertNotNull( actionException );
        assertEquals(
                format(
                        AbstractGame.INVALID_PLAY_CARDS_NOT_IN_HAND,
                        player,
                        play.cards() ),
                actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome cards_not_dealt_exception()
    {
        assertNotNull( actionException );
        assertEquals( AbstractGame.CARDS_NOT_DEALT, actionException.getMessage() );

        return self();
    }
}
