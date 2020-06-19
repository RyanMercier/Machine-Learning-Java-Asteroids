import java.io.*;

public class Generation
{
   public int genSize = 20;
   public int genIndex;
   
   public int[] topFive;
   
   public Main[] players;
   public NetSaveData[] saveData;
   
   private int doneCount = 0;
   public boolean genFinished = false;
   
   private double[] playerFitness;
   
   public Generation(int index)
   {
      //For training
      this.genIndex = index;
      playerFitness = new double[genSize];
   }
   
   public Generation(int index, int genSize)
   {
      this.genIndex = index;
      this.genSize = genSize;
      
      players = new Main[genSize];
      saveData = new NetSaveData[genSize];
      
      for(int i = 0; i < genSize; i++)
      {
         this.players[i] = new Main(this, i);
         saveData[i] = this.players[i].myBrain.saveNet();
      }
      
      playerFitness = new double[genSize];
   }
   
   public Generation(int index, Main[] p)
   {
      this.genIndex = index;
      this.genSize = p.length;
      
      this.players = p;
      
      saveData = new NetSaveData[genSize];
      for(int i = 0; i < genSize; i++)
      {
         saveData[i] = this.players[i].myBrain.saveNet();
      }
      
      playerFitness = new double[genSize];
   }

   public void init(int index, int genSize, Main[] p)
   {
      this.genIndex = index;
      this.genSize = genSize;
      this.players = p;
      
      saveData = new NetSaveData[genSize];
      for(int i = 0; i < genSize; i++)
      {
         saveData[i] = this.players[i].myBrain.saveNet();
      }
      
      playerFitness = new double[genSize];
   }
   
   public void updatePlayerState()
   {
      if(this.allDone())
      {
         updatePlayerFitness();
         
         for(int i = 0; i < genSize; i++)
         {
            this.players[i].myFrame.dispose();
         }
         
         genFinished = true;
      }
      
      if(genFinished == true)
      {
         Manager.nextGeneration(topFive);
      }
   }
   
   private boolean allDone()
   {
      for(int i = 0; i < players.length; i++)
      {
         if(this.players[i].myFrame.isShowing() == true || this.players[i].isRunning == true)
         {
            return false;
         }
      }
      
      return true;
   }
   
   private void updatePlayerFitness()
   {
      this.topFive = new int[5];
      
      for(int i = 0; i < genSize; i++)
      {
         playerFitness[i] = this.players[i].fitness;
         //System.out.println(this.players[i].fitness);
      }
      
      int[] topIndex = selectSort(playerFitness);
         
      for(int i = 0; i < 5; i++)
      {
         this.topFive[i] = topIndex[i];
         System.out.println(players[topIndex[i]].fitness);
      }

   }
   
   private int[] selectSort(double[] a)
   {
      int[] result = new int[a.length];
      
      for(int i = 0; i < a.length; i++)
      {
         result[i] = i;
      }
   
      for(int i = 0; i < a.length - 1; i++)
      {
         for(int j = i + 1; j < a.length; j++)
         {
         
            if(a[i] < a[j])
            {
               double temp = a[i];
               a[i] = a[j];
               a[j] = temp;
               
               int tempI = result[i];
               result[i] = result[j];
               result[j] = tempI;
            }
         }
      }
      
      return result;
   }
   
   public void saveGen()
   {
      try(FileOutputStream f = new FileOutputStream("Generation" + genIndex + ".txt");
      
      ObjectOutput s = new ObjectOutputStream(f))
      {
         System.out.println("Writing File...");
         s.writeObject(saveData);
         System.out.println("File \"Generation" + genIndex + ".txt\" Written");
      }
      
      catch(Exception e)
      {
         System.out.println("Error writing File");
         System.out.println(e);
      }
   }
}