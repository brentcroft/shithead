package com.brentcroft.shithead;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import com.brentcroft.shithead.Cards.Card;
import com.brentcroft.shithead.Player.ROW;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Game extends AbstractGame
{
    public static final BiFunction< Stack< Card >, Card, Boolean > STACK_CARD_SELECTOR = ( stack,
            card ) -> stack.isEmpty()
                    || Cards.isWildcard( card )
                    || card.getValue() >= stack.peek().getValue();
    protected final Predicate< Card > CARD_PREDICATE = ( card ) -> STACK_CARD_SELECTOR.apply( stack, card );


    public Predicate< Card > getSelector()
    {
        return CARD_PREDICATE;
    }



    public void addPlayer( Player player )
    {
        if ( cards.dealt() )
        {
            throw new RuntimeException( CARDS_ALREADY_DEALT );
        }

        if (hasPlayer(player.getName()))
        {
            throw new RuntimeException(PLAYER_ALREADY_EXISTS);
        }
        players.add( player );
    }


    public void deal()
    {
        if ( cards.dealt() )
        {
            throw new RuntimeException( CARDS_ALREADY_DEALT );
        }

        ROW.forEach(
                row -> {
                    IntStream
                            .range( 0, NUM_CARDS_COLUMNS )
                            .forEachOrdered(
                                    column -> players.forEach( player -> player.addCard( row, cards.next() ) ) );
                } );
    }


    public Player getFirstPlayer()
    {
        if ( !cards.dealt() )
        {
            throw new RuntimeException( CARDS_NOT_DEALT );
        }

        if ( firstPlayer == null )
        {
            firstPlayer = firstPlayer( FIRST_PLAYER_VALUE );
        }
        return firstPlayer;
    }


    public Player getCurrentPlayer()
    {
        if ( currentPlayer == null )
        {
            currentPlayer = getFirstPlayer();
        }

        return currentPlayer;
    }


    public void play()
    {
        try
        {
            while ( numPlayers() > 0 )
            {
                Player player = getCurrentPlayer();

                Play play = new Play( player.chooseCards( CARD_PREDICATE ) );

                notifyPlay( player, "stack", stack );
                notifyPlay( player, "starts", player.cardsText() );

                play( play );
            }
        }
        catch ( ShitheadException e )
        {
            System.out.println( e.getMessage() );
        }
    }



    public void play( Play play )
    {
        Player player = getCurrentPlayer();

        verifier.validPlayForPlayer( player, play );

        if ( verifier.validPlayForStack( player, play ) )
        {
            electFaceupCards( player, play );

            playCards( player, play );

            playerTopsUpHand( player );

            if ( stack.size() > 0 )
            {
                chooseNextPlayer();
            }
        }
        else
        {
            notifyPlay( player, "elects", play.cardsText() );

            pickUpStack( player );

            choosePreviousPlayer();
        }

        removeFinishedPlayer( player );

        checkGameFinished();
    }
}
