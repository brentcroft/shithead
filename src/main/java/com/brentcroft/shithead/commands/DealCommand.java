package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

import java.util.stream.IntStream;

public class DealCommand implements Command<GameContext> {

    public static final int NUM_CARDS_COLUMNS = 3;

    @Override
    public void action(GameContext context) {
        deal( context.getGameModel() );
    }



    public void deal(GameModel gameModel) {
        if (gameModel.getDeck().dealt()) {
            throw new RuntimeException(Messages.CARDS_ALREADY_DEALT);
        }

        Player.ROW.forEach(
                row -> {
                    IntStream
                            .range(0, NUM_CARDS_COLUMNS)
                            .forEachOrdered(
                                    column -> gameModel
                                            .getPlayers()
                                            .forEach(player -> player.addCard(row, gameModel.getDeck().next())));
                });
    }

}
