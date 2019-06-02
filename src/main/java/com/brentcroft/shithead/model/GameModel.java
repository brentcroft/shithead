package com.brentcroft.shithead.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import lombok.Setter;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class GameModel
{
    public static final int NUM_CARDS_COLUMNS = 3;
    public static final int FIRST_PLAYER_VALUE = 3;
    public static final int MIN_HAND_SIZE_TO_PICKUP = 3;

    @Setter
    private int minPlayers = 2;


    //
    private final Cards deck = new Cards();
    private final Stack< Card > stack = new Stack<>();
    private final List< Player > players = new ArrayList<>();
    private Stack< Player > lastPlayer = new Stack<>();
    private Player currentPlayer;

    private int turnNo = 0;
    //
    private Predicate< Card > selector = ( card ) -> Rules.STACK_CARD_SELECTOR.apply( stack, card );


    public void setCurrentPlayer( Player player )
    {
        this.currentPlayer = player;
    }

    public boolean hasPlayer( String playerName )
    {
        return players
                .stream()
                .anyMatch( p -> p.getName().equals( playerName ) );
    }

    public Player getPlayer( String playerName )
    {
        return players
                .stream()
                .filter( p -> p.getName().equals( playerName ) )
                .findFirst()
                .orElseThrow( () -> new RuntimeException( "No such player: " + playerName ) );
    }

    public boolean isFinished()
    {
        return minPlayers > players.size();
    }


    public void incrementTurn()
    {
        turnNo++;
    }
}