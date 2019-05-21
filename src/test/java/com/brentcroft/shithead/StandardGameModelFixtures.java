package com.brentcroft.shithead;

import org.junit.Test;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.tngtech.jgiven.junit.ScenarioTest;

public class StandardGameModelFixtures extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    @Test( )
    public void cannot_deal_if_more_than_five_players()
    {
        given().a_new_game().with_six_players();
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
        given().a_new_game().with_three_players();
        when().next_player();
        then().cards_not_dealt_exception();
    }


    @Test( )
    public void play()
    {
        given().a_dealt_3_player_game().first_player_detected().after_playing_turns( 5 );
    }
}
