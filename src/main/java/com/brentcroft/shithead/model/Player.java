package com.brentcroft.shithead.model;

import static com.brentcroft.shithead.context.Messages.CARD_NOT_IN_CURRENT_ROW;
import static com.brentcroft.shithead.model.Rules.CARD_COMPARATOR;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
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

    public static String joinCards( CardList cards )
    {
        return "[ " + cards.stream().map( Object::toString ).collect( joining( ", " ) ) + " ]";
    }

    public String cardsText()
    {
        return format( "h=%-10s f=%-10s b=%-10s",
                joinCards( handCards ),
                joinCards( faceUpCards ),
                joinCards( blindCards ) );
    }



    public CardList getCards( ROW row )
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
                .anyMatch( c -> c.getValue() == value );

    }

    public boolean hasCards( ROW row, CardList cards )
    {
        return cards
                .stream()
                .anyMatch( card -> hasCard( row, card.getValue() ) );
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
                        .filter( c -> c.equals( card ) )
                        .findFirst()
                        .orElse( null ) );
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



    public CardList chooseValidCards( Predicate< Card > selector )
    {
        ROW currentRow = currentRow();

        if ( currentRow == null )
        {
            throw new RuntimeException( "Current row is null: " + this );
        }

        switch ( currentRow )
        {
            case BLIND:
                return CardList.of( getCards( currentRow ).get( 0 ) );

            default:
                return CardList.of( getCards( currentRow )
                        .stream()
                        .filter( selector )
                        .sorted( CARD_COMPARATOR )
                        .collect( Collectors.toList() ) );
        }
    }

    public CardList chooseAnyCards()
    {
        ROW currentRow = currentRow();

        if ( currentRow == null )
        {
            throw new RuntimeException( "Current row is null: " + this );
        }

        switch ( currentRow )
        {
            case BLIND:
                return CardList.of( getCards( currentRow ).get( 0 ) );

            default:
                return CardList.of( getCards( currentRow )
                        .stream()
                        .sorted( CARD_COMPARATOR )
                        .collect( Collectors.toList() ) );
        }
    }


    public Discard getDiscard( Predicate< Card > selector )
    {
        // valid discards
        CardList cards = chooseValidCards( selector );

        if ( cards.size() == 0 )
        {
            // invalid discards
            cards = chooseAnyCards();
        }

        int value = cards.get( 0 ).getValue();

        // TODO: control over multiplicity
        return new Discard(
                getName(),
                CardList.of(
                        cards
                                .stream()
                                .filter( card -> card.getValue() == value )
                                .collect( Collectors.toList() ) ) );
    }



    public void electCards( CardList cards )
    {
        ROW currentRow = faceUpCards.isEmpty() ? ROW.BLIND : ROW.FACEUP;
        CardList choices = faceUpCards.isEmpty() ? blindCards : faceUpCards;
        
        cards
                .forEach( card -> {
                    if (handCards.contains( card ))
                    {
                        //
                    }
                    else if ( choices.remove( choices
                            .stream()
                            .filter( c -> c.equals( card ) )
                            .findFirst()
                            .orElse( null ) ) )
                    {
                        handCards.add( card );
                    }
                    else
                    {
                        throw new RuntimeException( format( CARD_NOT_IN_CURRENT_ROW, card, currentRow ) );
                    }
                } );
    }



    /*
     * how valuable is the card - I suppose depends on context
     */
    public static int cardScore( Card card )
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
