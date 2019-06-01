package com.brentcroft.shithead;


import com.brentcroft.shithead.model.CardList;
import com.brentcroft.shithead.model.Discard;
import com.brentcroft.shithead.model.Player;
import static com.brentcroft.shithead.www.JSONRenderer.render;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@SpringBootApplication
@Controller
@RestController
@EnableAutoConfiguration
public class GameApp
{
    private StandardGame game;

    private StandardGame getGame()
    {
        if ( game == null )
        {
            throw new RuntimeException( "No game" );
        }
        return game;
    }


    public static void main( String[] args )
    {
        SpringApplication.run( GameApp.class, args );
    }    
    

    @RequestMapping( "/" )
    ModelAndView home()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName( "index.html" );
        return modelAndView;
    }


    @RequestMapping( "/cards/playable" )
    String cardsPlayable()
    {
        if ( game == null || getGame().getGameModel().getCurrentPlayer() == null)
        {
            return "[]";
        }

        CardList playable = getGame()
                .getGameModel()
                .getCurrentPlayer()
                .chooseValidCards( getGame().getGameModel().getSelector() );

        return render( playable );
    }



    @RequestMapping( "/cards/recommended" )
    String cardsRecommended()
    {
        if ( game == null || getGame().getGameModel().getCurrentPlayer() == null)
        {
            return "[]";
        }

        CardList playable = getGame()
                .getGameModel()
                .getCurrentPlayer()
                .getDiscard(getGame().getGameModel().getSelector())
                .getCards();

        return render( playable );
    }



    @RequestMapping( "/game" )
    String game()
    {
        if ( game == null)
        {
            return "{}";
        }
        return render( getGame().getGameModel() );
    }


    @RequestMapping( "/new-game" )
    String newGame()
    {
        game = new StandardGame();
        return render( game.getGameModel() );
    }


    @RequestMapping( "/add-player/{name}" )
    String addPlayer( @PathVariable String name )
    {
        StandardGame game = getGame();

        game.addPlayer( new Player( name ) );

        return render( game.getGameModel() );
    }


    @RequestMapping( "/deal" )
    String deal()
    {
        StandardGame game = getGame();

        game.dealCards();

        return render( game.getGameModel() );
    }
    
    @RequestMapping( "/detect-first-player" )
    String detectFirstPlayer()
    {
        StandardGame game = getGame();

        game.detectFirstPlayer();

        return render( game.getGameModel() );
    }



    @RequestMapping( "/play/{name}" )
    String play( @PathVariable String name, @RequestParam("cards") String cardsText )
    {
        StandardGame game = getGame();

        game.playerDiscard(new Discard(name,cardsText));

        return render( game.getGameModel() );
    }
}
