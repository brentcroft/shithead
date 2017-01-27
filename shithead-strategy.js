var Strategy = {

    "sort" : {
        "byValue": function( a, b ) {
            return ( a.value === b.value ) ? 0
                :( a.value === 10 ) ? 1
                :( b.value === 10 ) ? -1
                :( a.value === 2 ) ? 1
                :( b.value === 2 ) ? -1
                :( a.value - b.value );
            },
            
        "byValueReverse": function( a, b ) { return -1 * Strategy.sort.byValue( a, b ); },
                        
            
        "byValueAceBeatsTwo": function( a, b ) {
            return ( a.value === 14 ) && ( b.value === 2 ) ? 1
                : ( a.value === 2 ) && ( b.value === 14 ) ? -1
                : Strategy.sort.byValue( a, b );
            },
            
        "byValueAceBeatsTwoReverse": function( a, b ) { return -1 * Strategy.sort.byValueAceBeatsTwo( a, b ); }
    },

	"prepareCards": function( cardSet1, cardSet2, comparator ){
		
		var cardSet = [];

        // capture the lengths 
        var numCS1 = cardSet1.length;
        var numCS2 = cardSet2.length;
        
		// move cards to common set
		while (cardSet1.length > 0 ) { cardSet.push( cardSet1.pop() ); }
		while (cardSet2.length > 0 ) { cardSet.push( cardSet2.pop() ); }
		
        // sort them
		cardSet.sort( comparator );
		
        for ( var i = 1; i <= numCS2; i++ ) {
            cardSet2.push( cardSet.pop() );
        }
        
        for ( var i = 1; i <= numCS2; i++ ) {
            cardSet1.push( cardSet.pop() );
        }
	}
};
