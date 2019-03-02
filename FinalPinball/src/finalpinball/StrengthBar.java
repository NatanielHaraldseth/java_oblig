/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpinball;


import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * @version 1.0
 * @author Ove Simon Wernersson
 */
public class StrengthBar extends Rectangle {
    private double currentHeight = 0;
    private AnimationTimer moveProgress;
    MediaPlayer wowPerfectShotSound;
    MediaPlayer shosLaunchSound;
    
    
    public StrengthBar(double x, double y, double width, double height) {
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
        wowPerfectShotSound = new MediaPlayer(new Media(new File("src/resources/audio/wowPerfectShot.mp4").toURI().toString()));
        shosLaunchSound = new MediaPlayer(new Media(new File("src/resources/audio/shosLaunch.mp4").toURI().toString()));

        moveProgress.stop();
        if(currentHeight > 200) {
            if(wowPerfectShotSound.getStatus() == MediaPlayer.Status.PLAYING){
                wowPerfectShotSound.stop();
                wowPerfectShotSound.play();
            }else{
                wowPerfectShotSound.play();
            }
            text.setText("PERFECT SHOT!");
        } else {
            shosLaunchSound.play();
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