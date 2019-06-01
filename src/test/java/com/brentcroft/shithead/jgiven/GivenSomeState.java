package com.brentcroft.shithead.jgiven;

import static com.brentcroft.shithead.jgiven.CardsUtil.PLAYERS;

import java.util.stream.IntStream;

import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;

public class GivenSomeState extends Stage< GivenSomeState >
{


    @ProvidedScenarioState
    StandardGame game;


    @ProvidedScenarioState
    Player player;


    public GivenSomeState a_new_game()
    {
        game = new StandardGame();
        return self();
    }

    public GivenSomeState with_min_players(int i) {
        game.getGameModel().setMinPlayers(i);
        return self();
    }


    public GivenSomeState with_players(int i) {
        PLAYERS
                .getPlayers( i )
                .forEach( player -> game.addPlayer( player ) );
        return self();
    }


    public GivenSomeState first_player_detected()
    {
        game.detectFirstPlayer();

        player = game.getGameModel().getCurrentPlayer();

        return self();
    }

    public GivenSomeState cards_are_dealt()
    {
        game.dealCards();
        return self();
    }

    public GivenSomeState after_playing_turns( int turns )
    {
        IntStream
                .range( 0, turns )
                .forEach( turn -> {

                    Player player = game.getGameModel().getCurrentPlayer();

                    game.playerDiscard( player.getDiscard( game.getGameModel().getSelector() ) ) ;
                } );
        return self();
    }

    public GivenSomeState a_dealt_3_player_game()
    {
        a_new_game().with_players(3).and().cards_are_dealt();
        return self();
    }



    public GivenSomeState a_player( String name )
    {
        if ( game.getGameModel().hasPlayer( name ) )
        {
            player = game.getGameModel().getPlayer( name );
        }
        else if ( name == null )
        {
            game.getGameModel().getPlayers().get( 0 );
        }
        else
        {
            player = new Player( name );
            game.addPlayer( player );
        }

        return self();
    }


    public GivenSomeState with_deck_cards(String cardText) {
        CardList.of( cardText )
                .forEach( card -> game.getGameModel().getDeck().getCards().push( card ));

        return self();
    }


    public GivenSomeState with_stack_cards( String cardText )
    {
        CardList.of( cardText )
                .forEach( card -> game.getGameModel().getStack().push( card ) );

        return self();
    }

    public GivenSomeState with_empty_hand_cards() {

        player.getHandCards().clear();

        return self();
    }

    public GivenSomeState with_hand_cards( String cardText )
    {
        with_empty_hand_cards();

        if ( cardText != null ) {
            CardList.of(cardText)
                    .forEach(card -> player.addCard(Player.ROW.HAND, card));
        }

        return self();
    }


    public GivenSomeState with_empty_faceup_cards() {

        player.getFaceUpCards().clear();

        return self();
    }

    public GivenSomeState with_faceup_cards(String cardText) {
        with_empty_faceup_cards();

        if ( cardText != null ) {
            CardList.of(cardText)
                    .forEach(card -> player.addCard(Player.ROW.FACEUP, card));
        }

        return self();
    }

    public GivenSomeState with_empty_blind_cards() {

        player.getBlindCards().clear();

        return self();
    }

    public GivenSomeState with_blind_Cards(String cardText) {
        with_empty_blind_cards();

        if ( cardText != null ) {
            CardList.of(cardText)
                    .forEach(card -> player.addCard(Player.ROW.BLIND, card));
        }

        return self();
    }

}