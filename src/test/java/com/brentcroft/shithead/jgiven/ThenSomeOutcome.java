package com.brentcroft.shithead.jgiven;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.context.Messages;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;

public class ThenSomeOutcome extends Stage< ThenSomeOutcome >
{
    @ScenarioState
    RuntimeException actionException;

    @ScenarioState
    StandardGame game;

    @ScenarioState
    Player player;

    @ScenarioState
    Discard play;


    public ThenSomeOutcome cards_already_dealt_exception()
    {
        assertNotNull( actionException );
        assertEquals( Messages.CARDS_ALREADY_DEALT, actionException.getMessage() );

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
                        Messages.NOT_YOUR_TURN,
                        player ),
                actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome invalid_play_exception()
    {
        assertNotNull( actionException );
        assertEquals(
                format(
                        Messages.INVALID_PLAY_CARDS_NOT_IN_HAND,
                        player,
                        play.getCards() ),
                actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome cards_not_dealt_exception()
    {
        assertNotNull( actionException );
        assertEquals( Messages.CARDS_NOT_DEALT, actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome the_stack_is_empty()
    {
        assertEquals( true, game.getGameModel().getStack().isEmpty());
        return self();
    }
}
