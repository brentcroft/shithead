package com.brentcroft.shithead.context;

import com.brentcroft.shithead.model.GameModel;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public class GameContext {
    private final GameModel gameModel;

    public GameContext(GameModel gameModel)
    {
        this.gameModel = gameModel;
    }

}
