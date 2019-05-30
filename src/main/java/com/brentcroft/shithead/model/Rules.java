package com.brentcroft.shithead.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Rules
{
    public final static int NUM_SUITS = 4;
    public final static int SUIT_SIZE = 13;
    public final static int NUM_CARDS = SUIT_SIZE * NUM_SUITS;

    // go on anything
    public final static List< Integer > WILDCARDS = Arrays.asList( 1, 2, 10 );

    public static boolean isWildcard( Card card )
    {
        return WILDCARDS.contains( card.getValue() );
    }

    /*
        can the card be played on the stack
     */
    public static final int actualValue( Card card ) {
        return card.getValue() == 1 ? SUIT_SIZE + 1 : card.getValue();
    }

    public static final BiFunction< Stack< Card >, Card, Boolean > STACK_CARD_SELECTOR = (
            stack, card ) -> isWildcard( card )
                            || stack.isEmpty()
                            || actualValue(card) >= actualValue(stack.peek() );

    public static final BiFunction< Card, Card, Integer > CARD_COMPARATOR = (
            card1, card2 ) -> Integer.valueOf(actualValue(card1)).compareTo(actualValue(card2)) ;



    public static int getTextValue( String text)
    {
        switch (text)
        {
            case "?": return 0;
            case "A": return 1;
            case "K": return 13;
            case "Q": return 12;
            case "J": return 11;

            default:
                return Integer.valueOf(text.trim());
        }
    }

    public static int getTextSuit( char text )
    {
        switch ( text )
        {
            case '?': return -1;
            // spades
            case '\u2660': return 0;
            // diamonds
            case '\u2666': return 1;
            case '\u2662': return 1;
            // clubs
            case '\u2663': return 2;
            // hearts
            case '\u2764': return 3;
            case '\u2761': return 3;
        }
        return -2;
        // throw new RuntimeException( "Invalid suit: " + suit );
    }


    public static String getValueText(Card card)
    {
        switch (card.getValue())
        {
            case 1: return "A";
            case 13: return  "K";
            case 12: return "Q";
            case 11 : return "J";
            default:
                return card.getValue() < 0 ? "?" : "" + card.getValue();
        }
    }

    public static char getSuitText(Card card)
    {
        switch ( card.getSuit() )
        {
            case -1:
                return '?';
            case 0:
                return '\u2660'; // spades
            case 1:
                //return '\u2666'; // Diamonds
                return '\u2662'; // Diamonds
            case 2:
                return '\u2663'; // Clubs
            case 3:
                //return '\u2764'; // Hearts
                return '\u2661'; // Hearts
        }
        return '#';
        // throw new RuntimeException( "Invalid suit: " + suit );
    }


}
