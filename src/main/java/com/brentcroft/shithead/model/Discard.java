package com.brentcroft.shithead.model;

import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Cards.Card;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Getter
public class Discard {
    private final String playerName;
    private final List<Card> cards;

    public Discard(String playerName, List<Card> cards) {
        this.playerName = playerName;
        this.cards = new ArrayList<>();
        this.cards.addAll(cards);
    }

    public Discard(String playerName, String cardsText) {
        this.playerName = playerName;
        this.cards = new ArrayList<>();
        this.cards.addAll(Cards.fromText(cardsText));
    }

    public String toText() {
        return Cards.toText(cards);
    }
}
