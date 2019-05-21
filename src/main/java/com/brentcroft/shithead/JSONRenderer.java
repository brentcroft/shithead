package com.brentcroft.shithead;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JSONRenderer {

    public static String render(Game game) {
        String r = renderX(game);

        //log.warn(r);

        return r;
    }

    public static String renderX(Game game) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(
                "GameSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(Game.class, new GameSerializer());
        module.addSerializer(Player.class, new PlayerSerializer());
        module.addSerializer(Cards.Card.class, new CardSerializer());

        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static class GameSerializer extends StdSerializer<Game> {

        public GameSerializer() {
            this(null);
        }

        public GameSerializer(Class<Game> t) {
            super(t);
        }

        @Override
        public void serialize(Game game, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("deck",game.cards.getCards());
            jsonGenerator.writeObjectField("cards", game.stackTop());
            jsonGenerator.writeArrayFieldStart("players");

            game.players.forEach(o -> {
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
        public void serialize( Player player, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", player.getName());
            jsonGenerator.writeObjectField("hand", player.getHandCards());
            jsonGenerator.writeObjectField("faceup", player.getFaceUpCards());
            jsonGenerator.writeObjectField("blind", player.getBlindCards());
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
        public void serialize( Cards.Card card, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeString(card.toString());
        }
    }
}
