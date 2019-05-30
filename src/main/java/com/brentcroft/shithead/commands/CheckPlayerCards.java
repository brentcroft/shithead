package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_NOT_IN_FACEUP;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_NOT_IN_HAND;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_NO_CARDS;
import static java.lang.String.format;

import java.util.List;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.Player;

public class CheckPlayerCards implements Command< DiscardContext >
{
    @Override
    public void action( DiscardContext context )
    {
        checkPlayerCards( context.getPlayer(), context.getDiscard().getCards() );
    }

    void checkPlayerCards( Player player, CardList cards )
    {
        if ( cards.isEmpty() )
        {
            throw new RuntimeException( INVALID_PLAY_NO_CARDS );
        }
        else if ( !cardsAllSameValue( cards ) )
        {
            throw new RuntimeException( format( INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES, player, cards ) );
        }
        // TODO:
        // tally of the cards against the hand
        // any remainder must be in faceup else invalid play
        else if ( player.hasCardsInHand() )
        {
            if ( !player.hasCards( Player.ROW.HAND, cards ) )
            {
                throw new RuntimeException( format( INVALID_PLAY_CARDS_NOT_IN_HAND, player, cards ) );
            }
        }
        else if ( player.hasCardsInFaceUp() )
        {
            if ( !player.hasCards( Player.ROW.FACEUP, cards ) )
            {
                throw new RuntimeException( format( INVALID_PLAY_CARDS_NOT_IN_FACEUP, player, cards ) );
            }
        }
    }

    boolean cardsAllSameValue( List< Card > cards )
    {
        if ( cards.size() < 0 )
        {
            return false;
        }
        int value = cards.get( 0 ).getValue();
        return 0 == cards.stream().filter( c -> c.getValue() != value ).count();
    }
}
