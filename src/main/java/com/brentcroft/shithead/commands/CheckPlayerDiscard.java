package com.brentcroft.shithead.commands;

import static com.brentcroft.shithead.context.Messages.PLAYER_COULD_HAVE_PLAYED;

import java.util.List;

import com.brentcroft.shithead.chain.Command;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;

public class CheckPlayerDiscard implements Command< DiscardContext >
{
    @Override
    public void action( DiscardContext context )
    {

        context.setValid(
                validPlayForStack(
                        context.getGameModel(),
                        context.getPlayer(),
                        context.getDiscard().getCards() ) );
    }

    boolean validPlayForStack( GameModel gameModel, Player player, List< Card > cards )
    {
        if ( cards.isEmpty() )
        {
            return false;
        }
        else if ( gameModel.getSelector().test( cards.get( 0 ) ) )
        {
            return true;
        }

        verifyPlayerCannotPlay( player, cards );

        return false;
    }

    void verifyPlayerCannotPlay( Player player, List< Card > cards )
    {
        boolean couldHavePlayed = false;

        if ( couldHavePlayed )
        {
            throw new RuntimeException( PLAYER_COULD_HAVE_PLAYED );
        }
    }
}
