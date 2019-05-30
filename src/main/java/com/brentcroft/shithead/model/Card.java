package com.brentcroft.shithead.model;

import static java.lang.String.format;

import lombok.Getter;

@Getter
public class Card
{
    private final int value;
    private final int suit;


    public Card( int value, int suit )
    {
        this.value = value;
        this.suit = suit;
    }

    public Card( int i )
    {
        this(
                i < 0 ? -1 : 1 + ( i % Rules.SUIT_SIZE ),
                i < 0 ? -1 : i / Rules.SUIT_SIZE );
    }

    public boolean equals(Object obj)
    {
        if ( obj instanceof Card) {
            Card card = (Card)obj;
            return card.getValue() == value && card.getSuit() == suit;
        }
        else
        {
            return super.equals(obj);
        }
    }

    public String toString()
    {
        return format( "%s%s", Rules.getValueText(this), Rules.getSuitText(this) );
    }
}