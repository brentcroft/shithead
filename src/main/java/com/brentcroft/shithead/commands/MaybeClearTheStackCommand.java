package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Cards.Card;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;

@Component
public class MaybeClearTheStackCommand implements Command<DiscardContext> {

    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(DiscardContext context) {
        playCards( context.getGameModel().getStack(), context.getPlayer(), context.getDiscard().getCards() );
    }

    protected void playCards(Stack<Card> stack, Player player, List<Card> cards )
    {
        if ( maybe10(stack) || maybeFourOfAKind(stack) )
        {
            stack.clear();

            notifyPlay( player, "Tsshhh...", stack );
       };
    }


    private boolean maybe10(Stack<Card> stack)
    {
        return stack.peek().getValue() == 10;
    }

    private boolean maybeFourOfAKind(Stack<Card> stack)
    {
        Card lastCard = null;
        int numOfAKind = 0;

        for ( int index = stack.size() - 1; index > 0; index-- )
        {
            Card nextCard = stack.elementAt( index );
            if ( nextCard.getValue() != 3 )
            {
                if ( lastCard == null )
                {
                    lastCard = nextCard;
                    numOfAKind = 1;
                }
                else if ( nextCard.getValue() == lastCard.getValue() )
                {
                    numOfAKind++;

                    if ( numOfAKind == 4 )
                    {
                        notifyPlay( "*", "Four of a kind", "" );
                        return true;
                    }
                }
                else
                {
                    break;
                }
            }

        }
        return false;
    }
}
