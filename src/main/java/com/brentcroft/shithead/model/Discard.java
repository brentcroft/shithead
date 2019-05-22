package com.brentcroft.shithead.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Discard
{
    private final String playerName;
    private final List< Card > cards;

    public Discard( String playerName, List< Card > cards )
    {
        this.playerName = playerName;
        this.cards = new ArrayList<>();
        this.cards.addAll( cards );
    }

    public Discard( String playerName, String cardsText )
    {
        this.playerName = playerName;
        this.cards = new ArrayList<>();
        this.cards.addAll( Cards.fromText( cardsText ) );
    }

    public String toText()
    {
        return Cards.toText( cards );
    }
}
