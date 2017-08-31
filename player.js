function Player( name )
{
    this.name = name;
    this.blind = [];
    this.faceup = [];
    this.hand = [];    
}
Player.prototype.toString = function(){
    return "" + name;    
};
Player.prototype.isHandEmpty = function( ){
    return ( !this.hand || this.hand.length === 0 );
};
Player.prototype.isFaceupEmpty = function( ){
    return ( !this.faceup || this.faceup.length === 0 );
};
Player.prototype.isBlind = function( ){
    return this.isHandEmpty() && this.isFaceupEmpty();
};
Player.prototype.isFinished = function( ){
    return this.isBlind() && ( !this.blind || this.blind.length === 0 );
};

// make any swaps between this.hand and this.faceup
// lowest cards in hand
Player.prototype.prepareCards = function( bestInHand ){
    // set true if you want to have another look at other players choices
    //this.notPrepared = false;
};

Player.prototype.selectCards = function( topDiscard, cardsetChoices ){
    
};

Player.prototype.selectFaceupCardsToPromote = function( faceupCardset ){
    return this.faceup && this.faceup.length > 0 ? [ this.faceup[0] ] : [];
};

Player.prototype.clean = function(){
    this.faceup.forEach( function( c ) { c.clean(); } );
};


Player.prototype.makeChoice = function( choices ) {

    if ( !choices )
    {
        throw ( "No choices! " + choices );
    }    
    
    var twos = null;
    var tens = null;
    var minChoice = null;
    
    choices.forEach( function( choiceSet ){
        var exmplr = choiceSet[ 0 ].value;

        if ( exmplr === 2 )
        {
            twos = choiceSet;
        }
        else if ( exmplr === 10 )
        {
            tens = choiceSet;
        }
        else if ( !minChoice || ( exmplr < minChoice[ 0 ].value ) )
        {
            minChoice = choiceSet;
        }
    });
    
    if ( minChoice )
    {
        return minChoice;
    }
    else if ( twos )
    {
        return twos;
    }
    else if ( tens )
    {
        return tens;
    }
    else
    {
        return null;
    }
};


Player.prototype.receiveCard = function( card ) {
    if ( card != null )
    {
        this.hand.push( card );

        Cards.sortCardSet( this.hand );
        Cards.sortCardSet( this.faceup );
    }
};
Player.prototype.discardCard = function( card ) {
    var predicate = function( candidate ) { return card === candidate; };
    
    var removed = this.hand.removeFirst( predicate ) 
        || this.faceup.removeFirst( predicate ) 
        || this.blind.removeFirst( predicate );
    
    Cards.sortCardSet( this.hand );
    Cards.sortCardSet( this.faceup );
};

