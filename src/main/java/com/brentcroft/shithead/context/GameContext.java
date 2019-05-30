package com.brentcroft.shithead.context;

import com.brentcroft.shithead.model.GameModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameContext
{
    private final GameModel gameModel;
    private boolean finished = false;

    public GameContext( GameModel gameModel )
    {
        this.gameModel = gameModel;
    }

}
