package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.context.Messages.NOT_ENOUGH_PLAYERS;
import static com.brentcroft.shithead.context.Messages.NO_MORE_CARDS;
import static java.lang.String.format;
import static org.junit.Assert.*;

import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.context.Messages;
import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import com.brentcroft.shithead.www.JSONRenderer;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;

import java.util.BitSet;

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


    public ThenSomeOutcome the_stack_is_empty()
    {
        assertTrue( game.getGameModel().getStack().isEmpty() );
        return self();
    }

    public ThenSomeOutcome the_stack_is_not_empty() {
        assertFalse( game.getGameModel().getStack().isEmpty() );
        return self();
    }

    public ThenSomeOutcome has_stack_top_card( String cardText) {
        assertEquals( Cards.getCard(cardText),  game.getGameModel().getStack().peek() );
        return self();
    }

    public ThenSomeOutcome the_stack_has_cards( String cardText) {
        assertEquals( CardList.of(cardText),  CardList.of(game.getGameModel().getStack()) );
        return self();
    }




    public ThenSomeOutcome the_discard_contains(String cardText) {
        assertNotNull( play );

        assertEquals(
                CardList.of(cardText),
                play.getCards() );

        return self();
    }

    public ThenSomeOutcome game_has_finished() {

        System.out.println( format("GAME FINISHED [%s]: %s", game.getGameModel().isFinished(), JSONRenderer.render(game.getGameModel())));
        System.out.flush();

        assertTrue( game.getGameModel().isFinished());

        return self();
    }


    public ThenSomeOutcome no_exception()
    {
        assertNull( actionException );
        return self();
    }

    public ThenSomeOutcome exception(String message)
    {
        assertNotNull( actionException );
        assertEquals( message, actionException.getMessage() );
        return self();
    }

    public ThenSomeOutcome exception_with_player(String message)
    {
        assertNotNull( actionException );
        assertEquals(
                format(
                        message,
                        player ),
                actionException.getMessage() );
        return self();
    }

    public ThenSomeOutcome exception_with_cards_player(String message, String cardText) {

        assertNotNull( actionException );
        assertEquals(
                format(
                        message,
                        player,
                        cardText ),
                actionException.getMessage() );

        return self();
    }

    public ThenSomeOutcome exception_with_cards_player_row(String message, String cardText, Player.ROW row) {

        assertNotNull( actionException );
        assertEquals(
                format( message, cardText, player, player.getCards(row) ),
                actionException.getMessage() );

        return self();
    }


}
