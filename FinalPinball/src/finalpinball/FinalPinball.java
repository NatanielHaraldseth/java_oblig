/**
 * 
 * 
 */
package finalpinball;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author Anders Bærø & Isak Hauge
 */
public class FinalPinball extends Application {
    
    final static int VINDU = 800;
    final static int PLAY_BOARD = 500;
    final static double BOUNCE = 0.5;
    
    double launchPower = 0;
    
    AnimationTimer timer;
    Scene scoreScene;
    Stage scoreStage;
    
    //Music files for audio effects
    MediaPlayer gameOverLatterSound = new MediaPlayer(new Media(new File("src/resources/audio/gameOverLatter.mp4").toURI().toString()));
    MediaPlayer gameOverSound = new MediaPlayer(new Media(new File("src/resources/audio/gameOver.mp4").toURI().toString()));;
    MediaPlayer flipperSound = new MediaPlayer(new Media(new File("src/resources/audio/flipper.mp4").toURI().toString()));
    MediaPlayer themeSong = new MediaPlayer(new Media(new File("src/resources/audio/themeSong.mp4").toURI().toString()));
    
    @Override
    public void start(Stage primaryStage) {
        
        Pane pane = new Pane();
        Scene scene = new Scene(pane, VINDU, VINDU);
        
        themeSong.play();
       
        
        /////////////////////////////////////////////////////////
        ////////////////////Drawing the board////////////////////
        /////////////////////////////////////////////////////////
        
        //Setting a background for the application.
        ImageView background = new ImageView("file:src/resources/images/bg.png");
        pane.getChildren().add(background);
        
        //Arraylist for storing every shape used as an obstacle.
        ArrayList<Shape> obstacleArr = new ArrayList<Shape>();
        
        //Creating balls and adding them to obstacleArr.
        Ball b1 = new Ball(140, 250, 34);
        Ball b3 = new Ball(315, 250, 34);
        obstacleArr.add(b1);
        obstacleArr.add(b3);
        b1.setVisible(false);
        b3.setVisible(false);
        
        //Creates a "powerbar" to allow the user to 
        //decide for 'hen'self what kind of power they want.
        StrengthBar strengthBarOutline = new StrengthBar(529, 578, 20, 210);
        StrengthBar strengthBar = new StrengthBar(529, 578, 20, 210);
        Text progressText = new Text();
        strengthBarOutline.setFill(null);
        strengthBar.setFill(Color.FIREBRICK);
        strengthBarOutline.setStroke(Color.WHITE);
        strengthBar.setHeight(0);
        strengthBar.getTransforms().add(new Rotate(180,strengthBarOutline.getX()+strengthBarOutline.getWidth()/2,strengthBarOutline.getY()+strengthBarOutline.getHeight()/2));
        progressText.setX(strengthBarOutline.getX() + 80);
        progressText.setY(strengthBarOutline.getY() + strengthBarOutline.getHeight()/2);
        progressText.setFill(Color.ORANGERED);
        progressText.setStyle("-fx-font-size: 24;");
        
        pane.getChildren().addAll(strengthBar, strengthBarOutline, progressText);
        
        //Players ball
        Ball ball = new Ball(180, 200, 15);
        ball.setFill(Color.WHITESMOKE);
        pane.getChildren().add(ball);
        
        //Creating polygon obstacle one
        //Polygon lines are named poorly, but 
        //we have named them pl for poly-line, and
        //then numbered them. 1-3 is the first obstacle
        //on the left hand side.
        Line pl1 = new Line(22, 463, 120, 610);
        pl1.setStrokeWidth(10);
        pl1.setStroke(Color.BLUE);
        
        Line pl2 = new Line(120, 610, 22, 610);
        pl2.setStrokeWidth(10);
        pl2.setStroke(Color.MAGENTA);
        
        Line pl3 = new Line(22, 610, 22, 463);
        pl3.setStrokeWidth(10);
        pl3.setStroke(Color.WHITE);
        
        obstacleArr.add(pl1);
        obstacleArr.add(pl2);
        obstacleArr.add(pl3);
        
        //Creating polygon obstacle two on the
        //right hand side.
        Line pl4 = new Line(433, 463, 337, 610);
        pl4.setStrokeWidth(10);
        pl4.setStroke(Color.RED);
        
        Line pl5 = new Line(337, 610, 433, 610);
        pl5.setStrokeWidth(10);
        pl5.setStroke(Color.MAGENTA);
        
        Line pl6 = new Line(433, 610, 433, 463);
        pl6.setStrokeWidth(10);
        pl6.setStroke(Color.FIREBRICK);
        
        obstacleArr.add(pl4);
        obstacleArr.add(pl5);
        obstacleArr.add(pl6);
        
        
        //Drawing lower left corner
        //Lines for each of the "corners" are named as
        //LL = LowerLeft etc.
        Line lineLL = new Line(12, 634, 168, 788);
        lineLL.setStrokeWidth(10);
        lineLL.setStroke(Color.ORANGE);
        obstacleArr.add(lineLL);
        
        //Drawing upper left corner
        Line lineUL = new Line(12, 113, 112, 12);
        lineUL.setStrokeWidth(10);
        lineUL.setStroke(Color.YELLOW);
        obstacleArr.add(lineUL);
        
        //Drawing upper right corner
        Line lineUR = new Line(401, 12, 505, 118);
        lineUR.setStrokeWidth(10);
        lineUR.setStroke(Color.GREEN);
        obstacleArr.add(lineUR);
        
        //Drawing lower right corner
        Line lineLR = new Line(284, 788, 439, 634);
        lineLR.setStrokeWidth(10);
        lineLR.setStroke(Color.BROWN);
        obstacleArr.add(lineLR);
        
        //Drawing launcher/walls.
        Line lineLauncher = new Line(439, 634, 439, 150);
        lineLauncher.setStrokeWidth(10);
        lineLauncher.setStroke(Color.WHITE);
        obstacleArr.add(lineLauncher);
        
        //Drawing bottom line
        Line lineBottom = new Line(168, 788, 284, 788);
        lineBottom.setStrokeWidth(10);
        lineBottom.setStroke(Color.BLACK);
        obstacleArr.add(lineBottom);
        
        //Sets every drawn line invisible except for lineLauncher.
        for(Shape s : obstacleArr){
            if(s instanceof Line){
                s.setVisible(false);
            }
        }
        
        lineLauncher.setVisible(true);
       
        //Creating the left flipper.
        Flipper leftFlipper = new Flipper(Rotation.LEFT, 120, 610, 195, 610);
        leftFlipper.setStroke(Color.DARKORANGE);
        leftFlipper.setStrokeWidth(15);
        leftFlipper.setStrokeLineCap(StrokeLineCap.ROUND);
        obstacleArr.add(leftFlipper);
        
        //Creating the right flipper
        Flipper rightFlipper = new Flipper(Rotation.RIGHT, 260, 610, 337, 610);
        rightFlipper.setStroke(Color.DARKORANGE);
        rightFlipper.setStrokeWidth(15);
        rightFlipper.setStrokeLineCap(StrokeLineCap.ROUND);
        obstacleArr.add(rightFlipper);
        
        //Create text for displaying score.
        Text scoreText = new Text();
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 24;");
        scoreText.setX(630);
        scoreText.setY(375);
        
        //Create text for displaying remaining lives.
        Text lifeText = new Text();
        lifeText.setFill(Color.WHITE);
        lifeText.setStyle("-fx-font-size: 18;");
        lifeText.setX(570);
        lifeText.setY(400);
        
        pane.getChildren().addAll(scoreText, lifeText);
        
        //Creates a scorecard for keeping track
        //and managing results.
        ScoreCard resultSet = new ScoreCard();
        
        
        //Adds the entire obstacleArr to the pane.
        pane.getChildren().addAll(obstacleArr);
        
        
        /////////////////////////////////////////////////////////
        ////////////////////Game-over Scoreboard/////////////////
        /////////////////////////////////////////////////////////
        
        //Scene that shows the highscore after game over.
        scoreScene = new Scene(new Group());
        scoreStage = new Stage();
        TextArea txtArea = new TextArea();
        ImageView imw = new ImageView(new Image("file:src/resources/images/GameOver.png"));
        scoreStage.setWidth(500);
        scoreStage.setHeight(500);
        scoreStage.setResizable(false);
        
        double height = 135;
        double width = 300;
        
        final AnchorPane anchorPane = new AnchorPane();
        anchorPane.setTopAnchor(txtArea, 180.0);
        anchorPane.setLeftAnchor(txtArea, 100.0);
        anchorPane.setRightAnchor(txtArea, 100.0);
        anchorPane.setBottomAnchor(txtArea, 70.0);
        
        txtArea.setPrefHeight(height);  //sets height of the TextArea to 400 pixels 
        txtArea.setPrefWidth(width);
        txtArea.setEditable(false);
        txtArea.setWrapText(true);
        txtArea.setStyle("-fx-control-inner-background: #322635; -fx-focus-color: transparent;");
        
        anchorPane.getChildren().addAll(imw, txtArea);
 
        ((Group) scoreScene.getRoot()).getChildren().addAll(anchorPane);
 
        //Promts user for username
        TextInputDialog usernameIn = new TextInputDialog();
        usernameIn.setTitle("AtomicBall");
        usernameIn.setHeaderText("Username: ");
        Optional<String> result = usernameIn.showAndWait();
        String input;
        input = result.get();
                    
        resultSet.setUsername(input);
        while(input.length() < 1){
            result = usernameIn.showAndWait();
        }
        if(input.length() >= 1){
            themeSong.stop();
        }
        
        
        /////////////////////////////////////////////////////////
        ////////////////////Playing the game/////////////////////
        /////////////////////////////////////////////////////////
        
      /**
        * AnimationTimer is what we used to get the ball 'alive'.
        * This timer runs "forever" unless you use .stop();
        * In this timer we call on methods in Ball that applies gravity
        * and constantly checks for collisions with obstacles
        * 
        */
        timer = new AnimationTimer() {
            long lastUpdate = 0 ;
            
            
            @Override
            public void handle(long now) {
                for(Shape s : obstacleArr){ 
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                        return;
                    }

                    long elapsedNanos = now - lastUpdate;
                    double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
                    lastUpdate = now;

                    ball.moveBall(ball, elapsedSeconds, pane);
                    ball.checkCollision(ball, s, resultSet);
                    
                    //Checks if the ball is "dead" and in a spot that should result
                    //in a lost life, and a new ball.
                    if(ball.getCenterY() > 650 && ball.getCenterX() < 400){
                        if(ball.getLives() > 1){
                            stopTimer();
                            ball.setLives(ball.getLives()-1);
                            
                            if(gameOverLatterSound.getStatus() == Status.PLAYING){
                                gameOverLatterSound.stop();
                                gameOverLatterSound.play();
                            }else{
                                gameOverLatterSound.play();
                            }
                            
                            initBall(ball);
                        }else{
                            stopTimer();
                            gameOver(resultSet, txtArea);
                            resetScene(ball);
                        }
                    }
                    
                    //Continually updates scores and lives.
                    scoreText.setText(""+resultSet.getScore());
                    lifeText.setText("Lives remaining: "+ball.getLives());
                    
                }
            }
        };
        
        
        
        
        //Actions
        scene.setOnMousePressed( e -> {
            strengthBar.progressBar(strengthBar);
            
        });
        scene.setOnMouseReleased(e -> {
            strengthBar.progressStop(progressText);
            launchPower = strengthBar.getCurrentHeight();
            ball.setChangeY(- ball.getChangeY() * ball.getElasticity() + (launchPower/30));
        });
        
        
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT){
                leftFlipper.flipTheFlipper(leftFlipper);
                if(flipperSound.getStatus() == Status.PLAYING){
                    flipperSound.stop();
                    flipperSound.play();
                }else{
                    flipperSound.play();
                }
            
            }else if(e.getCode() == KeyCode.RIGHT){
                rightFlipper.flipTheFlipper(rightFlipper);
                if(flipperSound.getStatus() == Status.PLAYING){
                    flipperSound.stop();
                    flipperSound.play();
                }else{
                    flipperSound.play();
                }
            }
        });
        
       
        primaryStage.setTitle("Pinball");
        primaryStage.setScene(scene);
        primaryStage.show();
        initBall(ball);
        timer.start();
        
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
   
  /**
    * Method to allow us to stop time timer while still in the timer.
    * The timer can't directly call on itself while still running.
    * So we had to make this method.
    */
    private void stopTimer (){
        timer.stop();
    }
    
   /**
    * Stops the animation, removes any planned movements 
    * from the ball, and respawns it in the launching "canon".
    */
    private void initBall(Ball ball){
        stopTimer();
        ball.setChangeX(0);
        ball.setChangeY(0);
        ball.respawnBall(470, 720);
        timer.start();
    }
  /**
    * Writes the current players score to a score file, 
    * and then reads every score from the file to determine the top
    * 10 achieved scores. Then clears the score, and displays a new scene
    * which displays the top 10 scores.
    */
    private void gameOver(ScoreCard resultSet, TextArea txtArea){
        if(gameOverSound.getStatus() == MediaPlayer.Status.PLAYING){
            gameOverSound.stop();
            gameOverSound.play();
        }else{
            gameOverSound.play();
        }
        
        resultSet.writeScoreToFile();
        txtArea = resultSet.readScoreFromFile(txtArea);
        scoreStage.setScene(scoreScene);
        scoreStage.show();
        resultSet.resetScore();
        
    }
    
  /**
    * Initializes the ball, and sets ball lives to 5 again.
    */
    private void resetScene(Ball ball){
        initBall(ball);
        ball.setLives(5);
        
    }
    
    
}
