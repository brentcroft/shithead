package com.brentcroft.shithead.commands;

import java.util.List;
import java.util.Objects;

import com.brentcroft.shithead.model.Discard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;


@Component
public class PlayerTopsUpCards implements Command< DiscardContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();

    MaybeClearTheStack mcts = new MaybeClearTheStack();

    @Override
    public void action( DiscardContext context )
    {
        playerTopsUpCards( context);
    }

    void playerTopsUpCards( DiscardContext context  )
    {
        GameModel gameModel = context.getGameModel();
        Player player = context.getPlayer();
        Discard discard  = context.getDiscard();



        while ( !gameModel.getDeck().isEmpty()
                && ( GameModel.MIN_HAND_SIZE_TO_PICKUP > player.countCards( Player.ROW.HAND ) ) )
        {
            Card card = gameModel.getDeck().next();

            if ( discard.rolloverOnPickup()
                    && !gameModel.getStack().isEmpty()
                    && card.getValue() == gameModel.getStack().peek().getValue() )
            {

                if (Objects.nonNull(notifier))
                {
                    notifier.notifyAction(player, "slides", card);
                }

                gameModel.getStack().push( card );

                mcts.action(context);

                continue;
            }

            player.addCard( Player.ROW.HAND, card );

            if (Objects.nonNull(notifier))
            {
                notifier.notifyAction(player, "tops up", card);
            }
        }
    }
}
