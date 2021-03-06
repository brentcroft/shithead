package com.brentcroft.shithead;

import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.context.PlayerContext;
import com.brentcroft.shithead.model.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardGame implements GamePlay
{
    private final GameModel gameModel = new GameModel();


    public void addPlayer( Player player )
    {
        ADD_PLAYER.executeUsing( new PlayerContext( gameModel, player ) );
    }
    
    public void dealCards()
    {
        DEAL.executeUsing( new GameContext( gameModel ) );
    }



    public void detectFirstPlayer()
    {
        FIRST_PLAYER.executeUsing( new GameContext( gameModel ) );
    }


    public void playerDiscard( Discard discard )
    {
        DiscardContext context = new DiscardContext(
                gameModel,
                gameModel.getPlayer( discard.getPlayerName() ),
                discard );

        discard.debacinate(context.getPlayer().getBlindCards());

        CHECK_PLAYER_AND_CARDS.executeUsing( context );

        PLAYER_DISCARDS.executeUsing( context );

        if ( context.isValid() )
        {
            PLAYER_TOPS_UP.executeUsing( context );
        }
        else
        {
            PLAYER_PICKS_UP_STACK.executeUsing( context );
        }

        PLAYER_ENDS_TURN.executeUsing( context );
    }
}
