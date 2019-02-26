/**
 * A class that defines a Rectangle as a flipper.
 * Method to animate the behavior of the flipper, dependant on if it is left or right.
 * 
 */
package springandflipper;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * @version 2.0
 * @author Ove Simon Wernersson
 */
public class Flipper extends Rectangle{
    
    
    /**
     * Constructor method that inherits from the superclass rectangle, therefore
     * it makes a rectangle.
     * 
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle, how wide you want the rectangle to be.
     * @param height The height of the rectangle, how high you want the rectangle to be.
     */
    public Flipper(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    
    /**
     * Moves the flipper in the right direction depending on the enum LEFT or RIGHT
     * 
     * @param flipper A flipper must be a subclass of Rectangle, with x,y,width and height
     * 
     * @param direction The enum direction decides if it is the right or left side of the flipper that flips
     */
    public void flipTheFlipper(Flipper flipper, Direction direction){
        AnimationTimer timer2 = new AnimationTimer() {
                            double currentRotate = -20;
                            double increment = 10;
                            final int MAX_ROTATE = 40;
                            final int MIN_ROTATE = -20;
                            boolean isUp = false;
                            
                            
                            @Override
                            public void handle(long now) {
                                if(direction == Direction.LEFT) {
                                    if(currentRotate < MAX_ROTATE && !isUp) {
                                    flipper.getTransforms().add(new Rotate(-increment,flipper.getX(),flipper.getY()+(flipper.getHeight())/2));
                                    currentRotate += increment;
                                    if(currentRotate == MAX_ROTATE) {
                                        isUp = true;
                                    }
                                   
                                    } else if(currentRotate > MIN_ROTATE && isUp) {
                                        flipper.getTransforms().add(new Rotate(increment,flipper.getX(),flipper.getY()+(flipper.getHeight())/2));
                                        currentRotate -= increment;

                                    } 
                                } else {
                                    if(currentRotate < MAX_ROTATE && !isUp) {
                                    flipper.getTransforms().add(new Rotate(increment,flipper.getX()+flipper.getWidth(),flipper.getY()+(flipper.getHeight())/2));
                                    currentRotate += increment;
                                        if(currentRotate == MAX_ROTATE) {
                                            isUp = true;
                                        }
                                    }else if(currentRotate > MIN_ROTATE && isUp) {
                                        flipper.getTransforms().add(new Rotate(-increment,flipper.getX()+flipper.getWidth(),flipper.getY()+ (flipper.getHeight())/2));
                                        currentRotate -= increment;

                                    }
                                }
                                
                            }
                            
                        };
                     timer2.start(); 
    }
    
    
    
}
