package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.jgiven.CardsUtil.PLAYERS;

import java.util.stream.IntStream;

import com.brentcroft.shithead.Game;
import com.brentcroft.shithead.Play;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenSomeState extends Stage< GivenSomeState >
{


    @ProvidedScenarioState
    Game game;



    public GivenSomeState a_new_game()
    {
        game = new Game();
        return self();
    }

    public GivenSomeState with_three_players()
    {
        PLAYERS
                .getPlayers( 3 )
                .forEach( player -> game.addPlayer( player ) );
        return self();
    }


    public GivenSomeState with_six_players()
    {
        PLAYERS
                .getPlayers( 6 )
                .forEach( player -> game.addPlayer( player ) );
        return self();
    }


    public GivenSomeState cards_are_dealt()
    {
        game.deal();
        return self();
    }

    public GivenSomeState after_playing_turns( int turns )
    {
        IntStream
                .range( 0, turns )
                .forEach( turn -> game.play( new Play( game.getCurrentPlayer().chooseCards( game.getSelector() ) ) ) );
        return self();
    }

    public GivenSomeState a_dealt_3_player_game()
    {
        a_new_game().with_three_players().and().cards_are_dealt();
        return self();
    }


}