package com.brentcroft.shithead.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Discard
{
    private final String playerName;
    private final CardList cards;

    public Discard( String playerName, CardList cards )
    {
        this.playerName = playerName;
        this.cards = cards;
    }

    public Discard( String playerName, String cardsText )
    {
        this.playerName = playerName;
        this.cards = CardList.of( cardsText );
    }

    public boolean rolloverOnPickup()
    {
        return true;
    }

    public void debacinate( CardList realCards )
    {
        CardList newCards = Cards.debacinate( getCards(), realCards );
        cards.clear();
        cards.addAll( newCards );
    }
}
