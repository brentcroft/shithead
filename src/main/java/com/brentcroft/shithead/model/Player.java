package com.brentcroft.shithead.model;

import static com.brentcroft.shithead.context.Messages.CARD_NOT_IN_CURRENT_ROW;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class Player
{

    public enum ROW
    {
        BLIND, FACEUP, HAND;

        public static void forEach( Consumer< ? super ROW > f )
        {
            Arrays.asList( values() ).forEach( f );
        }
    };


    private String name;
    private CardList blindCards = new CardList();
    private CardList faceUpCards = new CardList();
    private CardList handCards = new CardList();

    public Player( String name )
    {
        this.name = name;
    }

    public String toString()
    {
        return format( "%s", name );
    }

    public static String joinCards(CardList cards)
    {
        return "[ " + cards.stream().map( Object::toString ).collect( joining( ", " ) ) + " ]";
    }

    public String cardsText()
    {
        return format( "h=%-10s f=%-10s b=%-10s",
                joinCards(handCards ),
                joinCards(faceUpCards ),
                joinCards(blindCards ) );
    }



    private CardList getCards( ROW row )
    {
        switch ( row )
        {
            case BLIND:
                return blindCards;

            case FACEUP:
                return faceUpCards;

            case HAND:
                return handCards;

            default:
                throw new RuntimeException( "Unknown row type: " + row );
        }
    }



    public void addCard( ROW row, Card card )
    {
        getCards( row ).add( card );
    }

    public boolean hasCards()
    {
        return !( blindCards.isEmpty() && faceUpCards.isEmpty() && handCards.isEmpty() );
    }

    public boolean hasCard( ROW row, int value )
    {
        return getCards( row )
                .stream()
                .anyMatch(c -> c.getValue() == value );

    }

    public boolean hasCards( ROW row, CardList cards )
    {
        return cards
                .stream()
                .anyMatch( card->hasCard(row, card.getValue() ) );
    }


    public String getName()
    {
        return name;
    }

    public int countCards( ROW row )
    {
        return getCards( row ).size();
    }

    public boolean playCard( Card card )
    {
        return handCards
                .remove( handCards
                    .stream()
                    .filter(c->c.equals(card))
                    .findFirst()
                    .orElse(null) );
    }

    public boolean hasCardsInHand()
    {
        return !handCards.isEmpty();
    }

    public boolean hasCardsInFaceUp()
    {
        return !faceUpCards.isEmpty();
    }

    public ROW currentRow()
    {
        return hasCardsInHand()
                ? ROW.HAND
                : !faceUpCards.isEmpty()
                        ? ROW.FACEUP
                        : !blindCards.isEmpty()
                                ? ROW.BLIND
                                : null;
    }

    public CardList chooseCards( Predicate<Card> selector )
    {
        ROW currentRow = currentRow();

        if ( currentRow == null )
        {
            throw new RuntimeException( "Current row is null: " + this );
        }

        switch ( currentRow )
        {
            case BLIND:
                return CardList.of(getCards(currentRow).get(0));

            default:
                return  CardList.of( getCards( currentRow )
                        .stream()
                        .filter( selector )
                        .collect( Collectors.toList() ) );
        }
    }

    public void electCards( CardList cards )
    {
        ROW currentRow = currentRow();

        CardList choices = getCards( currentRow );

        cards
                .forEach( card -> {
                    if ( choices.remove( choices
                            .stream()
                            .filter(c->c.equals(card))
                            .findFirst()
                            .orElse(null) ) )
                    {
                        handCards.add( card );
                    }
                    else
                    {
                        throw new RuntimeException( format(CARD_NOT_IN_CURRENT_ROW, card,  currentRow ) );
                    }
                } );
    }





    /*
        how valuable is the card - I suppose depends on context
    */
    public static int cardScore(Card card)
    {
        int BASE_SCORE = 14;

        switch ( card.getValue() )
        {
            case 1:
                return BASE_SCORE;

            case 2:
                return BASE_SCORE + 1;

            case 3:
                return BASE_SCORE + 2;

            case 10:
                return BASE_SCORE + 3;

            default:
                return card.getValue();
        }
    }

}
