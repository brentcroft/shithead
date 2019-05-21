package com.brentcroft.shithead.context;

import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import lombok.Getter;

@Getter
public class PlayerContext extends GameContext {
    private final Player player;

    public PlayerContext(GameModel gameModel, Player player) {
        super(gameModel);
        this.player = player;
    }
}
