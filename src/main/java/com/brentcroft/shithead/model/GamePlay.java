package com.brentcroft.shithead.model;

import static com.brentcroft.shithead.chain.Guards.onlyIf;

import com.brentcroft.shithead.chain.Chain;
import com.brentcroft.shithead.commands.AddPlayer;
import com.brentcroft.shithead.commands.CheckGameFinished;
import com.brentcroft.shithead.commands.CheckPlayerCards;
import com.brentcroft.shithead.commands.CheckPlayerDiscard;
import com.brentcroft.shithead.commands.CheckPlayerTurn;
import com.brentcroft.shithead.commands.ChooseNextPlayer;
import com.brentcroft.shithead.commands.ChoosePreviousPlayer;
import com.brentcroft.shithead.commands.DealTheCards;
import com.brentcroft.shithead.commands.DetectFirstPlayer;
import com.brentcroft.shithead.commands.MaybeClearTheStack;
import com.brentcroft.shithead.commands.MaybeRemoveFinishedPlayer;
import com.brentcroft.shithead.commands.PlayerDiscards;
import com.brentcroft.shithead.commands.PlayerElectsFaceupCards;
import com.brentcroft.shithead.commands.PlayerPicksUpTheStack;
import com.brentcroft.shithead.commands.PlayerTopsUpCards;
import com.brentcroft.shithead.context.DiscardContext;
import com.brentcroft.shithead.context.GameContext;
import com.brentcroft.shithead.context.PlayerContext;

public interface GamePlay
{

    Chain< PlayerContext > ADD_PLAYER = Chain.of( PlayerContext.class )
            .firstDo( new AddPlayer() )
            .build();


    Chain< GameContext > DEAL = Chain.of( GameContext.class )
            .firstDo( new DealTheCards() )
            .build();

    // TODO: players can swap hand and faceup cards
    
    Chain< GameContext > FIRST_PLAYER = Chain.of( GameContext.class )
            .firstDo( new DetectFirstPlayer() )
            .build();

    //
    Chain< DiscardContext > CHECK_PLAYER_AND_CARDS = Chain.of( DiscardContext.class )
            .firstDo( new CheckPlayerTurn() )
            .andThen( new CheckPlayerCards() )
            .andThen( new CheckPlayerDiscard() )
            .build();

    Chain< DiscardContext > PLAYER_DISCARDS_AND_TOPS_UP = Chain.of( DiscardContext.class )
            .firstDo( new PlayerElectsFaceupCards() )
            .andThen( new PlayerDiscards() )
            .andThen( new MaybeClearTheStack() )
            .andThen( new PlayerTopsUpCards() )
            .andThen( context -> {

            } )
            .andThen( new ChooseNextPlayer(), onlyIf( c -> !c.getGameModel().getStack().isEmpty() ) )
            .build();

    Chain< DiscardContext > PLAYER_PICKS_UP_STACK = Chain.of( DiscardContext.class )
            .firstDo( new PlayerPicksUpTheStack() )
            .andThen( new ChoosePreviousPlayer() )
            .build();

    Chain< DiscardContext > PLAYER_ENDS_TURN = Chain.of( DiscardContext.class )
            .firstDo( new MaybeRemoveFinishedPlayer() )
            .andThen( new CheckGameFinished() )
            .build();
}
