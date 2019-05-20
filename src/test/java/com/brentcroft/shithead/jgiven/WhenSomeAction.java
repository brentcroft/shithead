package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.jgiven.CardsUtil.ANY_CARDS;
import static com.brentcroft.shithead.jgiven.CardsUtil.CARDS_NOT_IN_HAND;

import com.brentcroft.shithead.Game;
import com.brentcroft.shithead.Play;
import com.brentcroft.shithead.Player;
import com.brentcroft.shithead.jgiven.CardsUtil.CardListGenerator;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class WhenSomeAction extends Stage< WhenSomeAction >
{
    @ExpectedScenarioState
    Game game;

    @ProvidedScenarioState
    RuntimeException actionException;

    @ProvidedScenarioState
    Player player;

    @ProvidedScenarioState
    Play play;


    interface GameAction
    {
        void act();
    }

    private void ga( GameAction ga )
    {
        try
        {
            ga.act();
        }
        catch ( RuntimeException e )
        {
            actionException = e;
        }
    }


    public WhenSomeAction a_new_player_is_added()
    {
        ga( () -> game.addPlayer( new Player( "Fenonimy" ) ) );
        return self();
    }

    public WhenSomeAction deal_cards()
    {
        ga( () -> game.deal() );
        return self();
    }


    public WhenSomeAction next_player()
    {
        ga( () -> player = game.getCurrentPlayer() );
        return self();
    }


    public WhenSomeAction plays( CardListGenerator cardsGenerator )
    {
        ga( () -> game.play( new Play( cardsGenerator.cards( game.getCurrentPlayer() ) ) ) );
        return self();
    }


    public WhenSomeAction plays_any_cards()
    {
        try
        {
            plays( ANY_CARDS );
        }
        catch ( RuntimeException e )
        {
            actionException = e;
        }
        return self();
    }

    public WhenSomeAction plays_cards_not_in_hand()
    {
        try
        {
            plays( CARDS_NOT_IN_HAND );
        }
        catch ( RuntimeException e )
        {
            actionException = e;
        }
        return self();
    }
}