package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Player;

import java.util.List;
import java.util.Stack;

import static java.lang.String.format;

public class CheckDiscardCommand implements Command<DiscardContext> {
    @Override
    public void action(DiscardContext context) {

        context.setValid(
            validPlayForStack(
                    context.getGameModel().getStack(),
                    context.getPlayer(),
                    context.getDiscard().getCards() ) );
    }



    private boolean validPlayForStack(Stack<Cards.Card> stack, Player player, List<Cards.Card> cards )
    {
        if ( cards.size() < 0)
        {
            return false;
        }

        Cards.Card card = cards.get( 0 );

        if ( !( Cards.isWildcard( card ) || stack.isEmpty()
                || stack.peek().getValue() <= card.getValue() ) )
        {
            verifyPlayerCannotPlay( player, cards );
            return false;
        }
        return true;
    }


    private void verifyPlayerCannotPlay( Player player, List<Cards.Card> cards)
    {
        boolean couldHavePlayed = false;

        if ( couldHavePlayed )
        {
            throw new RuntimeException( "Could have played" );
        }
    }
}
