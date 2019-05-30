function Card( value )
{
    this.value = value;
}
Card.prototype.valueKey = function( ){
    return this.value == 14 ? "A"
        : this.value == 13 ? "K"
        : this.value == 12 ? "Q"
        : this.value == 11 ? "J"
        : this.value;
};
Card.prototype.keyValue = function( v ){
    return v == "A" ? 14
        : v == "K" ? 13
        : v == "Q" ? 12
        : v == "J" ? 11
        : v;
};
Card.prototype.id = function( ){
 return ( this.valueKey() + "-" + this.suit )
};
Card.prototype.toString = function() {
    return this.valueKey() + this.suitGlyph;
};

Card.prototype.clean = function() {
    delete this.selectable;
    delete this.selectgroup;
    delete this.selected;
    delete this.topDiscaArd;
};
Card.prototype.suitToGlyph = function( suit ) {
    switch ( s ) {
        case "Spades":
            return '\u2660';
        case "Diamonds":
            return '\u2666';
        case "Clubs":
            return '\u2663';
        case "Hearts":
            return '\u2764';
    }
    throw "Invalid suit: " + suit;
}

Card.prototype.glyphToSuit = function( glyph ) {
    switch ( glyph ) {
        case "\u2660":
            return "Spades";
        case "\u2666":
            return "Diamonds";
        case "\u2663":
            return "Clubs";
        case "\u2764":
            return "Hearts";
    }
    throw "Invalid suit glyph: " + glyph;
}

Card.prototype.decode = function( s ) {

    var value = this.keyValue( s.substring( 0, s.length - 1 ) );
    var glyph = s.substring( s.length - 1 );

    var suitKey = Card.prototype.glyphToSuit( glyph );

    var suit = Cards.deck[ suitKey ];

    for ( var n in suit )
    {
        if ( suit[ n ].value == value )
        {
            return suit[ n ];
        }
    }
    throw "Failed to decode: value=[" + value + "], glyph=[" + glyph + "], suit=[" + suitKey + "]; data=[" + s + "].";
};



var Cards = {
    deck: {
        Spades: {
            Two: new Card( 2 ),
            Three: new Card( 3 ),
            Four: new Card( 4 ),
            Five: new Card( 5 ),
            Six: new Card( 6 ),
            Seven: new Card( 7 ),
            Eight: new Card( 8 ),
            Nine: new Card( 9 ),
            Ten: new Card( 10 ),
            Jack: new Card( 11 ),
            Queen: new Card( 12 ),
            King: new Card( 13 ),
            Ace:  new Card( 14 )
        },
        Hearts: {
            Two: new Card( 2 ),
            Three: new Card( 3 ),
            Four: new Card( 4 ),
            Five: new Card( 5 ),
            Six: new Card( 6 ),
            Seven: new Card( 7 ),
            Eight: new Card( 8 ),
            Nine: new Card( 9 ),
            Ten: new Card( 10 ),
            Jack: new Card( 11 ),
            Queen: new Card( 12 ),
            King: new Card( 13 ),
            Ace:  new Card( 14 )
        },
        Clubs: {
            Two: new Card( 2 ),
            Three: new Card( 3 ),
            Four: new Card( 4 ),
            Five: new Card( 5 ),
            Six: new Card( 6 ),
            Seven: new Card( 7 ),
            Eight: new Card( 8 ),
            Nine: new Card( 9 ),
            Ten: new Card( 10 ),
            Jack: new Card( 11 ),
            Queen: new Card( 12 ),
            King: new Card( 13 ),
            Ace:  new Card( 14 )
        },
        Diamonds: {
            Two: new Card( 2 ),
            Three: new Card( 3 ),
            Four: new Card( 4 ),
            Five: new Card( 5 ),
            Six: new Card( 6 ),
            Seven: new Card( 7 ),
            Eight: new Card( 8 ),
            Nine: new Card( 9 ),
            Ten: new Card( 10 ),
            Jack: new Card( 11 ),
            Queen: new Card( 12 ),
            King: new Card( 13 ),
            Ace:  new Card( 14 )
        }
    },



    shuffleArray: function( array ) {

      var currentIndex = array.length;

      // While there remain elements to shuffle...
      while (0 !== currentIndex) {

        // Pick a remaining element...
        var randomIndex = Math.floor( Math.random() * currentIndex );
        currentIndex -= 1;

        // And swap it with the current element.
        var temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
      }

      return array;
    },

    newDeck: function(){

        var unshuffledDeck = [];

        for ( var s in this.deck )
        {
            var suit = this.deck[ s ];

            var suitGlyph = "";

            switch ( s ) {
                case "Spades":
                    suitGlyph = '\u2660';
                    break;
                case "Diamonds":
                    suitGlyph = '\u2666';
                    break;
                case "Clubs":
                    suitGlyph = '\u2663';
                    break;
                case "Hearts":
                    suitGlyph = '\u2764';
                    break;
            }

            for ( var c in suit )
            {
                var card = suit[ c ];

                // wipe the cards clean before re-use
                delete card.selectable;
                delete card.selectgroup;

                // set the suit and name
                // while we're at it
                card.suit = s;
                card.name = c;

                var offset = ( card.value == 14 )
                    ? 1
                    : ( card.value == 13 )
                        ? 'E'
                    : ( card.value == 12 )
                        ? 'D'
                    : ( card.value == 11 )
                        ? 'B'
                    : ( card.value == 10 )
                        ? 'A'
                    : card.value;

                var hex = (
                        ("Spades" == s) ? "1F0A"
                        :("Hearts" == s) ? "1F0B"
                        :("Diamonds" == s) ? "1F0C"
                        :("Clubs" == s) ? "1F0D"
                        :"1F0E"
                        ) + offset;

                while (hex.length < 6) { hex = '0' + hex; } // Zero pad.

                var code = parseInt( hex, 16 );

                card.glyph = String.fromCodePoint( code );

                card.suitGlyph = suitGlyph;

                // adding new one
                unshuffledDeck[ unshuffledDeck.length ] = card;
            }
        }
        return unshuffledDeck;
    },


    shuffle: function( )
    {
        return this.shuffleArray( this.newDeck() );
    },

    sortCardSet: function( cardSet ) {
        cardSet.sort( function( a, b ) {
            return ( a.value === b.value ) ? 0
                :( a.value === 10 ) ? 1
                :( b.value === 10 ) ? -1
                :( a.value === 2 ) ? 1
                :( b.value === 2 ) ? -1
                :( a.value - b.value );
            });
    },

    fromData: function( data )
    {
        var cards  = [];
        data
            .split( "," )
            .forEach( s => cards.push( Card.prototype.decode( s.trim() ) ) );

        return cards;
    },

    formatCardDeck( deckToFormat )
    {
        // wrapped layout

        var formattedText = "";
        var isFirst = true;
        var index = -1;
        var cols = 4;

        deckToFormat
            .forEach( card => {
                formattedText += ( isFirst ? "" : ", " )
                    + ( (index % cols) != ( cols - 1 ) ? "" : "\n" )
                    + card;
                isFirst = false;
                index++;
            } );
        return formattedText;
    }
};


