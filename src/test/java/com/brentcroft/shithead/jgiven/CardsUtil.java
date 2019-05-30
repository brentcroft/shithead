package com.brentcroft.shithead.jgiven;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.brentcroft.shithead.model.*;
import com.brentcroft.shithead.model.Player.ROW;

public class CardsUtil
{


    public interface CardListGenerator
    {
        CardList cards( Player player );
    }


    public static final CardListGenerator ANY_CARDS = new CardListGenerator()
    {
        @Override
        public CardList cards( Player player )
        {
            return CardList.of( Cards.newCard( new Random().nextInt( Rules.NUM_CARDS ) ) );
        }
    };


    public static final CardListGenerator CARDS_NOT_IN_HAND = new CardListGenerator()
    {

        @Override
        public CardList cards(Player player )
        {
            CardListGenerator any = ANY_CARDS;

            while ( player.hasCards( ROW.FACEUP, any.cards( player ) ) )
            {

            }

            return any.cards( player );
        }
    };

    public interface PlayersListGenerator
    {
        List< Player > getPlayers( int i );
    }

    public static final PlayersListGenerator PLAYERS = new PlayersListGenerator()
    {

        List< Player > players = Arrays
                .asList(
                        new Player( "Apple" ),
                        new Player( "Orange" ),
                        new Player( "Lemon" ),
                        new Player( "Pear" ),
                        new Player( "Grape" ),
                        new Player( "Plum" ) );

        @Override
        public List< Player > getPlayers( int i )
        {

            return players.subList( 0, i );
        }

    };
}
