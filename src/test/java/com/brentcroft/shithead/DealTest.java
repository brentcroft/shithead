package com.brentcroft.shithead;

import org.junit.Test;

import com.brentcroft.shithead.jgiven.GivenSomeState;
import com.brentcroft.shithead.jgiven.ThenSomeOutcome;
import com.brentcroft.shithead.jgiven.WhenSomeAction;
import com.tngtech.jgiven.junit.ScenarioTest;

import static com.brentcroft.shithead.context.Messages.*;

public class DealTest extends ScenarioTest< GivenSomeState, WhenSomeAction, ThenSomeOutcome >
{
    @Test( )
    public void cannot_deal_no_players()
    {
        given().a_new_game();
        when().deal_cards();
        then().exception( NOT_ENOUGH_PLAYERS );
    }

    @Test( )
    public void cannot_deal_with_less_than_min_players()
    {
        given().a_new_game().with_min_players( 2 ).with_players( 1 );
        when().deal_cards();
        then().exception( NOT_ENOUGH_PLAYERS );
    }

    @Test( )
    public void cannot_deal_if_more_than_five_players()
    {
        given().a_new_game().with_players( 6 );
        when().deal_cards();
        then().exception(NO_MORE_CARDS);
    }

    @Test
    public void cannot_deal_twice()
    {
        given().a_dealt_3_player_game();
        when().deal_cards();
        then().exception(CARDS_ALREADY_DEALT);
    }

    @Test
    public void cannot_add_players_after_cards_are_dealt()
    {
        given().a_dealt_3_player_game();
        when().a_new_player_is_added();
        then().exception(CARDS_ALREADY_DEALT);
    }

    @Test( )
    public void cannot_play_until_cards_dealt()
    {
        given().a_new_game().with_players( 3 );
        when().next_player();
        then().exception(CARDS_NOT_DEALT);
    }
}
