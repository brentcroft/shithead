package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MaybeRemoveFinishedPlayerCommand implements Command<PlayerContext> {

    @Autowired
    private CommandNotifier commandNotifier;

    @Override
    public void action(PlayerContext context) {
        removeFinishedPlayer(
                context.getGameModel(),
                context.getPlayer() );
    }


    protected void removeFinishedPlayer(GameModel gameModel, Player player )
    {
        if ( !player.hasCards() )
        {
            gameModel.getPlayers().remove( player );

            gameModel.getLastPlayer().pop();

            notifyPlay( player, "finished", gameModel.getPlayers().size() );
        }
    }

}
