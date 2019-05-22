package com.brentcroft.shithead.commands;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;


@Component
public class PlayerTopsUpCards implements Command< DiscardContext >
{

    @Override
    public void action( DiscardContext context )
    {
        playerTopsUpCards(
                context.getGameModel(),
                context.getPlayer(),
                context.getDiscard().getCards() );
    }

    void playerTopsUpCards( GameModel gameModel, Player player, List< Card > cards )
    {
        while ( !gameModel.getDeck().isEmpty()
                && ( GameModel.MIN_HAND_SIZE_TO_PICKUP > player.countCards( Player.ROW.HAND ) ) )
        {
            Card card = gameModel.getDeck().next();

            if ( !gameModel.getStack().isEmpty() && card.getValue() == gameModel.getStack().peek().getValue() )
            {
                notifyAction( player, "slides", card );

                gameModel.getStack().push( card );

                continue;
            }

            player.addCard( Player.ROW.HAND, card );

            notifyAction( player, "tops up", card );
        }
    }
}
