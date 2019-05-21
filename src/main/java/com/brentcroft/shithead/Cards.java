package com.brentcroft.shithead;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Getter;

@Getter
public class Cards
{
    public final static int NUM_SUITS = 4;
    public final static int SUIT_SIZE = 13;
    public final static int NUM_CARDS = SUIT_SIZE * NUM_SUITS;

    public final static List< Integer > WILDCARDS = Arrays.asList( 2, 3, 10 );

    public static final String NO_MORE_CARDS = "No more cards";

    public static boolean isWildcard( Card card )
    {
        return WILDCARDS.contains( card.value );
    }


    public static Card newCard( int i )
    {
        return new Card( i );
    }


    public static String toJson(Collection< Card > cards)
    {
        return "[ " + cards.stream().map( c-> "\"" + c + "\"" ).collect( joining( ", " ) ) + " ]";
    }



    @Getter
    public static class Card
    {
        private final int value;
        private final int suit;

        public Card( int i )
        {
            this.suit = i / SUIT_SIZE;
            this.value = 1 + ( i % SUIT_SIZE );
        }


        public int getScore()
        {
            return value == 1
                    ? 14
                    : value == 2
                            ? 15
                            : value == 10
                                    ? 16
                                    : value;
        }

        public String getValueText()
        {
            return value == 1 ? "A"
                    : this.value == 13 ? "K"
                            : this.value == 12 ? "Q"
                                    : this.value == 11 ? "J"
                                            : "" + this.value;
        }

        public char getSuitText()
        {
            switch ( suit )
            {

                case 0:
                    // spades
                    return '\u2660';
                case 1:
                    // Diamonds
                    return '\u2666';
                case 2:
                    // Clubs
                    return '\u2663';
                case 3:
                    // Hearts
                    return '\u2764';
            }
            return '@';
            // throw new RuntimeException( "Invalid suit: " + suit );
        }


        public String toString()
        {
            return format( "%s%s", getValueText(), getSuitText() );
        }
    }


    private final Stack< Card > cards;


    public Cards( List< Integer > cardIds )
    {
        cards = new Stack<>();
        cardIds.forEach( cardId -> cards.push( new Card( cardId ) ) );
    }

    public Cards()
    {
        this( IntStream
                .range( 0, 52 )
                .boxed()
                .collect( Collectors.toList() ) );
    }

    public int size() {
        return cards.size();
    }


    public Card next()
    {
        if ( cards.isEmpty() )
        {
            throw new RuntimeException( NO_MORE_CARDS );
        }
        return cards.pop();
    }

    public boolean dealt()
    {
        return cards.size() < NUM_CARDS;
    }

    public boolean isEmpty()
    {
        return cards.isEmpty();
    }

}
