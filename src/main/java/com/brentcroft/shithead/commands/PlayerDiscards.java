package com.brentcroft.shithead.commands;

import java.util.Stack;

import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;

@Component
public class PlayerDiscards implements Command< DiscardContext >
{

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

        notifyAction( player, "discards", discard.toText() );
    }
}
