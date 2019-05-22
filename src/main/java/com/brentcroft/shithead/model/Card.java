package com.brentcroft.shithead.model;

import static java.lang.String.format;

import lombok.Getter;

@Getter
public class Card
{
    final int value;
    private final int suit;

    public Card( int i )
    {
        this(
                i < 0 ? -1 : 1 + ( i % Cards.SUIT_SIZE ),
                i < 0 ? -1 : i / Cards.SUIT_SIZE );
    }

    public Card( int value, int suit )
    {
        this.value = value;
        this.suit = suit;
    }


    public int getScore()
    {
        switch ( value )
        {
            case 1:
                return 14;
            case 2:
                return 15;
            case 10:
                return 16;
            default:
                return value;
        }
    }

    public String getValueText()
    {
        return value < 0 ? "?"
                : value == 1 ? "A"
                        : this.value == 13 ? "K"
                                : this.value == 12 ? "Q"
                                        : this.value == 11 ? "J"
                                                : "" + this.value;
    }

    public char getSuitText()
    {
        switch ( suit )
        {
            case -1:
                return '?';
            case 0:
                return '\u2660'; // spades
            case 1:
                return '\u2666'; // Diamonds
            case 2:
                return '\u2663'; // Clubs
            case 3:
                return '\u2764'; // Hearts
        }
        return '#';
        // throw new RuntimeException( "Invalid suit: " + suit );
    }


    public String toString()
    {
        return format( "%s%s", getValueText(), getSuitText() );
    }
}