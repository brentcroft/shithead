package com.brentcroft.shithead.model;

import static com.brentcroft.shithead.context.Messages.NO_MORE_CARDS;
import static com.brentcroft.shithead.model.Rules.NUM_CARDS;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Cards
{
    private final Stack< Card > cards;

    public Cards( List< Integer > cardIds )
    {
        cards = new Stack<>();
        cardIds.forEach( cardId -> cards.push( new Card( cardId ) ) );
    }

    public Cards()
    {
        this( IntStream
                .range( 0, NUM_CARDS )
                .boxed()
                .collect( Collectors.toList() ) );
    }

    public int size() {
        return cards.size();
    }


    public static Card newCard( int i )
    {
        return new Card( i );
    }



    public static CardList abacinate(CardList blindCards) {
        return CardList.of(blindCards
                .stream()
                .map(Cards::blindCard)
                .collect(Collectors.toList()));
    }

    private static Card blindCard(Card card) {
        return new Card(-1);
    }

    public static Card getCard(String ct) {
        switch (ct.length())
        {
            case 3:
                return new Card( Rules.getTextValue( ct.substring(0,2)), Rules.getTextSuit( ct.charAt(2)));
            case 2:
                return new Card( Rules.getTextValue( ct.substring(0,1)), Rules.getTextSuit( ct.charAt(1)));

            default:
                throw new RuntimeException("Invalid card text: " + ct);
        }
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
