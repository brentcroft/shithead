package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscardCommand implements Command<DiscardContext> {

    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(DiscardContext context) {
        context
                .getDiscard()
                .getCards()
                .forEach( card -> {
                    if ( context.getPlayer().playCard( card ) )
                    {
                        context.getGameModel().getStack().push( card );
                    }
                } );

        notifyPlay( context.getPlayer(), "plays", context.getDiscard().toText() );
    }
}
