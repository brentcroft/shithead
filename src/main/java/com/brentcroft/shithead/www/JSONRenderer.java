package com.brentcroft.shithead.www;

import com.brentcroft.shithead.model.Cards;
import com.brentcroft.shithead.model.GameModel;
import com.brentcroft.shithead.StandardGame;
import com.brentcroft.shithead.model.Player;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class JSONRenderer {

    public static String render(StandardGame game) {
        String r = renderX(game);

        //log.warn(r);

        return r;
    }

    public static String renderX(StandardGame game) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(
                "GameSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(GameModel.class, new GameSerializer());
        module.addSerializer(Player.class, new PlayerSerializer());
        module.addSerializer(Cards.Card.class, new CardSerializer());

        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Cards.Card> stackTop(Stack<Cards.Card> stack)
    {
        List<Cards.Card> stackTop = new ArrayList<>();

        Cards.Card topCard = null;
        for ( int index = stack.size() - 1; index > 0; index-- )
        {
            Cards.Card nextCard = stack.elementAt( index );
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


    public static class GameSerializer extends StdSerializer<GameModel> {

        public GameSerializer() {
            this(null);
        }

        public GameSerializer(Class<GameModel> t) {
            super(t);
        }

        @Override
        public void serialize(GameModel gameModel, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("deck", gameModel.getDeck());
            jsonGenerator.writeObjectField("cards", stackTop(gameModel.getStack()));
            jsonGenerator.writeArrayFieldStart("players");

            gameModel
                    .getPlayers()
                    .forEach(o -> {
                        try {
                            jsonGenerator.writeObject(o);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();
        }
    }


    public static class PlayerSerializer extends StdSerializer<Player> {

        public PlayerSerializer() {
            this(null);
        }

        public PlayerSerializer(Class<Player> t) {
            super(t);
        }

        @Override
        public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", player.getName());
            jsonGenerator.writeObjectField("hand", player.getHandCards());
            jsonGenerator.writeObjectField("faceup", player.getFaceUpCards());
            jsonGenerator.writeObjectField("blind", Cards.abacinate(player.getBlindCards()));
            jsonGenerator.writeEndObject();
        }
    }


    public static class CardSerializer extends StdSerializer<Cards.Card> {

        public CardSerializer() {
            this(null);
        }

        public CardSerializer(Class<Cards.Card> t) {
            super(t);
        }

        @Override
        public void serialize(Cards.Card card, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeString(card.toString());
        }
    }
}
