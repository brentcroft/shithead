package com.brentcroft.shithead.model;

import java.util.*;
import java.util.stream.Collectors;

import static com.brentcroft.shithead.model.Rules.CARD_COMPARATOR;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class CardList extends ArrayList< Card >
{
    private static final long serialVersionUID = 64937354491535615L;

    public static CardList of( List< Card > cards )
    {
        CardList cl = new CardList();
        cl.addAll( cards );
        cl.sort( CARD_COMPARATOR );
        return cl;
    }

    public static CardList of( Card... cards )
    {
        return of( Arrays.asList( cards ) );
    }

    public static CardList of( String text )
    {
        if ( text.length() < 2 )
        {
            throw new RuntimeException( "Invalid cards text: " + text );
        }
        else if ( text.charAt( 0 ) == '[' && text.charAt( text.length() - 1 ) == ']' )
        {
            text = text.substring( 1, text.length() - 1 );
        }

        List< String > ct = Arrays
                .asList( text.split( "[,\\s]+" ) )
                .stream()
                .map( String::trim )
                .filter( s -> !s.isEmpty() )
                .collect( Collectors.toList() );

        return of(
                ct
                        .stream()
                        .map( Cards::getCard )
                        .collect( Collectors.toList() ) );
    }


    public String toString()
    {
        return toText( this );
    }

    public static String toText( List< Card > cards )
    {
        return format( "[ %s ]", cards.stream().map( Object::toString ).collect( joining( ", " ) ) );
    }

}
