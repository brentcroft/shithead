package com.brentcroft.shithead.www;

import java.io.IOException;
import java.util.*;

import com.brentcroft.shithead.model.Card;
import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.model.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


public class JSONRenderer
{

    public static String render( Object game )
    {
        ObjectMapper objectMapper = new ObjectMapper();
        
        SimpleModule module = new SimpleModule( "GameSerializer", new Version( 1, 0, 0, null, null, null ) );
        
        module.addSerializer( GameModel.class, new GameSerializer() );
        module.addSerializer( Player.class, new PlayerSerializer() );
        module.addSerializer( Card.class, new CardSerializer() );

        objectMapper.setDefaultPrettyPrinter( new DefaultPrettyPrinter() );

        objectMapper.registerModule( module );
        try
        {
            return objectMapper.writeValueAsString( game );
        }
        catch ( JsonProcessingException e )
        {
            throw new RuntimeException( e );
        }
    }


    public static List< Card > stackTop( Stack< Card > stack )
    {
        List< Card > stackTop = new ArrayList<>();

        Card topCard = null;
        for ( int index = stack.size() - 1; index > 0; index-- )
        {
            Card nextCard = stack.elementAt( index );
            if ( nextCard.getValue() != 3 )
            {
                if ( topCard == null )
                {
                    topCard = nextCard;
                    stackTop.add( nextCard );
                }
                else if ( nextCard.getValue() == topCard.getValue() )
                {
                    stackTop.add( topCard );
                }
                else
                {
                    break;
                }
            }
        }
        return stackTop;
    }


    public static class GameSerializer extends StdSerializer< GameModel >
    {
        private static final long serialVersionUID = 5863199541327532448L;

        public GameSerializer()
        {
            this( null );
        }

        public GameSerializer( Class< GameModel > t )
        {
            super( t );
        }

        @Override
        public void serialize( GameModel gameModel, JsonGenerator jsonGenerator, SerializerProvider serializer )
                throws IOException
        {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField( "deck", gameModel.getDeck() );
            jsonGenerator.writeObjectField( "stack", gameModel.getStack() );

            jsonGenerator.writeNumberField( "turnNo", gameModel.getTurnNo() );

            if ( Objects.nonNull( gameModel.getCurrentPlayer() ) )
            {
                jsonGenerator.writeStringField( "nextPlayer", gameModel.getCurrentPlayer().getName() );
            }
            if ( !gameModel.getLastPlayer().isEmpty() )
            {
                jsonGenerator.writeStringField( "lastPlayer", gameModel.getLastPlayer().peek().getName() );
            }

            jsonGenerator.writeArrayFieldStart( "players" );

            gameModel
                    .getPlayers()
                    .forEach( o -> {
                        try
                        {
                            jsonGenerator.writeObject( o );
                        }
                        catch ( IOException e )
                        {
                            throw new RuntimeException( e );
                        }
                    } );

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }


    public static class PlayerSerializer extends StdSerializer< Player >
    {
        private static final long serialVersionUID = -8449182964792214590L;

        public PlayerSerializer()
        {
            this( null );
        }

        public PlayerSerializer( Class< Player > t )
        {
            super( t );
        }

        @Override
        public void serialize( Player player, JsonGenerator jsonGenerator, SerializerProvider serializer )
                throws IOException
        {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField( "name", player.getName() );
            jsonGenerator.writeObjectField( "hand", player.getHandCards() );
            jsonGenerator.writeObjectField( "faceup", player.getFaceUpCards() );
            jsonGenerator.writeObjectField( "blind", Cards.abacinate( player.getBlindCards() ) );
            jsonGenerator.writeEndObject();
        }
    }


    public static class CardSerializer extends StdSerializer< Card >
    {
        private static final long serialVersionUID = -7258822693727310409L;

        public CardSerializer()
        {
            this( null );
        }

        public CardSerializer( Class< Card > t )
        {
            super( t );
        }

        @Override
        public void serialize( Card card, JsonGenerator jsonGenerator, SerializerProvider serializer )
                throws IOException
        {
            jsonGenerator.writeString( card.toString() );
        }
    }
}
