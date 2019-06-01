package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;

public class IncrementCounters implements Command<DiscardContext> {
    @Override
    public void action(DiscardContext discardContext) {
        discardContext.getGameModel().incrementTurn();
    }
}
