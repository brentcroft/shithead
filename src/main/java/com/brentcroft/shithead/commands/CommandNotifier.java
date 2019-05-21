package com.brentcroft.shithead.commands;

import com.brentcroft.shithead.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@Slf4j
public class CommandNotifier {

    public void notifyPlay(Player player, Object action, Object cards )
    {
        log.info( format( "[%-8s] %-15s %s", player == null ? "*" : player.getName(), action, cards ) );
    }
}
