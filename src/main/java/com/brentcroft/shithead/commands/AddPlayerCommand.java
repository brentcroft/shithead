package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

import static com.brentcroft.shithead.commands.Messages.PLAYER_ALREADY_EXISTS;

public class AddPlayerCommand implements Command<PlayerContext> {
    @Override
    public void action(PlayerContext context) {
        addPlayer(context.getGameModel(), context.getPlayer());
    }



    public void addPlayer(GameModel gameModel, Player player) {
        if (gameModel.getDeck().dealt()) {
            throw new RuntimeException(Messages.CARDS_ALREADY_DEALT);
        }

        if (gameModel.hasPlayer(player.getName())) {
            throw new RuntimeException(PLAYER_ALREADY_EXISTS);
        }

        gameModel.getPlayers().add(player);
    }


}
