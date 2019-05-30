package com.brentcroft.shithead.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

import java.util.Objects;


@Component
public class MaybeRemoveFinishedPlayer implements Command< PlayerContext >
{
    @Autowired
    ActionNotifier notifier = ActionNotifier.getNotifier();

    @Override
    public void action( PlayerContext context )
    {
        removeFinishedPlayer(
                context.getGameModel(),
                context.getPlayer() );
    }

    void removeFinishedPlayer( GameModel gameModel, Player player )
    {
        if ( !player.hasCards() )
        {
            gameModel.getPlayers().remove( player );

            gameModel.getLastPlayer().pop();

            if (Objects.nonNull(notifier))
            {
                notifier.notifyAction(player, "has finished", gameModel.getPlayers().size());
            }
        }
    }
}
