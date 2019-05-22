package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.jgiven.CardsUtil.ANY_CARDS;

import java.util.Objects;

import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.jgiven.CardsUtil.CardListGenerator;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class WhenSomeAction extends Stage< WhenSomeAction >
{
    @ExpectedScenarioState
    StandardGame game;

    @ProvidedScenarioState
    RuntimeException actionException;

    @ProvidedScenarioState
    Player player;

    @ProvidedScenarioState
    Discard play;


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
        ga( () -> game.dealCards() );
        return self();
    }


    public WhenSomeAction next_player()
    {
        ga( () -> {
            if ( game.getGameModel().getCurrentPlayer() == null )
            {
                game.detectFirstPlayer();
            }
            player = game.getGameModel().getCurrentPlayer();
        } );
        return self();
    }


    public WhenSomeAction plays( CardListGenerator cardsGenerator )
    {
        if ( Objects.isNull( game.getGameModel().getCurrentPlayer() ) )
        {
            throw new IllegalStateException( "No current player" );
        }

        ga( () -> game.playerDiscard(
                new Discard(
                        game.getGameModel().getCurrentPlayer().getName(),
                        cardsGenerator.cards( game.getGameModel().getCurrentPlayer() ) ) ) );
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

    public WhenSomeAction plays_cards( String cardText )
    {
        try
        {
            plays( player -> Cards.fromText( cardText ) );
        }
        catch ( RuntimeException e )
        {
            actionException = e;
        }
        return self();
    }
}