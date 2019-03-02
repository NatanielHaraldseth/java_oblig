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
    
  /**
    * We should have created a MediaPlayer class, but due to this
    * being something we worked on at the very end of the project,
    * we decided to just "make it work".
    */
    MediaPlayer polygonSound = new MediaPlayer(new Media(new File("src/resources/audio/polygon.mp4").toURI().toString()));
    MediaPlayer tinktinkSound = new MediaPlayer(new Media(new File("src/resources/audio/tinktink.mp4").toURI().toString()));
    MediaPlayer sirkelPowSound = new MediaPlayer(new Media(new File("src/resources/audio/sirkelPow.mp4").toURI().toString()));
    

    
    public Ball(int x, int y, int r){
       super(x, y, r);
       ballID = teller;
       teller++;
       
    }
    
    

    
  /**
    * @author Anders Bærø
     * @param ball takes the players ball.
     * 
     * @param elapsedSeconds takes currently elapsed seconds within
     * the AnimationTimer handle. This is to calculate gravity.
     * 
     * @param pane takes the pane to be able to get dimensions to 
     * check collisions with the walls/roof etc.
     * 
     * This methods primary function is to check if the ball is within the bounds
     * of the "playboard", and if it collides with either edges, it needs to
     * turn around. This method also applies gravity if the ball doesn't collide
     * with anything.
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
    * @author Anders Bærø
    * @param ball takes the players ball.
    * 
    * @param s takes Shape s to check collisions with obstacleArr. 
    * This is taken from the for-each loop running in AnimationTimer.
    * 
    * @param resultSet takes the "scoreboard" so we can give the player points
    * for hitting different obstacles.
    * 
    * This method first check what kind of shape the current selected shape in 
    * obstacleArr is, it then checks whether or not the ball has collided with it. 
    * 
    * Within this section we check collisions between Flippers, Balls, and Lines,
    * and then apply the appropriate "response" in terms of the balls trajectory.
    * 
    */
    public void checkCollision(Ball ball, Shape s, ScoreCard resultSet){
        if(s instanceof Flipper){
            Shape shape = Shape.intersect(ball, s);
            boolean intersects = shape.getBoundsInLocal().getWidth() != -1;
         
            if(intersects){
                resultSet.addScore(800);
                if((((Flipper) s).getRotation()) == Rotation.RIGHT){
                    
                    //If the ball collides with the RIGHT flipper, and the flippers location is NOT level.
                    //That meaning it is currently laying flat and not being used.
                    //The ball will change trajectory towards the right.
                    if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() < (((Flipper) s).getStartY())-10){
                        changeY = - changeY * elasticity - (BOUNCE + 1);
                        changeX = - changeX * elasticity + (BOUNCE + 1);
                    
                    //If the ball collides while the flipper is laying flat, only change the Y trajectory
                    }else if(ball.getCenterY()+ball.getRadius()+ball.getChangeY() > (((Flipper) s).getStartY())){
                        changeY = - changeY * elasticity - 1;
                    }
                
                //Exact same process as for the RIGHT one, except opposite directions.
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
                
                //Here we have split the ball into four parts. Much like a 4-piece pizza.
                //If the players ball centerX is currently less then other balls X, and 
                //the players ball centerY is currently less than other balls Y we can
                //see that we should bounce the ball -y and -x.
                if(ball.getCenterX() < (((Circle) s).getCenterX()) && ball.getCenterY() < (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX =  changeX * elasticity - BOUNCE;

                //Bounce +y -x   
                }else if(ball.getCenterX() < (((Circle) s).getCenterX()) && ball.getCenterY() > (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity + BOUNCE; 
                    changeX =  changeX * elasticity - BOUNCE;

                //Bounce -y +x   
                }else if(ball.getCenterX() > (((Circle) s).getCenterX()) && ball.getCenterY() < (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity - BOUNCE;
                    changeX =  changeX * elasticity + BOUNCE;

                //Bounce +y +x   
                }else if(ball.getCenterX() > (((Circle) s).getCenterX()) && ball.getCenterY() > (((Circle) s).getCenterY())){
                    changeY = - changeY * elasticity + BOUNCE;
                    changeX =  changeX * elasticity + BOUNCE;
                }
            }
                            
            
      /**
        * Fair warning: This piece of code is a nightmare.
        * It's somehow my best and worst solution to a problem.
        * 
        * So, collision with a line(which are our mostly obstacles)
        * turned out to be a huge challenge considering all the different ways a
        * ball could collide with one single line. What we did, in a panic-induced last attempt,
        * was to color-code each of the 10 different ways we used lines, and give them appropriate 
        * responses. 
        * 
        * For example, a red line would be used on our "polygon" obstacles, and would provide more
        * "boost" than a regular brown line. Both brown and red lines move the ball -x -y.
        * 
        * I'm having a hard time trying to explain this, and if you have any questions,
        * I'll be more than happy to answer them.
        * 
        */    
        }else if(s instanceof Line){
            Shape shape = Shape.intersect(ball, s);
            boolean intersects = shape.getBoundsInLocal().getWidth() != -1;
            
            if(intersects){
                
                resultSet.addScore(500);          
                //45grader = Venstre nedre hjørne
                if((((Line) s).getStroke()) == Color.BLUE){
                    if(polygonSound.getStatus() == MediaPlayer.Status.PLAYING){
                        polygonSound.stop();
                        polygonSound.play();
                    }else{
                        polygonSound.play();
                    }
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
    
    
  /** @author Anders Bærø
    * Respawns the ball.
    * 
     * @param x takes X-value.
     * @param y takes Y-value.
    */
    
    public void respawnBall(int x, int y){
        this.setCenterX(x);
        this.setCenterY(y);
    }
    
  /** @author Anders Bærø
    * Alternative method for checking collision
    * with other balls.
    * 
    * 
    * @param b takes another ball to compare.
    */
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
    
    /** @author Anders Bærø
    * Methods for playing, preparing and stopping sound-effect "tink".
    */
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

}
