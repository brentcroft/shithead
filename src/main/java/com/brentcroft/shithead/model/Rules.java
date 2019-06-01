package com.brentcroft.shithead.model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

import static java.lang.String.format;


public class Rules
{
    public final static int NUM_SUITS = 4;
    public final static int SUIT_SIZE = 13;
    public final static int NUM_CARDS = SUIT_SIZE * NUM_SUITS;

    public final static int LOWEST_CARD_VALUE = 2;


    // goes on any stack top
    public final static List< Integer > WILDCARDS = Arrays.asList( 2, 10 );
    public static final int BLIND_SUIT = NUM_SUITS + 17;

    public static boolean isWildcard( Card card )
    {
        return WILDCARDS.contains( card.getValue() );
    }

    /*
        can the card be played on the stack
     */
    public static final BiFunction< Stack< Card >, Card, Boolean > STACK_CARD_SELECTOR = (
            stack, card ) -> isWildcard( card )
                            || stack.isEmpty()
                            || card.getValue() >= stack.peek().getValue();

//    public static final BiFunction< Card, Card, Integer > CARD_COMPARATOR = (
//            card1, card2 ) -> Integer.compare(card1.getValue(), card2.getValue());


    public static int discardValue(Card card){
        switch(card.getValue())
        {
            case 10:
                return Integer.MAX_VALUE;

            default:
                return card.getValue() + ( Rules.isWildcard(card)  ?  Rules.SUIT_SIZE : 0 );
        }
    }

    public static final Comparator<? super Card> CARD_COMPARATOR = (Comparator<Card>) (
            card1, card2 ) -> Integer.compare(discardValue(card1), discardValue(card2));





    public static int getTextValue( String text)
    {
        switch (text)
        {
            case "?": return 0;
            case "A": return 14;
            case "K": return 13;
            case "Q": return 12;
            case "J": return 11;

            default:
                return Integer.valueOf(text.trim());
        }
    }


    public static String getValueText(Card card)
    {
        switch (card.getValue())
        {
            case 14: return "A";
            case 13: return  "K";
            case 12: return "Q";
            case 11 : return "J";
            default:
                return card.getValue() < 0 ? "?" : "" + card.getValue();
        }
    }


    public static final char BLIND = 'B';
    public static final char SPADES = '♠';
    public static final char HEARTS = '♡';
    public static final char CLUBS = '♣';
    public static final char DIAMONDS = '♢';



    public static int getTextSuit( char text )
    {
        switch ( text )
        {
            case BLIND: return BLIND_SUIT;

            case SPADES: return 0;

            case DIAMONDS: return 1;

            case CLUBS: return 2;

            case HEARTS: return 3;

            default:
                throw new RuntimeException( "Invalid suit: " + text );
        }
    }

    public static char getSuitText(Card card)
    {
        switch ( card.getSuit() )
        {
            case 0: return SPADES;
            case 1: return DIAMONDS;
            case 2: return CLUBS;
            case 3: return HEARTS;

            case BLIND_SUIT: return BLIND;

            default:
                throw new RuntimeException( format("Invalid suit: %s", card.getSuit() ) );
        }
    }
}
