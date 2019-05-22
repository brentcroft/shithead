package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.jgiven.CardsUtil.PLAYERS;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenSomeState extends Stage<GivenSomeState> {


    @ProvidedScenarioState
    StandardGame game;


    @ProvidedScenarioState
    Player player;


    public GivenSomeState a_new_game() {
        game = new StandardGame();
        return self();
    }

    public GivenSomeState with_three_players() {
        PLAYERS
                .getPlayers(3)
                .forEach(player -> game.addPlayer(player));
        return self();
    }


    public GivenSomeState with_six_players() {
        PLAYERS
                .getPlayers(6)
                .forEach(player -> game.addPlayer(player));
        return self();
    }


    public GivenSomeState first_player_detected() {
        game.detectFirstPlayer();
        return self();
    }

    public GivenSomeState cards_are_dealt() {
        game.dealCards();
        return self();
    }

    public GivenSomeState after_playing_turns(int turns) {
        IntStream
                .range(0, turns)
                .forEach(turn -> game.playerDiscard(
                        new Discard(
                                game.getGameModel().getCurrentPlayer().getName(),
                                game.getGameModel().getCurrentPlayer().chooseCards(game.getGameModel().getSelector())
                        )));
        return self();
    }

    public GivenSomeState a_dealt_3_player_game() {
        a_new_game().with_three_players().and().cards_are_dealt();
        return self();
    }



    public GivenSomeState a_player(String name)
    {
        if ( game.getGameModel().hasPlayer(name) )
        {
            player = game.getGameModel().getPlayer(name);
        }
        else if (name == null)
        {
            game.getGameModel().getPlayers().get(0);
        }
        else
        {
            player = new Player(name);
            game.addPlayer(player);
        }

        return self();
    }

    public GivenSomeState with_hand_cards(String cardText)
    {
        Cards
                .fromText(cardText)
                .forEach(card->player.addCard(Player.ROW.HAND, card));

        return self();
    }

    public GivenSomeState with_stack_cards(String cardText) {
        Cards
                .fromText(cardText)
                .forEach(card->game.getGameModel().getStack().push(card));

        return self();
    }
}