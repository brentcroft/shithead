package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Player;

import java.util.List;

import static java.lang.String.format;

public class CheckPlayerCardsCommand implements Command<DiscardContext> {

    public static final String NOT_YOUR_TURN = "Not your turn";
    public static final String INVALID_PLAY_NO_CARDS = "Invalid play, no cards";
    public static final String INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES = "Invalid play, cards different values: %s %s";
    public static final String INVALID_PLAY_CARDS_NOT_IN_HAND = "Invalid play, cards not in hand: %s %s";
    public static final String INVALID_PLAY_CARDS_NOT_IN_FACEUP = "Invalid play, cards not in faceup: %s %s";


    @Override
    public void action(DiscardContext context) {

        if (context.getPlayer() != context.getGameModel().getCurrentPlayer()) {
            throw new RuntimeException( NOT_YOUR_TURN );
        }

        checkPlayerCards( context.getPlayer(), context.getDiscard().getCards() );
    }

    private void checkPlayerCards(Player player, List<Cards.Card> cards )
    {
        if ( cards.size() < 1 )
        {
            throw new RuntimeException( INVALID_PLAY_NO_CARDS );
        }

        if ( !cardsAllSameValue( cards ) )
        {
            throw new RuntimeException( format( INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES, player, cards ) );
        }

        if ( player.hasCardsInHand() )
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


    private boolean cardsAllSameValue( List<Cards.Card> cards )
    {
        if ( cards.size() < 0)
        {
            return false;
        }
        int value = cards.get( 0 ).getValue();
        return 0 == cards.stream().filter( c -> c.getValue() != value ).count();
    }
}
