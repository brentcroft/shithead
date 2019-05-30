package com.brentcroft.shithead.jgiven;


import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import java.util.Stack;

public class GivenPlayerState extends Stage<GivenPlayerState> {


    @ProvidedScenarioState
    Player player;

    @ProvidedScenarioState
    Stack<Card> stack;

    @ProvidedScenarioState
    Cards deck;


    public GivenPlayerState a_player(String name)
    {
        player = new Player(name);

        return self();
    }

    public GivenPlayerState with_hand_cards(String cardText)
    {
        CardList.of(cardText)
                .forEach(card->player.addCard(Player.ROW.HAND, card));

        return self();
    }

    public GivenPlayerState with_faceup_cards(String cardText)
    {
        CardList.of(cardText)
                .forEach(card->player.addCard(Player.ROW.FACEUP, card));
        return self();
    }

    public GivenPlayerState with_blind_cards(String cardText)
    {
        CardList.of(cardText)
                .forEach(card->player.addCard(Player.ROW.BLIND, card));
        return self();
    }

    public GivenPlayerState with_stack_cards(String s)
    {

        return self();
    }
}