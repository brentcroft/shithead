package com.brentcroft.shithead;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import com.brentcroft.shithead.Cards.Card;
import com.brentcroft.shithead.Player.ROW;



public class AbstractGame
{
    protected static final int FIRST_PLAYER_VALUE = 3;
    protected static final int NUM_CARDS_COLUMNS = 3;
    private static final int MIN_HAND_SIZE_TO_PICKUP = 3;
    public static final String CARDS_ALREADY_DEALT = "Cards already dealt";
    public static final String INVALID_PLAY_CARDS_NOT_IN_HAND = "Invalid play, cards not in hand: %s %s";
    public static final String NOT_YOUR_TURN = "Not your turn %s";
    public static final String CARDS_NOT_DEALT = "Cards not dealt";
    public static final String PLAYER_ALREADY_EXISTS = "PLAYER_ALREADY_EXISTS";




    protected final Cards cards = new Cards();
    protected final Stack< Card > stack = new Stack<>();

    protected final List< Player > players = new ArrayList<>();
    private Stack< Player > lastPlayer = new Stack<>();

    protected Player firstPlayer;
    protected Player currentPlayer;

    protected Verifier verifier = new Verifier( this );



    protected static void notifyPlay( Player player, Object action, Object cards )
    {
        System.out.println( format( "[%-8s] %-15s %s", player == null ? "*" : player.getName(), action, cards ) );
    }


    public List< Player > getPlayers()
    {
        return players;
    }

    public int numPlayers()
    {
        return players.size();
    }



    protected Player firstPlayer( int value )
    {
        if ( value > Cards.SUIT_SIZE )
        {
            throw new RuntimeException( "No first player" );
        }

        return players
                .stream()
                .filter( p -> p.hasCard( ROW.HAND, value ) )
                .findFirst()
                .orElseGet( () -> firstPlayer( value + 1 ) );
    }

    protected void checkGameFinished()
    {
        if ( players.size() < 2 )
        {
            throw new ShitheadException(
                    format( "[%s] is the shithead! %s", currentPlayer, currentPlayer.cardsText() ) );
        }
    }

    protected void playCards( Player player, Play play )
    {
        play
                .cards()
                .forEach( card -> {
                    if ( player.playCard( card ) )
                    {
                        stack.push( card );
                    }
                } );

        notifyPlay( player, "plays", play.cardsText() );

        if ( maybe10() || maybeFourOfAKind() )
        {
            notifyPlay( player, "Tsshhh...", stack );
            
            stack.clear();
        };
    }

    protected void removeFinishedPlayer( Player player )
    {
        if ( !player.hasCards() )
        {
            players.remove( player );

            lastPlayer.pop();

            notifyPlay( player, "finished", players.size() );
        }
    }

    protected void choosePreviousPlayer()
    {
        currentPlayer = lastPlayer.pop();

        notifyPlay( currentPlayer, "goes again", "" );
    }

    protected void chooseNextPlayer()
    {
        boolean[] foundLastPlayer = { false };

        players
                .stream()
                .forEach( p -> {
                    if ( p == currentPlayer )
                    {
                        foundLastPlayer[ 0 ] = true;
                        lastPlayer.push( currentPlayer );
                        currentPlayer = null;
                    }
                    else if ( foundLastPlayer[ 0 ] )
                    {
                        currentPlayer = p;
                        foundLastPlayer[ 0 ] = false;
                    }
                } );

        if ( currentPlayer == null )
        {
            currentPlayer = players.get( 0 );
        }
    }

    protected void playerTopsUpHand( Player player )
    {
        while ( !cards.isEmpty() && ( MIN_HAND_SIZE_TO_PICKUP - player.countCards( ROW.HAND ) > 0 ) )
        {
            Card card = cards.next();

            if ( !stack.isEmpty() && card.getValue() == stack.peek().getValue() )
            {
                notifyPlay( player, "slides", card );

                stack.push( card );

                continue;
            }

            notifyPlay( player, "receives", card );

            player.addCard( ROW.HAND, card );
        }

    }


    private boolean maybe10()
    {
        return stack.peek().getValue() == 10;
    }

    private boolean maybeFourOfAKind()
    {
        Card lastCard = null;
        int numOfAKind = 0;

        for ( int index = stack.size() - 1; index > 0; index-- )
        {
            Card nextCard = stack.elementAt( index );
            if ( nextCard.getValue() != 3 )
            {
                if ( lastCard == null )
                {
                    lastCard = nextCard;
                    numOfAKind = 1;
                }
                else if ( nextCard.getValue() == lastCard.getValue() )
                {
                    numOfAKind++;

                    if ( numOfAKind == 4 )
                    {
                        notifyPlay( null, "Four of a kind", "" );
                        return true;
                    }
                }
                else
                {
                    break;
                }
            }

        }
        return false;
    }


    public List<Card> stackTop()
    {
        List<Card> stackTop = new ArrayList<>();

            Card topCard = null;
            for ( int index = stack.size() - 1; index > 0; index-- )
            {
                Card nextCard = stack.elementAt( index );
                if ( nextCard.getValue() != 3 )
                {
                    if ( topCard == null )
                    {
                        topCard = nextCard;
                        stackTop.add(nextCard);
                    }
                    else if ( nextCard.getValue() == topCard.getValue() )
                    {
                        stackTop.add(topCard);
                    }
                    else
                    {
                        break;
                    }
                }
            }
            return stackTop;
    }


    protected void pickUpStack( Player player )
    {
        List< Card > pickedUp = new ArrayList<>();
        while ( !stack.isEmpty() )
        {
            Card card = stack.pop();

            pickedUp.add( card );

            player.addCard( ROW.HAND, card );
        }

        notifyPlay( player, "picks up", pickedUp );
    }

    protected void electFaceupCards( Player player, Play play )
    {
        if ( player.hasCardsInHand() )
        {
            // ok
        }
        else if ( player.hasCardsInFaceUp() )
        {
            if ( player.hasCards( ROW.FACEUP, play.cards() ) )
            {
                player.electCards( play.cards() );
            }
            else
            {
                throw new RuntimeException( "Play cards not in faceup" );
            }
        }
        else
        {
            if ( player.hasCards( ROW.BLIND, play.cards() ) )
            {
                player.electCards( play.cards() );
            }
            else
            {
                throw new RuntimeException( "Play cards not in blind" );
            }
        }

    }

    public boolean hasPlayer(String playerName) {
        return players
                .stream()
                .filter(p->p.getName().equals(playerName))
                .findFirst()
                .isPresent();
    }

    public Player getPlayer(String playerName) {
        return players
                .stream()
                .filter(p->p.getName().equals(playerName))
                .findFirst()
                .orElseThrow(()->new RuntimeException("No such player: " + playerName));
    }


    @SuppressWarnings( "serial" )
    class ShitheadException extends RuntimeException
    {

        public ShitheadException( String msg )
        {
            super( msg );
        }

    }

    @SuppressWarnings( "serial" )
    class PickupStackException extends RuntimeException
    {

        public PickupStackException( String msg )
        {
            super( msg );
        }

    }


}