package com.brentcroft.shithead;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.brentcroft.shithead.Cards.Card;

public class Play
{
    private final List< Card > cards;

    public Play( List< Card > cards )
    {
        this.cards = new ArrayList<>();
        this.cards.addAll( cards );
    }

    public Play( Player player, Player.ROW row, Integer... items )
    {
        this.cards = new ArrayList<>();

        Arrays.asList( items )
                .forEach( i -> cards.add( player.getCard( row, i ) ) );
    }


    public List< Card > cards()
    {
        return cards;
    }
    
    public String cardsText()
    {
        return format( "[%s]", cards.stream().map( Object::toString ).collect( joining( ", " ) ) );
    }

}
