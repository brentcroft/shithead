package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Cards.Card;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PickUpTheStackCommand implements Command<DiscardContext> {

    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(DiscardContext context) {
        List<Card> pickedUp = new ArrayList<>();
        while (!context.getGameModel().getStack().isEmpty()) {
            Card card = context.getGameModel().getStack().pop();

            pickedUp.add(card);

            context.getPlayer().addCard(Player.ROW.HAND, card);
        }

        notifyPlay(context.getPlayer(), "picks up", pickedUp);
    }


}
