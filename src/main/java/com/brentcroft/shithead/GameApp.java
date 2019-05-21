package com.brentcroft.shithead;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RestController
@EnableAutoConfiguration
public class GameApp {

    Game game;

    @RequestMapping("/")
    ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/game")
    String game() {
        if ( game == null )
        {
            game = new Game();
        }
        return JSONRenderer.render(game);
    }


    @RequestMapping("/add-player/{name}")
    String addPlayer(@PathVariable String name) {
        if ( game == null )
        {
            game = new Game();
        }

        game.addPlayer(new Player(name));

        return JSONRenderer.render(game);
    }


    @RequestMapping("/deal")
    String deal() {
        if ( game == null )
        {
            throw new RuntimeException("No game");
        }

        game.deal();

        return JSONRenderer.render(game);
    }



    public static void main(String[] args) {
        SpringApplication.run(GameApp.class, args);
    }
}
