package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElectFaceupCardsCommand implements Command<DiscardContext> {

    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(DiscardContext context) {
        electFaceupCards(context.getPlayer(), context.getDiscard() );
    }


    protected void electFaceupCards(Player player, Discard discard )
    {
        if ( player.hasCardsInHand() )
        {
            // ok
        }
        else if ( player.hasCardsInFaceUp() )
        {
            if ( player.hasCards( Player.ROW.FACEUP, discard.getCards() ) )
            {
                player.electCards( discard.getCards() );
            }
            else
            {
                throw new RuntimeException( "Discard cards not in faceup" );
            }
        }
        else
        {
            if ( player.hasCards( Player.ROW.BLIND, discard.getCards() ) )
            {
                player.electCards( discard.getCards() );
            }
            else
            {
                throw new RuntimeException( "Discard cards not in blind" );
            }
        }

    }
}
