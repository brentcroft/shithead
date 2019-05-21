package com.brentcroft.shithead.model;

import static com.brentcroft.shithead.commands.Messages.FIRST_PLAYER_NOT_SELECTED;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import com.brentcroft.shithead.model.Cards.Card;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class GameModel
{
    private final Cards deck = new Cards();
    private final Stack< Card > stack = new Stack<>();
    private final List<Player> players = new ArrayList<>();
    private Stack< Player > lastPlayer = new Stack<>();
    private Player currentPlayer;

    private static final BiFunction<Stack<Card>, Card, Boolean> STACK_CARD_SELECTOR = (stack,
                                                                                      card) -> stack.isEmpty()
            || Cards.isWildcard(card)
            || card.getValue() >= stack.peek().getValue();


    private Predicate<Card> selector = (card) -> STACK_CARD_SELECTOR.apply(stack, card);


    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public boolean hasPlayer(String playerName) {
        return players
                .stream()
                .anyMatch(p->p.getName().equals(playerName));
    }

    public Player getPlayer(String playerName) {
        return players
                .stream()
                .filter(p->p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(()->new RuntimeException("No such player: " + playerName));
    }


}