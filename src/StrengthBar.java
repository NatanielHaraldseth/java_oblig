
package springandflipper; // TODO - Change to the right package

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * @version 1.0
 * @author Ove Simon Wernersson
 */
public class StrenghtBar extends Rectangle {
    private double currentHeight = 0;
    private AnimationTimer moveProgress;
    
    
    public StrenghtBar(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    
    
    /**
     * Method for changing height on the progressbar, need to be used onMouseClicked
     * 
     * @param progressBar Rectangle for changing its height.
     */
    public void progressBar(Rectangle progressBar) {
        
        currentHeight = 0;
        moveProgress = new AnimationTimer() {
            
           
            double heightMax = 210;
            double heightGrow = 5;
            @Override
            public void handle(long now) {
                if(currentHeight < heightMax) {
                    currentHeight += heightGrow;
                    progressBar.setHeight(currentHeight);
                } else if(currentHeight == heightMax) {
                    currentHeight = 0;
                }
                
                
            }
        };
        moveProgress.start();
    }
    /**
     * Stopping the progressbar, when mousebutton is released.
     * 
     * @param text Text for changing status message.
     */
        public void progressStop(Text text) {
        moveProgress.stop();
        if(currentHeight > 200) {
            text.setText("PERFECT SHOT!");
        } else {
            text.setText("");
        }
    }
    /**
     * Returns the currentHeight when bar is stopped, needed for speed on ball launch.
     * @return currentHeight - double.
     */
    public double getCurrentHeight() {
        return this.currentHeight;
    }
        
    
    
}
