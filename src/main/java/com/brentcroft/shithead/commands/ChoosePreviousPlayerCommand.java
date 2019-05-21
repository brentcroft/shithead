package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChoosePreviousPlayerCommand implements Command<GameContext> {

    @Autowired
    private CommandNotifier commandNotifier;


    @Override
    public void action(GameContext context) {
        choosePreviousPlayer( context.getGameModel() );
    }


    private void choosePreviousPlayer(GameModel gameModel)
    {
        Player player = gameModel.getLastPlayer().pop();

        gameModel.setCurrentPlayer(player);

        notifyPlay( player, "goes again", "" );
    }
}
