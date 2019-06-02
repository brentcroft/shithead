package com.brentcroft.shithead.commands;

import java.util.Objects;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;

@Component
public class PlayerDiscards implements Command< DiscardContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();

    @Override
    public void action( DiscardContext context )
    {
        discard(
                context.getGameModel().getStack(),
                context.getPlayer(),
                context.getDiscard() );
    }

    void discard( Stack< Card > stack, Player player, Discard discard )
    {
        discard
                .getCards()
                .forEach( card -> {
                    if ( player.playCard( card ) )
                    {
                        stack.push( card );
                    }
                } );

        if ( Objects.nonNull( notifier ) )
        {
            notifier.notifyAction( player, "discards", discard.getCards() );
        }
    }
}
