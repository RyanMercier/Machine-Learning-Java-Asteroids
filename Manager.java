import java.io.*;
import java.util.Scanner;

public class Manager
{
   public static int genCount = 0;
   public static final int genSize = 20;
   public static Generation currentGen;

   
   public static void main(String[] args)
   {
      System.out.println("Machine Learning Asteroids Ryan Mercier");
      System.out.println("enter \"t\" to start training");
      System.out.println("enter \"p\" GenerationIndex to load saved generation");
            
      Scanner scan = new Scanner(System.in);
      
      String input = scan.nextLine();
      
      if(input.charAt(0) == 't')
      {
         currentGen = new Generation(genCount, genSize);
         currentGen.saveGen();
      }
      
      else if(input.charAt(0) == 'p')
      {
         playGeneration(Integer.parseInt(input.substring(2)));
      }
   }
   
   public static NetSaveData[] loadGeneration(int genIndex)
   {
      NetSaveData[] data = new NetSaveData[5];
      
      try(FileInputStream in = new FileInputStream("./Generation" + genIndex + ".txt");
      ObjectInputStream s = new ObjectInputStream(in))
      {
         data = (NetSaveData[]) s.readObject();
         System.out.println("Reading File...");
         return data;
      }
      
      catch(Exception e)
      {
         System.out.println("File not Found");
         System.out.println(e);
      }
      
      return null;
   }
   
   public static void playGeneration(int genIndex)
   {
      NetSaveData[] data = loadGeneration(genIndex);
      
      Main[] players = new Main[genSize];
      currentGen = new Generation(genIndex);
      
      for(int i = 0; i < data.length; i++)
      {
         players[i] = new Main();
         players[i].init(i, currentGen, new NeuralNetwork(players[i], data[i].hiddenLayer, data[i].hiddenLayer2, data[i].outputLayer));
         
      }
      
      currentGen.init(genCount, genSize, players);
   }
   
   public static void nextGeneration(int[] topFive)
   {
      if(currentGen.genFinished)
      {
         NetSaveData[] data = loadGeneration(genCount);
      
         genCount++;
         Main[] players = new Main[genSize]; //Currently Set Up For A Gen Size of 20
         currentGen = new Generation(genCount);
         
         for(int i = 0; i < 15; i++)
         {
            //Top Five Brains of Last Generation
            int index = i % 5;
            players[i] = new Main();
            players[i].init(i, currentGen, new NeuralNetwork(players[i], data[topFive[index]].hiddenLayer, data[topFive[index]].hiddenLayer2, data[topFive[index]].outputLayer));
         }
         
         for(int i = 5; i < 10; i++)
         {
            //Asexual Mutation of top five
            players[i].myBrain.mutate();
         }
         
         for(int i = 10; i < 15; i++)
         {
            players[i].myBrain.Cross(players[randomInt(1, 10)].myBrain);
         }
         
         for(int i = 15; i < 20; i++)
         {
            //Five New Random Brains
            players[i] = new Main();
            players[i].init(i, currentGen, new NeuralNetwork(players[i]));
         }
         
         currentGen.init(genCount, genSize, players);
         currentGen.saveGen();
      }
   }
   
   public static int randomInt(int min, int max)
   {
      return (int)(Math.random()*(max - min + 1)) + min;
   }
}