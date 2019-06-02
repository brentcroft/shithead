package com.brentcroft.shithead;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.brentcroft.shithead.www.JSONRenderer;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.brentcroft.shithead.context.Messages.NOT_ENOUGH_PLAYERS;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PlayTest extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    /*
     * 3♡, 10♣, 9♢
     */


    @Test( )
    public void play()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected();

        when()
                .play_turns( 175 );

        then().game_has_finished();
    }


    @Test( )
    public void four_of_a_kind_detected_during_rollover()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ 9♢, 8♣, 7♣ ]" )
                .with_stack_cards( "[ 9♣, 9♢]" )
                .with_deck_cards( "[ 9♣ ]" );

        when().next_player().plays_cards( "[ 9♢ ]" );

        then().the_stack_is_empty();
    }
}
