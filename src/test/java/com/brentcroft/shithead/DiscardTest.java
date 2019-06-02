package com.brentcroft.shithead;

import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_NOT_IN_HAND;
import static com.brentcroft.shithead.context.Messages.PLAYER_COULD_HAVE_PLAYED;
import static java.lang.String.format;

import com.brentcroft.shithead.model.Player;
import org.junit.Test;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.tngtech.jgiven.junit.ScenarioTest;


public class DiscardTest extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    /*
     * A♣, A, 5♣
     */
    @Test( )
    public void error_when_player_could_have_played()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ 5♣, 2♣, 7♣, 10♣ ]" )
                .with_stack_cards( "[ 6♣ ]" );

        when().next_player().plays_cards( "[ 5♣ ]" );

        then().discard_exception_with_message_and_cards( PLAYER_COULD_HAVE_PLAYED, "[ 7♣, 2♣, 10♣ ]" );
    }



    @Test( )
    public void discard_from_faceup_when_empty_hand()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_empty_hand_cards()
                .with_faceup_cards( "[ K♢, 8♣, 8♢ ]" )
                .with_deck_cards( "[ 5♡ ]" );

        when()
                .next_player()
                .plays_cards( "[ K♢ ]" );

        then()
                .the_stack_is_not_empty()
                .has_stack_top_card( "K♢" );
    }



    @Test( )
    public void discard_from_blind_when_empty_hand_and_faceup()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_empty_hand_cards()
                .with_empty_faceup_cards()
                .with_blind_Cards( "[ K♢ ]" )
                .with_stack_cards( "[ 4♡ ]" )
                .with_deck_cards( "[ 5♡ ]" );

        when()
                .next_player()
                .plays_cards( "[ K♢ ]" );

        then()
                .the_stack_is_not_empty()
                .has_stack_top_card( "K♢" );
    }


    @Test( )
    public void elect_from_faceup_when_same_value_and_no_other_cards_in_hand()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ 5♢ ]" )
                .with_faceup_cards( "[ 5♠, 4♣, 3♠ ]" )
                .with_stack_cards( "[ 5♡ ]" )
                .with_deck_cards( "[ 8♡ ]" );

        when()
                .next_player()
                .plays_cards( "[ 5♢, 5♠ ]" );

        then()
                .no_exception()
                .the_stack_is_not_empty()
                .the_stack_has_cards( "[ 5♡, 5♢, 5♠ ]" );
    }


    @Test( )
    public void elect_from_faceup_when_same_value_and_no_other_cards_in_hand_and_rollover()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ 5♢ ]" )
                .with_faceup_cards( "[ 5♠, 4♣, 3♠ ]" )
                .with_stack_cards( "[ 5♡ ]" )
                .with_deck_cards( "[ 5♣ ]" );

        when()
                .next_player()
                .plays_cards( "[ 5♢, 5♠ ]" );

        then()
                .no_exception()
                .the_stack_is_empty();
    }




    @Test( )
    public void exception_when_elect_faceup_when_other_cards_in_hand()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_hand_cards( "[ 5♢, 6♢ ]" )
                .with_faceup_cards( "[ 5♠, 4♣, 3♠ ]" )
                .with_stack_cards( "[ 5♡ ]" )
                .with_deck_cards( "[ 5♣ ]" );

        when()
                .next_player()
                .plays_cards( "[ 5♢, 5♠ ]" );

        then()
                .exception_with_message_cards_row( INVALID_PLAY_CARDS_NOT_IN_HAND, "[ 5♢, 5♠ ]", Player.ROW.HAND )
                .the_stack_has_cards( "[ 5♡ ]" );
    }
}
