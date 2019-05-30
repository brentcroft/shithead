package com.brentcroft.shithead.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.Player;

@Component
public class PlayerPicksUpTheStack implements Command< DiscardContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();;

    @Override
    public void action( DiscardContext context )
    {
        pickUpTheStack(
                context.getGameModel().getStack(),
                context.getPlayer() );
    }

    void pickUpTheStack( Stack< Card > stack, Player player )
    {
        List< Card > pickedUp = new ArrayList<>();

        while ( !stack.isEmpty() )
        {
            Card card = stack.pop();

            pickedUp.add( card );

            player.addCard( Player.ROW.HAND, card );
        }

        if (Objects.nonNull(notifier))
        {
            notifier.notifyAction(player, "picks up the stack", pickedUp);
        }
    }
}
