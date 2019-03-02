/**
 * Class that extends circle into a playable ball.
 * This class contains methods for gravity, as well as 
 * collisions. It then deals with said collisions dependant
 * on what kind of shape the ball has collided with.
 */
package finalpinball;

import static finalpinball.FinalPinball.BOUNCE;
import static finalpinball.FinalPinball.PLAY_BOARD;
import java.io.File;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author Anders
 */
public class Ball extends Circle{
    static int teller = 0;
    int ballID;
    long lastUpdate = 0;
    double changeX = 0;
    int lives = 5;
    double changeY = 0 ;
    double gravity = 3 ; //egt 50
    double elasticity = 0.5 ; //egt 0.5
    
    MediaPlayer polygonSound = new MediaPlayer(new Media(new File("src/resources/audio/polygon.mp4").toURI().toString()));
    MediaPlayer tinktinkSound = new MediaPlayer(new Media(new File("src/resources/audio/tinktink.mp4").toURI().toString()));
    MediaPlayer sirkelPowSound = new MediaPlayer(new Media(new File("src/resources/audio/sirkelPow.mp4").toURI().toString()));
    

    
    public Ball(int x, int y, int r){
       super(x, y, r);
       ballID = teller;
       teller++;
       
    }

    public double getElasticity() {
        return elasticity;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public double getChangeX() {
        return changeX;
    }

    public double getChangeY() {
        return changeY;
    }
    
    public void setChangeX(double changeX) {
        this.changeX = changeX;
    }

    public void setChangeY(double changeY) {
        this.changeY = changeY;
    }
    
  /**
    * WRITE THIS SHIT
    * @param 
    * 
    */
    public void moveBall(Ball ball, double elapsedSeconds, Pane pane){
        ball.setCenterX(ball.getCenterX() + changeX);
        
        //Checks if the ball is colliding with the bottom.
        if (ball.getCenterY() + changeY + ball.getRadius() >= (pane.getHeight()-20)) {
            
            changeY = - changeY * elasticity;
        
            //Checks if the ball is colliding with the top.
        }else if(ball.getCenterY() + changeY + ball.getRadius() <= 10){
            playTink();
            changeY = - changeY * elasticity;

        //Checks if the ball is colliding with the right hand side.
        }else if(ball.getCenterX() + changeX + ball.getRadius() >= (PLAY_BOARD-10)) {
            playTink();
            changeX = - changeX * elasticity;

        //Checks if the ball is colliding with the left hand side.
        }else if(ball.getCenterX() + changeX + ball.getRadius() < (10 + ball.getRadius()*2)) {
            playTink();
            changeX = - changeX * elasticity;

        //Lets the ball fall in normal gravity unless there are no collisions.
        }else {
            changeY = changeY + gravity * elapsedSeconds ;
        }
                    
        ball.setCenterY(Math.min(ball.getCenterY() + changeY, pane.getHeight() - ball.getRadius()));
                    
    }
    
    /**
    * WRITE THIS SHIT
    * @param 
    * 
    */
    public void checkCollision(Ball ball, Shape s, ScoreCard resultSet){
        if(s instanceof Flipper){
            Shape shape = Shape.intersect(ball, s);
            boolean intersects = shape.getBoundsInLocal().getWidth() != -1;
         
            if(intersects){
                resultSet.addScore(800);
                if((((Flipper) s).getRotation()) == Rotation.RIGHT){
                    
                    //Skal sprette mot venstre
                    if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() < (((Flipper) s).getStartY())-10){
                        changeY = - changeY * elasticity - (BOUNCE + 1);
                        changeX = - changeX * elasticity + (BOUNCE + 1);
                    
                    }else if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() > (((Flipper) s).getStartY())){
                        changeY = - changeY * elasticity - 1;
                    }
                    
                }else if((((Flipper) s).getRotation()) == Rotation.LEFT){
                    if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() < (((Flipper) s).getStartY())-10){
                        changeY = - changeY * elasticity - (BOUNCE+1);
                        changeX = - changeX * elasticity - (BOUNCE+1);
                    
                    }else if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() > (((Flipper) s).getStartY())){
                        changeY = - changeY * elasticity - 1;
                        
                    }
                }
            }
        
        }else if(s instanceof Ball){
            if((((Ball) s).hasCollided(ball))){
                if(sirkelPowSound.getStatus() == MediaPlayer.Status.PLAYING){
                    sirkelPowSound.stop();
                    sirkelPowSound.play();
                }else{
                    sirkelPowSound.play();
                }
                resultSet.addScore(1300);
                
                //øvre venstre = nedre høyre
                if(ball.getCenterX() < (((Circle) s).getCenterX()) && ball.getCenterY() < (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX =  changeX * elasticity - BOUNCE;

                //nedre venstre = øvre høyre    
                }else if(ball.getCenterX() < (((Circle) s).getCenterX()) && ball.getCenterY() > (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity + BOUNCE; 
                    changeX =  changeX * elasticity - BOUNCE;

                //Øvre høyre = nedre venstre
                }else if(ball.getCenterX() > (((Circle) s).getCenterX()) && ball.getCenterY() < (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX =  changeX * elasticity + BOUNCE;

                //nedre høyre = øvre venstre
                }else if(ball.getCenterX() > (((Circle) s).getCenterX()) && ball.getCenterY() > (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity + BOUNCE;
                    changeX =  changeX * elasticity + BOUNCE;
                }
            }
                            
        }else if(s instanceof Line){
            Shape shape = Shape.intersect(ball, s);
            boolean intersects = shape.getBoundsInLocal().getWidth() != -1;
            
            
          /**
            * WRITE THIS SHIT
            * 
            */
            if(intersects){
                
                resultSet.addScore(500);          
                //45grader = Venstre nedre hjørne
                if((((Line) s).getStroke()) == Color.BLUE){
                    polygonSound.play();
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX = - changeX * elasticity + BOUNCE;
            
                //135grader = Venstre øvre hjørne
                }else if((((Line) s).getStroke()) == Color.YELLOW){
                    playTink();
                    changeY = - changeY * elasticity + BOUNCE;
                    changeX = - changeX * elasticity + BOUNCE;

                //225grader = høyre øvre hjørne
                }else if((((Line) s).getStroke()) == Color.GREEN){
                    playTink();
                    changeY = - changeY * elasticity + BOUNCE;
                    changeX = - changeX * elasticity - BOUNCE+4;

                //315grader = høyre nedre hjørne
                }else if(((((Line) s).getStroke()) == Color.RED)){
                    if(polygonSound.getStatus() == MediaPlayer.Status.PLAYING){
                        polygonSound.stop();
                        polygonSound.play();
                    }else{
                        polygonSound.play();
                    }
                    
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX = - changeX * elasticity - BOUNCE;
                
                }else if(((((Line) s).getStroke()) == Color.WHITE)){
                    playTink();
                    changeX = - changeX * elasticity - 0.2;
                    
                }else if(((((Line) s).getStroke()) == Color.FIREBRICK)){
                    playTink();
                    changeX = - changeX * elasticity +0.01;
                    
                }else if(((((Line) s).getStroke()) == Color.ORANGE)){
                    playTink();
                    changeY = - changeY * elasticity - 0.2;
                    changeX = - changeX * elasticity + 0.01;
                    
                }else if(((((Line) s).getStroke()) == Color.BROWN)){
                    playTink();
                    changeY = - changeY * elasticity - 0.2;
                    changeX = - changeX * elasticity - 0.01;
                    
                }else if(((((Line) s).getStroke()) == Color.MAGENTA)){
                    playTink();
                    changeY = - changeY * elasticity;
                    
                }else if(((((Line) s).getStroke()) == Color.BLACK)){
                    playTink();
                    changeY = - changeY * elasticity;
                    changeX = - changeX * elasticity;
                }
            }
        }
    }
    
    
    public void respawnBall(int x, int y){
        this.setCenterX(x);
        this.setCenterY(y);
    }
    
    public boolean hasCollided(Ball b){
        double dx = this.getCenterX()-b.getCenterX();
        double dy = this.getCenterY()-b.getCenterY();
        
        if(Math.sqrt(dx*dx+dy*dy) <= this.getRadius()+b.getRadius())
            return true;
        else
            return false;
    }
    
    public int getBallID() {
        return ballID;
    }
    
    public void stopTink(){
        tinktinkSound.stop();
    
    }
    
    public void playTink(){
        if(tinktinkSound.getStatus() == MediaPlayer.Status.PLAYING){
            tinktinkSound.stop();
            tinktinkSound.play();
        }else{
            tinktinkSound.play();
        }
    }

}
