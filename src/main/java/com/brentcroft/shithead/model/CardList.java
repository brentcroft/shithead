package com.brentcroft.shithead.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class CardList extends ArrayList<Card>
{
    private static final Comparator<? super Card> CARD_COMPARATOR = (Comparator<Card>) Rules.CARD_COMPARATOR::apply;

    public static CardList of(List<Card> cards)
    {
        CardList cl = new CardList();
        cl.addAll( cards);
        cl.sort(CARD_COMPARATOR);
        return cl;
    }

    public static CardList of(Card ... cards)
    {
        return of( Arrays.asList(cards));
    }

    public static CardList of(String text) {
        if ( text.length() < 2)
        {
            throw new RuntimeException("Invalid cards text: " + text);
        }
        else if(text.charAt(0) == '[' && text.charAt(text.length()-1)==']')
        {
            text = text.substring(1, text.length()-1);
        }

        return of(Arrays
                .asList(text.split("\\s*,\\s*"))
                .stream()
                .map(String::trim)
                .map(Cards::getCard)
                .collect(Collectors.toList()));
    }


    public String toString()
    {
        return toText(this);
    }

    public static String toText(List<Card> cards) {
        return format("[ %s ]", cards.stream().map(Object::toString).collect(joining(", ")));
    }

}
