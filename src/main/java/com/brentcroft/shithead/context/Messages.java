package com.brentcroft.shithead.context;

public interface Messages
{


    String NO_MORE_CARDS = "No more cards";

    String CARDS_ALREADY_DEALT = "Cards already dealt";
    String NOT_ENOUGH_PLAYERS = "Not enough players";


    String CARDS_NOT_DEALT = "Cards not dealt";
    String PLAYER_ALREADY_EXISTS = "Player already exists: %s";

    String CARD_NOT_IN_CURRENT_ROW = "Card [%s] not in current row: %s";

    String NOT_YOUR_TURN = "Not your turn";
    String INVALID_PLAY_NO_CARDS = "Invalid play, no cards";
    String INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES = "Invalid play, cards %s different values: %s %s";
    String INVALID_PLAY_CARDS_NOT_IN_HAND = "Invalid play, cards %s not in hand: %s %s";
    String INVALID_PLAY_CARDS_NOT_IN_FACEUP = "Invalid play, cards %s not in faceup: %s %s";

    String FIRST_PLAYER_NOT_SELECTED = "First player not selected";
    String CURRENT_PLAYER_ALREADY_EXISTS = "Current player already exists";
    String NO_FIRST_PLAYER = "No first player";

    String PLAYER_COULD_HAVE_PLAYED = "Player [%s] could have played: %s";

    String DISCARD_NOT_IN_FACEUP = "Discard not in faceup";
    String DISCARD_NOT_IN_BLIND = "Discard not in blind";
}
