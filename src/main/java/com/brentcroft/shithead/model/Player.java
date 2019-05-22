package com.brentcroft.shithead.model;

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
    private ArrayList< Card > blindCards = new ArrayList<>();
    private ArrayList< Card > faceUpCards = new ArrayList<>();
    private ArrayList< Card > handCards = new ArrayList<>();

    public Player( String name )
    {
        this.name = name;
    }

    public String toString()
    {
        return format( "%s", name );
    }

    public static String joinCards(List< Card > cards)
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



    private ArrayList< Card > getCards( ROW row )
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

    public boolean hasCards( ROW row, List< Card > cards )
    {
        return cards
                .stream()
                .anyMatch( card->hasCard(row, card.value ) );
    }


    public String getName()
    {
        return name;
    }

    public int countCards( ROW row )
    {
        return getCards( row ).size();
    }

    public Card getCard( ROW row, int i )
    {
        return getCards( row ).get( i );
    }

    public boolean playCard( Card card )
    {
        return handCards.remove( card );
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

    public List< Card > chooseCards( Predicate<Card> selector )
    {
        ROW currentRow = currentRow();

        if ( currentRow == null )
        {
            throw new RuntimeException( "Current row is null: " + this );
        }

        List< Card > choices = null;
        switch ( currentRow )
        {
            case BLIND:
                return Collections.singletonList(getCards(currentRow).get(0));


            case HAND:
                List< Card > cardsInCurrentRow = getCards( ROW.HAND );
                choices = cardsInCurrentRow
                        .stream()
                        .filter(selector)
                        .collect( Collectors.toList() );

                if ( choices.size() == cardsInCurrentRow.size() )
                {
                    Card choiceOne = choices.get( 0 );
                    
                    List< Card > electees = getCards( ROW.FACEUP )
                            .stream()
                            .filter( f -> f.getValue() == choiceOne.getValue() )
                            .collect( Collectors.toList() );

                    if ( !electees.isEmpty() )
                    {
                        choices = new ArrayList<>( choices );
                        choices.addAll( electees );
                    }
                }
                break;

            default:
                choices = getCards( currentRow )
                        .stream()
                        .filter( selector )
                        .collect( Collectors.toList() );

        }

        if ( choices.size() == 0 )
        {
            // we know we're going to pick up
            // if current row is faceup
            // then these will be elected to the hand

            choices = getCards( currentRow );
        }

        Card minCard = choices
                .stream()
                .min(Comparator.comparingInt(Card::getScore))
                .orElse( null );

        if ( minCard == null )
        {
            throw new RuntimeException( "No min card: " + choices + "\n   " + currentRow + "\n" + this );
        }

        return choices
                .stream()
                .filter( card -> card.getValue() == minCard.getValue() )
                .collect( Collectors.toList() );
    }

    public void electCards( List< Card > cards )
    {
        ArrayList< Card > choices = getCards( currentRow() );

        cards
                .forEach( card -> {
                    if ( choices.remove( card ) )
                    {
                        handCards.add( card );
                    }
                    else
                    {
                        throw new RuntimeException( "Card not in current row" );
                    }
                } );
    }
}
