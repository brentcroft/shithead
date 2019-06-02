package com.brentcroft.shithead;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PlayerTest extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    /*
     * 3♡, 10♣, 9♢
     */

    @Test( )
    public void player_discards_king_before_ten()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ K♢, 10♣ ]" )
                .with_stack_cards( "[ 9♣, 9♢]" )
                .with_deck_cards( "[ 9♣ ]" );



        when().next_player().selects_cards();

        then().the_discard_contains( "[ K♢ ]" );
    }
}
