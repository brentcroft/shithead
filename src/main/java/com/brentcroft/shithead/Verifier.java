package com.brentcroft.shithead;

import static java.lang.String.format;

import com.brentcroft.shithead.Cards.Card;
import com.brentcroft.shithead.Player.ROW;

public class Verifier
{
    private final AbstractGame game;


    public Verifier( AbstractGame game )
    {
        this.game = game;
    }


    void validPlayForPlayer( Player player, Play play )
    {
        if ( play.cards().size() < 1 )
        {
            throw new RuntimeException( "Invalid play, no cards" );
        }

        if ( !cardsAllSameValue( play ) )
        {
            throw new RuntimeException( "Invalid play, cards different values" );
        }

        if ( player.hasCardsInHand() )
        {
            if ( !player.hasCards( ROW.HAND, play.cards() ) )
            {
                throw new RuntimeException( format( AbstractGame.INVALID_PLAY_CARDS_NOT_IN_HAND, player, play.cards() ) );
            }
        }
        else if ( player.hasCardsInFaceUp() )
        {
            if ( !player.hasCards( ROW.FACEUP, play.cards() ) )
            {
                throw new RuntimeException(
                        format( "Invalid play, cards not in faceup: %s %s", player, play.cards() ) );
            }
        }
    }

    private boolean cardsAllSameValue( Play play )
    {
        int value = play.cards().get( 0 ).getValue();
        return 0 == play.cards().stream().filter( c -> c.getValue() != value ).count();
    }


    void verifyPlayerCannotPlay( Player player, Play play )
    {
        boolean couldHavePlayed = false;

        if ( couldHavePlayed )
        {
            throw new RuntimeException( "Could have played" );
        }
    }


    boolean validPlayForStack( Player player, Play play )
    {
        Card card = play.cards().get( 0 );
        if ( !( Cards.isWildcard( card ) || this.game.stack.isEmpty()
                || this.game.stack.peek().getValue() <= card.getValue() ) )
        {
            verifyPlayerCannotPlay( player, play );
            return false;
        }
        return true;


    }
}