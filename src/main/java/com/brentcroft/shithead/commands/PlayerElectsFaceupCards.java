package com.brentcroft.shithead.commands;


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


    void electFaceupCards( Player player, Discard discard )
    {
        player.electCards( discard.getCards() );
    }
}
