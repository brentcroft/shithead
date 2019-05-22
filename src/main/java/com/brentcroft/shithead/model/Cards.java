package com.brentcroft.shithead.model;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
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



    public static List<Card> abacinate(List<Card> blindCards) {
        return blindCards
                .stream()
                .map(Cards::blindCard)
                .collect(Collectors.toList());
    }

    private static Card blindCard(Card card) {
        return new Card(-1);
    }

    public static Card getCard(String ct) {
        switch (ct.length())
        {
            case 3:
                return new Card( getTextValue( ct.substring(0,2)), getTextSuit( ct.charAt(2)));
            case 2:
                return new Card( getTextValue( ct.substring(0,1)), getTextSuit( ct.charAt(1)));

            default:
                throw new RuntimeException("Invalid card text: " + ct);
        }
    }

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
            case '\u2660': return 0;
            case '\u2666': return 1;
            case '\u2663': return 2;
            case '\u2764': return 3;
        }
        return -2;
        // throw new RuntimeException( "Invalid suit: " + suit );
    }



    public static String toText(List<Card> cards) {
        return format("[%s]", cards.stream().map(Object::toString).collect(joining(", ")));
    }

    public static List<Card> fromText(String text) {
        if ( text.length() < 2)
        {
            throw new RuntimeException("Invalid cards text: " + text);
        }
        text = text.substring(1, text.length()-1);

        return Arrays
                .asList(text.split("\\s*,\\s*"))
                .stream()
                .map(ct -> Cards.getCard(ct))
                .collect(Collectors.toList());
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
