package com.brentcroft.shithead.commands;

import java.util.Objects;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.Card;

@Component
public class MaybeClearTheStack implements Command< GameContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();

    @Override
    public void action( GameContext context )
    {
        maybeClearTheStack( context.getGameModel().getStack() );
    }
    
    void maybeClearTheStack( Stack< Card > stack )
    {
        if ( !stack.isEmpty() && ( maybe10( stack ) || maybeFourOfAKind( stack ) ) )
        {
            if (Objects.nonNull(notifier))
            {
                notifier.notifyAction("*", "Tsshhh...", stack);
            }

            stack.clear();
        }
    }

    boolean maybe10( Stack< Card > stack )
    {
        return stack.peek().getValue() == 10;
    }


    boolean maybeFourOfAKind( Stack< Card > stack )
    {
        Card lastCard = null;
        int numOfAKind = 0;

        for ( int index = stack.size() - 1; index >= 0; index-- )
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
                        if (Objects.nonNull(notifier))
                        {
                            notifier.notifyAction("*", "Four of a kind", "");
                        }
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
