(function(){
    if (!Array.prototype.first)
    {
       Array.prototype.first = function(predicate)
       {
         "use strict";
         if (this == null)
           throw new TypeError();
         if (typeof predicate != "function")
           throw new TypeError();

         for (var i = 0; i < this.length; i++) {
           if (predicate(this[i])) {
             return this[i];
           }
         }
         return null;
       }
    }
    if (!Array.prototype.removeFirst)
    {
       Array.prototype.removeFirst = function( predicate )
       {
         "use strict";
         if (this == null)
           throw new TypeError();
         if (typeof predicate != "function")
           throw new TypeError();

         for (var i = 0; i < this.length; i++)
         {
           var item = this[i];
           if ( predicate( item ) )
           {
             this.splice(i, 1 );
             return true;
           }
         }
         return false;
       }
    }
})();


function JSLog(){
    this.buffer = null;
}
JSLog.prototype.write = function( s ) {

    if ( this.buffer !== null )
    {
        this.buffer += "\n" + s;
    }

    if ( typeof( document ) !== "undefined" )
    {
        //console.log( s );
    }
    else
    {
        print( s );
    }
};
JSLog.prototype.start = function( s ) { this.buffer = ""; };
JSLog.prototype.capture = function() {
    if ( this.buffer !== null )
    {
        var b = "" + this.buffer;
        this.buffer = null;
        return b;
    }
    else
    {
        return "empty!"
    }
};

var log = new JSLog();


function Stats()
{
    this.choices = {
            freq: 0,
            count: 0,
            sum: 0
        };
    this.winners = [];
}


function Game( players, cards )
{
    this.cards = cards;

    this.stack = [];
    this.discard = [];
    this.trash = [];

    this.players = players;
    this.stepCount = 0;
    this.cardChoices = null;

    this.stats = new Stats();

    this.state = "new";
}

Game.prototype.toString = function( ){
    var w = "";
    this.stats.winners.forEach( function( player ){
        w += ( ( w.length === 0 ) ? "" : ", " ) + player.name + "(" + player.finishedStepCount + ")";
    });
    return "steps=[" + this.stepCount +
        "], choices[ freq=" + this.stats.choices.freq + ", count=" + this.stats.choices.count +
        ", sum=" + this.stats.choices.sum +
        "], winners=[" + w +
        "]";
};

Game.prototype.onStart = function( game ){};
Game.prototype.onStop = function( game ){};
Game.prototype.onFinish = function( game ){};

Game.prototype.topDiscard = function() { return this.discard.length > 0 ? this.discard[ 0 ] : null; }
Game.prototype.takeNextCardOffStack = function() {
        if ( this.stack.length > 0 ) {
//            var p = this.stack.length;
//            var card = this.stack[ p - 1 ];
//            this.stack.splice( p - 1, 1 );
            var card = this.stack[ 0 ];
            this.stack.splice( 0, 1 );
            return card;
        }
        return null;
    };
Game.prototype.topFourOfAKind = function() {
        var gdl = this.discard.length;
        if ( gdl >= 4 ) {
            var value = null;
            for ( var i = 1 ; i <= 4 ; i++ ) {
                if ( value === null ) {
                    value = this.discard[ gdl - i ].value;
                } else if ( value !== this.discard[ gdl - i ].value ) {
                    return false;
                }
            }
            return value;
        }
    };
Game.prototype.linkPlayers = function(){
        var q = this.players.length;
        var players = this.players;
        players.forEach( function( player ) {
            var p = players.indexOf( player )
            player.nextPlayer = p < (q - 1) ? p + 1 : 0;
            player.previousPlayer = p > 0 ? p - 1 : (q - 1);
            player.autoPlay = true;
        });
    };
Game.prototype.findFirstPlayer = function( ) {
        var firstPlayer = null;
        for ( var i = 3; i <= 14; i++ ) {
            firstPlayer = this.players.first( function( player ) { return player.hand.first( function( card ) { return ( card.value === i ); }); } );
            if ( firstPlayer != null ) {

                // player must play declared first card
                firstPlayer.requiredFirstPlay = i;

                return firstPlayer;
            }
        }
        return null;
    };


