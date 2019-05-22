package com.brentcroft.shithead.context;

public interface Messages
{

    String CARDS_ALREADY_DEALT = "Cards already dealt";


    String CARDS_NOT_DEALT = "Cards not dealt";
    String PLAYER_ALREADY_EXISTS = "PLAYER_ALREADY_EXISTS";

    String NOT_YOUR_TURN = "Not your turn";
    String INVALID_PLAY_NO_CARDS = "Invalid play, no cards";
    String INVALID_PLAY_CARDS_OF_DIFFERENT_VALUES = "Invalid play, cards different values: %s %s";
    String INVALID_PLAY_CARDS_NOT_IN_HAND = "Invalid play, cards not in hand: %s %s";
    String INVALID_PLAY_CARDS_NOT_IN_FACEUP = "Invalid play, cards not in faceup: %s %s";

    String FIRST_PLAYER_NOT_SELECTED = "First player not selected";
    String CURRENT_PLAYER_ALREADY_EXISTS = "Current player already exists";
    String NO_FIRST_PLAYER = "No first player";
    
    String PLAYER_COULD_HAVE_PLAYED = "Player could have played";
    
    String DISCARD_NOT_IN_FACEUP = "Discard not in faceup";
    String DISCARD_NOT_IN_BLIND = "Discard not in blind";
}
