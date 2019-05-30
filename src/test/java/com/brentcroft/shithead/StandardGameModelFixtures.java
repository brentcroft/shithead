package com.brentcroft.shithead;

import org.junit.Test;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.tngtech.jgiven.junit.ScenarioTest;

import static com.brentcroft.shithead.context.Messages.NOT_ENOUGH_PLAYERS;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class StandardGameModelFixtures extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    /*
        3♡, 10♣, 9♢
     */
    @Test( )
    public void cannot_deal_no_players()
    {
        given().a_new_game();
        when().deal_cards();
        then().exception_with_message(NOT_ENOUGH_PLAYERS);
    }

    @Test( )
    public void cannot_deal_with_less_than_min_players()
    {
        given().a_new_game().with_min_players(2).with_players(1);
        when().deal_cards();
        then().exception_with_message(NOT_ENOUGH_PLAYERS);
    }

    @Test( )
    public void cannot_deal_if_more_than_five_players()
    {
        given().a_new_game().with_players(6);
        when().deal_cards();
        then().no_more_cards_exception();
    }

    @Test
    public void cannot_deal_twice()
    {
        given().a_dealt_3_player_game();
        when().deal_cards();
        then().cards_already_dealt_exception();
    }

    @Test
    public void cannot_add_players_after_cards_are_dealt()
    {
        given().a_dealt_3_player_game();
        when().a_new_player_is_added();
        then().cards_already_dealt_exception();
    }

    @Test( )
    public void cannot_play_until_cards_dealt()
    {
        given().a_new_game().with_players(3);
        when().next_player();
        then().cards_not_dealt_exception();
    }


    @Test( )
    public void play()
    {
        given().a_dealt_3_player_game().first_player_detected().after_playing_turns( 5 );
    }


    @Test( )
    public void four_of_a_kind_detected_during_rollover()
    {
        given()
                .a_new_game()
                .a_player( "aaa" ).with_hand_cards( "[ 7♣, 7♦]" )
                .a_player( "bbb" ).with_hand_cards( "[ 8♣, 8♦]" )
                .a_player( "ccc" ).with_hand_cards( "[ 9♣, 9♦]" )
                .cards_are_dealt()
                .first_player_detected()
                .with_stack_cards( "[ 9♣, 9♦]" );

        when().next_player().plays_cards( "[ 9♣, 9♦]" );

        then().the_stack_is_empty();
    }

    @Test()
    public void tetXX()
    {
        given()
                .a_new_game()
                .a_player("red").with_hand_cards( "[ A♣ ]" )
                .a_player("blue").with_hand_cards( "[ A♣ ]" )
                .cards_are_dealt()
                .first_player_detected()
                .with_stack_cards("[ 8♣, 8♢ ]");

        when().next_player().plays_cards( "[ A♣ ]" );

        then().the_stack_is_not_empty();
    }



    @Test()
    public void discard_from_faceup_when_empty_hand()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_empty_hand_cards()
                .with_faceup_Cards("[ K♣, 8♣, 8♢ ]");

        when()
                .next_player()
                .plays_cards( "[ K♣ ]" );

        then()
                .the_stack_is_not_empty()
                .has_stack_top_card("K♣");
    }




    @Test()
    public void discard_from_blind_when_empty_hand_and_faceup()
    {
        given()
                .a_dealt_3_player_game()
                .first_player_detected()
                .with_empty_hand_cards()
                .with_empty_faceup_cards()
                .with_blind_Cards("[ K♣ ]")
                .with_stack_cards("[ 4♡ ]");

        when()
                .next_player()
                .plays_cards( "[ K♣ ]" );

        then()
                .the_stack_is_not_empty()
                .has_stack_top_card("K♣");
    }
}