Game.prototype.deal = function( ) {

        this.players.forEach( player => {
            player.blind = [];
            player.faceup = [];
            player.hand = [];
        });

        // deal blind cards
        for ( var i = 1; i <= 3; i++ ) {
            this.players.forEach( player => {
                var numBlind = player.blind.length;
                if ( numBlind < 3 ) {
                    var card = game.takeNextCardOffStack();
                    if ( card ) {
                        player.blind[ numBlind ] = card;
                    }
                }
            });
        }
        // deal faceup cards
        for ( var i = 1; i <= 3; i++ ) {
            this.players.forEach( player => {
                var numFaceup = player.faceup.length;
                if ( numFaceup < 3 ) {
                    var card = game.takeNextCardOffStack();
                    if ( card ) {
                        player.faceup[ numFaceup ] = card;
                    }
                }
            });
        }
        // deal hand cards
        for ( var i = 1; i <= 3; i++ ) {
            this.players.forEach( player => {
                var numHand = player.hand.length;
                if ( numHand < 3 ) {
                    var card = game.takeNextCardOffStack();
                    if ( card ) {
                        player.hand[ numHand ] = card;
                    }
                }
            } );
        }
    };

Game.prototype.cardSetChoices = function( cardSet, topDiscard ){
    var value = (topDiscard) ? topDiscard.value : 0;
    var lookupCard = {};
    cardSet.forEach( function( card ){
        if ( card.value >= value || card.value === 2 || card.value === 10 ) {
            var group = lookupCard[ card.value ];
            if ( group ) {
                group.push( card );
            } else {
                lookupCard[ card.value ] = [ card ];
            }
        }
    });

    var choices = [];
    for (var k in lookupCard ) {
        choices.push( lookupCard[ k ] );
    }

    choices.sort(function( setA, setB ) {
        return ( setA[ 0 ].value === setB[ 0 ].value ) ? 0
            :( setA[ 0 ].value === 10 ) ? 1
            :( setB[ 0 ].value === 10 ) ? -1
            :( setA[ 0 ].value === 2 ) ? 1
            :( setB[ 0 ].value === 2 ) ? -1
            :( setA[ 0 ].value - setB[ 0 ].value );
    });

    return choices;
};
Game.prototype.setCardsSelectable = function( select, choices ) {
    var group = 1;
    if (choices) {
        choices.forEach( function( cardSet ){
            cardSet.forEach( function( card ){
                if ( select ){
                    card.selectable = true;
                    card.selectgroup = group;
                    //log.write( "Set selectable: " + card + ", group: " + group );
                } else {
                    delete card.selectable;
                    delete card.selectgroup;
                    //log.write( "Deleted selectable: " + card + ", group: " + group );
                }
            });
            group++;
        });
    }
};
Game.prototype.setCardsSelected = function( select, cardSet ) {
    if (cardSet) {
        cardSet.forEach( function( card ){
            if ( select ){
                card.selected = true;
            } else {
                delete card.selected;
            }
        });
    }
};

Game.prototype.isPlayerBlind = function( player ){
    return ( !player.hand || player.hand.length === 0 ) &&
           ( !player.faceup || player.faceup.length === 0 );
};
// TODO: move to Player
Game.prototype.playerChoices = function( player ) {

    if ( !player.hand || player.hand.length === 0 ) {
        if ( !player.faceup || player.faceup.length === 0 ) {
            if ( !player.blind || player.blind.length === 0 ) {
                log.write( player.name + " should have finished already; no cards!" );
                return null;
            } else {
                var choices = [];
                player.blind.forEach( function( card ){ choices.push( [ card ] ); });
                return choices;
            }
        }
        else
            return this.cardSetChoices( player.faceup, this.topDiscard() );
    }
    else
        return this.cardSetChoices( player.hand, this.topDiscard() );
};



Game.prototype.isFinished = function( ) {
        var numFinished = 0;
        this.players.forEach( function( player ){ numFinished += ( player.isFinished() ? 1 : 0 ); });
        return numFinished >= ( this.players.length - 1);
    };

Game.prototype.init = function( ) {

    this.stepCount = 0;
    this.stats = new Stats();

    // while the players are linked, they can't be stringified
    this.linkPlayers();

    // clean the cards
    this.cards.forEach( function( card ) { card.clean(); });

    // cloning the cards array
    this.stack = this.cards.slice( 0 );
    this.discard = [];
    this.trash = [];


    this.deal();

    log.write( "cards are dealt." );

    this.state = "prepare";
};

Game.prototype.prepare = function( ) {
    // multi steps

    for ( var i = 0; i < 3; i++ )
    {
        var notPrepared = false;

        this.players.forEach( function( p ){
            p.prepareCards( );
            if( p.notPrepared )
            {
                notPrepared = true;
            }
        } );

        if ( !notPrepared )
        {
            log.write( "players prepared after [" + i + "] round" + ( i == 1 ? "" : "s" ) + "." );

            this.state = "identifyFirstPlayer";

            return;
        }
    }

    log.write( "not all players prepared after 3 rounds." );

    this.state = "identifyFirstPlayer";
};


