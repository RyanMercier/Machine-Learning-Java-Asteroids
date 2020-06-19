import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.ArrayList;
import java.lang.Math;
import java.io.*;

public class Main implements Serializable
{  
   int myIndex;
   Generation gen;
   
   boolean replay = false;
   
   double fitness;
   
   //Initialize Window
   public MyFrame myFrame;
   public int frameWidth = 800;
   public int frameHeight = 800;
   
   //Initialize Game and Neural Network
   public SpaceShip ship = new SpaceShip(this, frameWidth / 2, frameHeight / 2);
   public boolean isRunning = false;
   public NeuralNetwork myBrain;
   
   long startTime = System.currentTimeMillis() / 1000;
   public int deltaTime;
   
   public int score = 0;
   public int totalShots = 0;
   public int shotHits = 0;
   
   //Initialize Asteroids   
   public ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
   public int maxAsteroids = 10;
   public final int asteroidUpdateTime = 100; //how many milliseconds between checking if another asteroid is neccessary
   
   public final double minAsteroidSpeed = 0.05;
   public final double maxAsteroidSpeed = 0.2;
   
   //Action Variables
   public boolean UP = false;
   public boolean LEFT = false;
   public boolean RIGHT = false;
   public boolean SHOOT = false;
   
   //Ray Lengths
   public int north = frameHeight / 2;
   public int northEast = frameWidth;
   public int east = frameWidth / 2;
   public int southEast = frameWidth;
   public int south = frameHeight / 2;
   public int southWest = frameWidth;
   public int west = frameWidth / 2;
   public int northWest = frameWidth ;
   
   public boolean drawGizmos = false;

   public Main()
   {
      //for net loading
   }
   
   public Main(Generation gen, int index)
   {  
      this.gen = gen;
      myIndex = index;
      myBrain = new NeuralNetwork(this);      
      MyGraphics g = new MyGraphics(this);
      myFrame = new MyFrame(g, frameWidth, frameHeight, gen.genIndex);
      isRunning = true;
      Timer timer = new Timer();
      timer.schedule(new GameUpdate(this, timer, g), 0, 1000 / 30); //new timer at 30 fps
   }
   
   public Main(int index, NeuralNetwork net) //Load NeuralNet
   {  
      this.gen = gen;
      myIndex = index;
      myBrain = net;      
      MyGraphics g = new MyGraphics(this);
      myFrame = new MyFrame(g, frameWidth, frameHeight, gen.genIndex);
      isRunning = true;
      Timer timer = new Timer();
      timer.schedule(new GameUpdate(this, timer, g), 0, 1000 / 30); //new timer at 30 fps
   }
   
   public void init(int index, Generation gen, NeuralNetwork n)
   {
      this.gen = gen;
      myIndex = index;
      myBrain = n;      
      MyGraphics g = new MyGraphics(this);
      myFrame = new MyFrame(g, frameWidth, frameHeight, gen.genIndex);
      isRunning = true;
      Timer timer = new Timer();
      timer.schedule(new GameUpdate(this, timer, g), 0, 1000 / 30); //new timer at 30 fps
   }
   
   public void stopUpdate(long endTime)
   { 
      fitness = myBrain.calcFitness(startTime - endTime);
      isRunning = false;
      myFrame.setVisible(false);
      gen.updatePlayerState();
   }
   
   public int randomInt(int min, int max)
   {
      return (int)(Math.random()*(max - min + 1)) + min;
   }
   
   public double randomDouble(double min, double max)
   {
      return (double)(Math.random()*(max-min))+min;
   }
   
   public void addShot()
   {
      totalShots++;
   }
}