package com.brentcroft.shithead;


import com.brentcroft.shithead.model.Player;
import static com.brentcroft.shithead.www.JSONRenderer.render;
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
public class GameApp
{
    private StandardGame game;

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

    @RequestMapping( "/game" )
    String game()
    {
        if ( game == null )
        {
            game = new StandardGame();
        }
        return render( game.getGameModel() );
    }


    @RequestMapping( "/add-player/{name}" )
    String addPlayer( @PathVariable String name )
    {
        if ( game == null )
        {
            game = new StandardGame();
        }

        game.addPlayer( new Player( name ) );

        return render( game.getGameModel() );
    }


    @RequestMapping( "/deal" )
    String deal()
    {
        if ( game == null )
        {
            throw new RuntimeException( "No game" );
        }

        game.dealCards();

        return render( game.getGameModel() );
    }
    
    @RequestMapping( "/detect-first-player" )
    String detectFirstPlayer()
    {
        if ( game == null )
        {
            throw new RuntimeException( "No game" );
        }

        game.detectFirstPlayer();

        return render( game.getGameModel() );
    }    
    
}
