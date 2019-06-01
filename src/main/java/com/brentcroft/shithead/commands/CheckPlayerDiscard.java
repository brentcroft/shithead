package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.PLAYER_COULD_HAVE_PLAYED;
import static java.lang.String.format;

import java.util.List;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

public class CheckPlayerDiscard implements Command< DiscardContext >
{
    @Override
    public void action( DiscardContext context )
    {

        context.setValid(
                validPlayForStack(
                        context.getGameModel(),
                        context.getPlayer(),
                        context.getDiscard().getCards() ) );
    }

    boolean validPlayForStack( GameModel gameModel, Player player, List< Card > cards )
    {
        if ( cards.isEmpty() )
        {
            return false;
        }
        else if ( ( player.hasCardsInHand() || player.hasCardsInFaceUp() ) && !gameModel.getSelector().test( cards.get( 0 ) ) )
        {
            verifyPlayerCannotPlay( gameModel, player, cards );

            return false;
        }
        return true;
    }

    void verifyPlayerCannotPlay( GameModel gameModel, Player player, List< Card > cards )
    {
        CardList choices = player.chooseValidCards(gameModel.getSelector());

        if ( choices != null && !choices.isEmpty() && ! choices.containsAll(cards))
        {
            throw new RuntimeException( format( PLAYER_COULD_HAVE_PLAYED, player, choices ) );
        }
    }
}
