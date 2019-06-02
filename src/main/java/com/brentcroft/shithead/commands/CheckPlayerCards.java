package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_NOT_IN_FACEUP;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_NOT_IN_HAND;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES;
import static com.brentcroft.shithead.context.Messages.INVALID_PLAY_NO_CARDS;
import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

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
        
        CardList remainder = cards;
        int remainderSize = remainder.size();
        
        if ( player.hasCardsInHand() )
        {
            CardList handCards = player.getHandCards();
            
            // any cards remaining not in hand?
            remainder = CardList.of( remainder
                .stream()
                .filter( c-> !handCards.contains( c ) )
                .collect( Collectors.toList() ) );
                
            if (!remainder.isEmpty() && (remainderSize - remainder.size()) != handCards.size())
            {
                throw new RuntimeException(
                        format( INVALID_PLAY_CARDS_NOT_IN_HAND, cards, player, player.getHandCards() ) );
            }
        }
        
        if ( player.hasCardsInFaceUp() )
        {
            CardList faceupCards = player.getFaceUpCards();
            
            remainderSize = remainder.size();
            
            // any cards remaining not in hand?
            remainder = CardList.of( remainder
                .stream()
                .filter( c-> !faceupCards.contains( c ) )
                .collect( Collectors.toList() ) );            
            
            
            if (!remainder.isEmpty() && (remainderSize - remainder.size()) != faceupCards.size())
            {
                throw new RuntimeException( format( INVALID_PLAY_CARDS_NOT_IN_FACEUP, cards, player, player.getFaceUpCards() ) );
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
