package com.brentcroft.shithead.context;

import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import lombok.Getter;

@Getter
public class DiscardContext extends PlayerContext{
    private final Discard discard;
    private boolean valid;
    public DiscardContext(GameModel gameModel, Player player, Discard discard)
    {
        super(gameModel, player);
        this.discard = discard;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
