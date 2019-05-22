package com.brentcroft.shithead.commands;


import static com.brentcroft.shithead.context.Messages.DISCARD_NOT_IN_BLIND;
import static com.brentcroft.shithead.context.Messages.DISCARD_NOT_IN_FACEUP;

import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;

@Component
public class PlayerElectsFaceupCards implements Command< DiscardContext >
{

    @Override
    public void action( DiscardContext context )
    {
        electFaceupCards( context.getPlayer(), context.getDiscard() );
    }


    // TODO: get the rules right
    void electFaceupCards( Player player, Discard discard )
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
                throw new RuntimeException( DISCARD_NOT_IN_FACEUP);
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
                throw new RuntimeException( DISCARD_NOT_IN_BLIND );
            }
        }

    }
}
