package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Cards.Card;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TopUpCardsCommand implements Command<DiscardContext> {

    public static final int MIN_HAND_SIZE_TO_PICKUP = 3;


    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(DiscardContext context) {
        playerTopsUpHand(
                context.getGameModel(),
                context.getPlayer(),
                context.getDiscard().getCards() );
    }


    private void playerTopsUpHand(GameModel gameModel, Player player, List<Card> cards )
    {
        while ( !gameModel.getDeck().isEmpty() && ( MIN_HAND_SIZE_TO_PICKUP - player.countCards( Player.ROW.HAND ) > 0 ) )
        {
            Card card = gameModel.getDeck().next();

            if ( ! gameModel.getStack().isEmpty() && card.getValue() ==  gameModel.getStack().peek().getValue() )
            {
                notifyPlay( player, "slides", card );

                gameModel.getStack().push( card );

                continue;
            }

            player.addCard( Player.ROW.HAND, card );

            notifyPlay( player, "receives", card );
        }
    }
}
