package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.PLAYER_COULD_HAVE_PLAYED;

import java.util.List;
import java.util.Stack;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Player;

public class CheckPlayerDiscard implements Command< DiscardContext >
{
    @Override
    public void action( DiscardContext context )
    {

        context.setValid(
                validPlayForStack(
                        context.getGameModel().getStack(),
                        context.getPlayer(),
                        context.getDiscard().getCards() ) );
    }

    boolean validPlayForStack( Stack< Card > stack, Player player, List< Card > cards )
    {
        if ( cards.isEmpty() )
        {
            return false;
        }

        Card card = cards.get( 0 );

        if ( !( Cards.isWildcard( card )
                || stack.isEmpty()
                || stack.peek().getValue() <= card.getValue() ) )
        {
            verifyPlayerCannotPlay( player, cards );
            return false;
        }
        return true;
    }

    void verifyPlayerCannotPlay( Player player, List< Card > cards )
    {
        boolean couldHavePlayed = false;

        if ( couldHavePlayed )
        {
            throw new RuntimeException( PLAYER_COULD_HAVE_PLAYED );
        }
    }
}
