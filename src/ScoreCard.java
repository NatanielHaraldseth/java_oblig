/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ballfysx_02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Nataniel Haraldseth
 */
 public class ScoreCard {
     
    private int score = 1000000;
    private double multiplier;
    private int rank = 1;
    private String username = "Inversebeast"; //Placeholder
    //Need player class completed in order to get playername
    //and append name to score --> file 

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
        this.score = score;
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
    
    //Read from score.txt and sort the top 10 based on score 
    
    //Add write score to file
    public void writeScoreToFile() {
        File file = new File("scores.txt");
        try(PrintWriter output = new PrintWriter(new FileWriter(file, true))){
            output.println(" " + rank + "      " + username + "          " + score);
        }catch(FileNotFoundException ex) {
            System.out.println("File: " + file + " does not exist");
        }catch(IOException ex) {
            System.out.println("File: " + file + " does not exist");
        }
    }
    
    //Multiplies the score
    public void addMultipliedScore(int score, double multiplier) {
        this.score = score * (int)multiplier;
    }
}//END OF CLASS
