package com.brentcroft.shithead;

import com.brentcroft.shithead.chain.Chain;
import com.brentcroft.shithead.commands.*;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import lombok.Getter;

import static com.brentcroft.shithead.chain.Guards.onlyIf;

public class StandardGame {

    @Getter
    private final GameModel gameModel = new GameModel();


    private final Chain<PlayerContext> addPlayerContext = Chain
            .createChain()
            .withContextType(PlayerContext.class)
            .startingWith(new AddPlayerCommand())
            .build();


    private final Chain<GameContext> dealContext = Chain
            .createChain()
            .withContextType(GameContext.class)
            .startingWith(new DealCommand())
            .build();

    private final Chain<GameContext> firstPlayerContext = Chain
            .createChain()
            .withContextType(GameContext.class)
            .startingWith(new DetectFirstPlayerCommand())
            .build();


    private final Chain<DiscardContext> checkPlayerAndCards = Chain
            .createChain()
            .withContextType(DiscardContext.class)
            .startingWith(new CheckPlayerCardsCommand())
            .andThen(new CheckDiscardCommand())
            .build();

    private final Chain<DiscardContext> playerDiscardsAndTopsUp = Chain
            .createChain()
            .withContextType(DiscardContext.class)
            .startingWith(new ElectFaceupCardsCommand())
            .andThen(new DiscardCommand())
            .andThen(new MaybeClearTheStackCommand())
            .andThen(new TopUpCardsCommand())
            .andThen(new ChooseNextPlayerCommand(), onlyIf(c -> !c.getGameModel().getStack().isEmpty()))
            .build();

    private final Chain<DiscardContext> playerPicksUpStack = Chain
            .createChain()
            .withContextType(DiscardContext.class)
            .startingWith(new PickUpTheStackCommand())
            .andThen(new ChoosePreviousPlayerCommand())
            .build();

    private final Chain<DiscardContext> finishChain = Chain
            .createChain()
            .withContextType(DiscardContext.class)
            .startingWith(new MaybeRemoveFinishedPlayerCommand())
            .andThen(new CheckGameFinishedCommand())
            .build();



    public void addPlayer(Player player) {
        addPlayerContext.executeUsing(new PlayerContext(gameModel, player));
    }


    public void detectFirstPlayer()
    {
        firstPlayerContext.executeUsing(new GameContext(gameModel));
    }

    public void deal()
    {
        dealContext.executeUsing(new GameContext(gameModel));
    }

    public void play(Discard discard) {

        DiscardContext context = new DiscardContext(gameModel, gameModel.getPlayer(discard.getPlayerName()), discard);

        checkPlayerAndCards.executeUsing(context);

        if (context.isValid()) {

            playerDiscardsAndTopsUp.executeUsing(context);

        } else {

            playerPicksUpStack.executeUsing(context);

        }

        finishChain.executeUsing(context);
    }


    public void play() {
        try {
            while (gameModel.getPlayers().size() > 0) {

                Player player = gameModel.getCurrentPlayer();

                Discard discard = new Discard(
                        player.getName(),
                        player.chooseCards(gameModel.getSelector())
                );

                play(discard);
            }
        } catch (ShitheadException e) {
            System.out.println(e.getMessage());
        }
    }

}
