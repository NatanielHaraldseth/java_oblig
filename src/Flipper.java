/**
 * A class that defines a Rectangle as a flipper.
 * Method to animate the behavior of the flipper, dependant on if it is left or right.
 * 
 */
package springandflipper;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 * @version 2.4
 * @author Ove Simon Wernersson
 */
public class Flipper extends Line{
    private final Rotation rotation;
    int rotate = 0;
    /**
     * Constructor method that inherits from the superclass line, therefore
     * it makes a line.
     * 
     * @param rotation The enum direction decides if it is the right or left side of the flipper that flips
     * @param startX The position which the line starts from horizontaly
     * @param startY The postition which the line starts from vertically
     * @param endX  The position which the line ends horizontally
     * @param endY  The position which the line ends vertically
     */
    public Flipper(Rotation rotation, double startX, double startY, double endX, double endY) {      
        super(startX, startY, endX, endY);
        this.rotation = rotation;
        if(rotation == Rotation.LEFT) {
            this.setRotate(20);
        } else if (rotation == Rotation.RIGHT) {
            this.setRotate(-20);
        }
    }

    /***
     * 
     * @return Returns the rotation direction of the flipper
     */
    public Rotation getRotation() {
        return rotation;
    }
    
    /**
     * Moves the flipper in the right direction depending on the enum LEFT or RIGHT
     * 
     * @param flipper A flipper must be a subclass of Line, with startX, startY, endX and endY parameters
     * 
     * 
     */
    public void flipTheFlipper(Flipper flipper){
        
        AnimationTimer timer2 = new AnimationTimer() {

                            
                            double currentRotate = -20;
                            double increment = 10;
                            final int MAX_ROTATE = 40;
                            final int MIN_ROTATE = -20;
                            boolean isUp = false;
                            
                            
                            @Override
                            public void handle(long now) {
                                
                                if(rotation == Rotation.LEFT) {
                                    
                                    if(currentRotate < MAX_ROTATE && !isUp) {
                                    flipper.getTransforms().add(new Rotate(-increment,flipper.getStartX(),flipper.getStartY()));
                                    currentRotate += increment;
                                        if(currentRotate == MAX_ROTATE) {
                                            isUp = true;
                                        }
                                   
                                    } else if(currentRotate > MIN_ROTATE && isUp) {
                                        flipper.getTransforms().add(new Rotate(increment,flipper.getStartX(),flipper.getStartY()));
                                        currentRotate -= increment;
                                    } 
                                } else {
                                    if(currentRotate < MAX_ROTATE && !isUp) {
                                    flipper.getTransforms().add(new Rotate(increment,flipper.getEndX(),flipper.getEndY()));
                                    currentRotate += increment;
                                        if(currentRotate == MAX_ROTATE) {
                                            isUp = true;
                                        }
                                    }else if(currentRotate > MIN_ROTATE && isUp) {
                                        flipper.getTransforms().add(new Rotate(-increment,flipper.getEndX(),flipper.getEndY()));
                                        currentRotate -= increment;

                                    }
                                }
                                
                            }

                        };
                     timer2.start(); 
    }
    

    
}