Game.prototype.identifyFirstPlayer = function( ) {

    this.nextPlayer = this.findFirstPlayer( );

    // set cardChoices, and selectable on next player choices
    if ( this.nextPlayer )
    {
        this.cardChoices = this.playerChoices( this.nextPlayer );
        this.setCardsSelectable( true, this.cardChoices );
        this.setCardsSelected( true, this.nextPlayer.makeChoice( this.cardChoices ) );
    }

    this.isStarted = true;
    this.firstGo = true;

    this.onStart( this );

    log.write( "first player is identified" );

    this.state = "play";
};



Game.prototype.play = function( ){

        if ( !this.isStarted )
        {
            throw "Game is not started.";
        }

        if ( this.isFinished() )
        {
            throw "Game is finished.";
        }



        this.stepCount++;

        var topCard = this.topDiscard();
        if ( topCard ) {
            topCard.topDiscard = true;
        }


        var player = this.nextPlayer;

        if ( player.isFinished() )
        {
            log.write( player.name + " has finished" );

            player = this.players[ player.nextPlayer ];
        }



        log.write( "Step [" + this.stepCount + "]" );
        log.write( "Top [" + topCard + "]" );

        if ( player.hand.length > 0 )
        {
            log.write( player.name + " in hand [" + player.hand + "]" );
        }
        else if ( player.faceup.length > 0 )
        {
            log.write( player.name + " in faceup [" + player.faceup + "]" );
        }
        else
        {
            log.write( player.name + " blind" );
        }



        // TODO:
        var card = null;

        // don't expect to update card choices, but just in case
        this.cardChoices = this.cardChoices ? this.cardChoices : this.playerChoices( player );

        // collect game stats
        if ( this.cardChoices &&
        (   ( this.cardChoices.length > 1)
            || (this.cardChoices.length == 1 && this.cardChoices[ 0 ].length > 1 )))
        {
            var choices = this.stats.choices;

            choices.freq ++;
            choices.count += (this.cardChoices.length - 1);

            this.cardChoices.forEach( function( cardSet ){
                choices.sum += ( cardSet.length - 1);
            });
        }


        if ( player.isBlind() ) {

            // random pick
            //card = [ player.blind[ Math.floor( Math.random() * player.blind.length ) ] ];
            card = [ player.blind[ 0 ] ];

        } else if ( this.cardChoices && this.cardChoices.length > 0 ) {


            // player may have already made their choice
            // indicated by the selected attribute
            var playerSelected = [];
            this.cardChoices.first( function( cs ){
                cs.forEach( function( c ){
                    if ( c.selected ) {
                        playerSelected.push( c );
                    }
                });
                return playerSelected.length > 0;
            });

            if ( playerSelected.length > 0 )
            {
                card = playerSelected;
            }
            else
            {
                // auto player pick
                card = player.makeChoice( this.cardChoices );
            }


            if ( player.requiredFirstPlay  )
            {
                // check card has value
                if ( player.requiredFirstPlay !== card[ 0 ].value )
                {
                    throw "Player[ " + player.name + " ] must play a card of value [" + Card.prototype.keyValue( player.requiredFirstPlay ) + "].";
                }

                delete player.requiredFirstPlay;
            }



            // clear any selected values now the choice is made
            this.cardChoices.forEach( function( cs ){ cs.forEach( function(c){ delete c.selected; }); });

        } else {

            // if player.hand is empty and not blind
            // then can select "invalid" card group from faceup into hand
            // so need another player choice function: selectFaceupCardsToPromote

            if ( player.isHandEmpty() && !player.isBlind() )
            {
                var cardsToPromote = player.selectFaceupCardsToPromote();

                log.write( player.name + " promotes [" + cardsToPromote + "]" );

                if ( cardsToPromote ) {
                    cardsToPromote.forEach( function( c ) {
                        player.discardCard( c );
                        player.receiveCard( c );
                    });
                }
            }
        }



        if ( card == null || card.length === 0 )
        {
            log.write( player.name + " picks discard pile [" + this.discard + "]." );

            while ( this.discard.length > 0 )
            {
                var discard = this.discard.pop( );
                player.receiveCard( discard );
            }

            this.nextPlayer = this.players[ player.previousPlayer ];
        }
        else if ( card[ 0 ].value === 10 )
        {
            log.write( player.name + " discards [" + card + "]." );

            var game = this;
            card.forEach( function( c ) {
                player.discardCard( c );
                game.discard.push( c );
            });

            log.write( "Tssshhhh... 10 - discard to trash [" + this.discard + "]." );

            while ( this.discard.length > 0 )
            {
                this.trash.push( this.discard.pop( ) );
            }

            // rule: player tops up hand to max of 3
            if ( player.hand.length < 3 )
            {
                var newCard = this.takeNextCardOffStack();

                if ( newCard )
                {
                    log.write( player.name + " picks [" + newCard + "]." );
                    player.receiveCard( newCard );
                }
            }
            // play again: not changing this.nextPlayer
        }
        else if ( topCard && card[ 0 ].value !== 2 && card[ 0 ].value < topCard.value )
        {
            log.write( player.name + " illegal discard [" + card + "]." );
            log.write( player.name + " picks discard pile [" + this.discard + "]." );

            // move card from blind or faceup to hand
            card.forEach( function( c ){
                player.discardCard( c );
                player.receiveCard( c );
            });

            while ( this.discard.length > 0 ) {
                var discard = this.discard.pop( );
                player.receiveCard( discard );
            }

            this.nextPlayer = this.players[ player.previousPlayer ];
        }
        else
        {
            if ( this.firstGo )
            {
                this.firstGo = false;
            }

            log.write( player.name + " discards [" + card + "]." );


            var game = this;
            card.forEach( function( c ) {
                player.discardCard( c );
                game.discard.push( c );
            });


            var playAgain = false;

            if ( this.topFourOfAKind() )
            {
                log.write( "Tssshhhh... 4 of a kind - discard to trash [" + this.discard + "]." );

                while ( this.discard.length > 0 ) {
                    this.trash.push( this.discard.pop( ) );
                }
                // play again: not changing this.nextPlayer
            }
            else
            {
                this.nextPlayer = this.players[ player.nextPlayer ];
            }

            // rule: player tops up hand to max of 3
            if ( player.hand.length < 3 )
            {
                var game = this;
                card.forEach( function() {
                    var newCard = game.takeNextCardOffStack();
                    while ( newCard != null && card != null && newCard.value === card.value )
                    {
                        log.write( player.name + " picks and discards [" + newCard + "]." );

                        game.discard.push( newCard );
                        newCard = game.takeNextCardOffStack();
                    }

                    if ( newCard )
                    {
                        log.write( player.name + " picks [" + newCard + "]." );

                        player.receiveCard( newCard );
                    }
                } );
            }
        }


        // clear selectable on old player choices
        this.setCardsSelectable( false, this.cardChoices );

        // drop old card choices
        this.cardChoices = null;

        //
        player.clean();

        // maybe extract finished player
        if ( player.isFinished() )
        {
            // detach this player from player previous/next links
            var previousPlayer = this.players[ player.previousPlayer ];
            var nextPlayer = this.players[ player.nextPlayer ];

            previousPlayer.nextPlayer = player.nextPlayer;
            nextPlayer.previousPlayer = player.previousPlayer;

            // case that player laid a ten or 4 of a kind
            if ( player === this.nextPlayer )
            {
                this.nextPlayer = nextPlayer;
            }

            player.finished = true;
            player.finishedStepCount = this.stepCount;

            log.write( player.name + " has finished at step " + this.stepCount );

            // stats
            this.stats.winners.push( player );
        }


        // set selectable on next player choices
        if ( this.nextPlayer )
        {
            this.cardChoices = this.playerChoices( this.nextPlayer );
            this.setCardsSelectable( true, this.cardChoices );
            this.setCardsSelected( true, this.nextPlayer.makeChoice( this.cardChoices ) );

            log.write( this.nextPlayer.name + " is the next player." );
        }

        if ( topCard )
        {
            delete topCard.topDiscard;
        }

        // now look at the current top discard
        topCard = this.topDiscard();

        if ( topCard )
        {
            topCard.topDiscard = true;
        }

        if ( this.isFinished() )
        {
            this.state = "finished";

            log.write( "Finished: " + game );

            this.onFinish( this );
        }

    };


Game.prototype.step = function( ) {

    log.start();

    try
    {
        switch ( this.state )
        {
            case "new":
                this.init();
                log.write( "state: init --> " + this.state );
                break;

            case "prepare":
                this.prepare();
                log.write( "state: prepare --> " + this.state );
                break;

            case "identifyFirstPlayer":
                this.identifyFirstPlayer();
                log.write( "state: identifyFirstPlayer --> " + this.state );
                break;

            case "play":
                this.play();
                log.write( "state: play --> " + this.state );
                break;

            case "finished":
                log.write( "state: finished!" );
                break;

            default:
                log.write( "unexpected state: " + this.state );

        }
    }
    catch ( e )
    {
        log.write( e );
        throw e;
    }
    finally
    {
        this.lastPlay = log.capture();
    }
};



""
