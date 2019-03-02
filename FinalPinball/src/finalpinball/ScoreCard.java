/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpinball;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.scene.control.TextArea;

/**
 *
 * @author Nataniel Haraldseth
 */
 public class ScoreCard implements Comparable<ScoreCard> {
     
    private int score;
    private double multiplier;
    private int rank = 1;
    private String username;
    private static final File FILE = new File("scores.txt"); 

/*=============================================================================*/ 
   
     /**
     * Private constructor for internal use in class only.
     * 
     * @param score in order to keep track of current score.
     * 
     * @param takes in username to keep for further use.
     * 
     */
    private ScoreCard(int score, String username) {
        this.score = score;
        this.username = username;
    }
    
    public ScoreCard() {}

/*=============================================================================*/

    public int getScore() {
        return score;
    }
    
    public double getMultiplier() {
        return multiplier;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }
    
/*=============================================================================*/ 

    public void addScore(int score) {
        this.score += score;
    }
    
    public void resetScore(){
        this.score = 0;
    }
    
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
/*=============================================================================*/
    
    /**
     * 
     * Method for reading score from file, returns textarea for usage where needed.
     * Sorts users based on scores accompanied by rank.
     * Utilizes private constructor for Score object creation.
     * 
     * @param TextArea takes a txtArea in for processing and adding elements to textarea. 
     * 
     */    
    public TextArea readScoreFromFile(TextArea txtArea) {
        ArrayList<ScoreCard> list = new ArrayList();
        String line;
        int listScore = 0;
        String listUsername = "";
        ScoreCard scoreCard;
        int c = 0;
        
        try(Scanner reader = new Scanner(FILE)) {
            while(reader.hasNextLine() && c < 10) {
                line = reader.nextLine();
                String[] items = line.split("-");
                listUsername = items[0];
                listScore = parseInt(items[1]);
                scoreCard = new ScoreCard(listScore, listUsername);
                list.add(scoreCard);
            }
            
            Collections.sort(list);
            for (ScoreCard scoreItems : list) {
                c++;
                if (c <= 10) {
                    txtArea.appendText(rank++ + "       " +  scoreItems.getScore() + "             " + scoreItems.getUsername() + "\n");
                }
            }
            
        }catch(FileNotFoundException ex) {
            System.out.println("File: " + FILE + " does not exist");
        }
        return txtArea;
    } 
    
    /**
     * 
     * Writes score to file for storage, call upon method at end of score
     * gathering for proper storage. Creates file on first use.
     * 
     */
    public void writeScoreToFile() {
        try(PrintWriter output = new PrintWriter(new FileWriter(FILE, true))){
            output.println(username + "-" + getScore());
        }catch(FileNotFoundException ex) {
            System.out.println("File: " + FILE + " does not exist");
        }catch(IOException ex) {
            System.out.println("Error!");
        }
    }
    
    /**
     * Implements compareTo method for object comparison and proper sorting of objects.
     */
    @Override
    public int compareTo(ScoreCard compareScoreCard) {
        int compareScore = ((ScoreCard) compareScoreCard).getScore(); 
		
        //ascending order
        //return this.score - compareScore;

        //descending order
        return compareScore - this.score;
    }
    
    
}//END OF CLASS