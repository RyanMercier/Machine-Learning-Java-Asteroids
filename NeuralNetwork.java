import java.lang.Math;

public class NeuralNetwork
{
   public Main main;
   Neuron[] hiddenLayer = new Neuron[9];
   Neuron[] hiddenLayer2 = new Neuron[9];
   Neuron[] outputLayer = new Neuron[4];
   public NetSaveData save;
   
   double fitness;
   
   //INPUTS                //Outputs
   //ship position         //Forward
   //ship rotation         //Rotate Left
   //North                 //Rotate Right
   //North East            //Shoot
   //East
   //South East
   //South
   //South West
   //West
   //North West
   
   public NeuralNetwork(Main main)
   {
      this.main = main;
      initNeurons();
   }
   
   public NeuralNetwork(Main main, Neuron[] h, Neuron[] h2, Neuron[] o)
   {
      this.main = main;
      this.hiddenLayer = h;
      this.hiddenLayer2 = h2;
      this.outputLayer = o;
   }
   
   public NetSaveData saveNet()
   {
      return new NetSaveData(hiddenLayer, hiddenLayer2, outputLayer);
   }
   
   public void initNeurons()
   {  
      for(int i = 0; i < 9; i++)
      {
         double[] inputWeights = new double[] {randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight()};
         double inputBias = randomBias();
         hiddenLayer[i] = new Neuron(inputWeights, inputBias);
      }
      
      for(int i = 0; i < 9; i++)
      {
         double[] inputWeights = new double[] {randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight()};
         double inputBias = randomBias();
         hiddenLayer2[i] = new Neuron(inputWeights, inputBias);
      }
      
      for(int i = 0; i < 4; i++)
      {
         double[] outputWeights = new double[] {randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight(), randomWeight()};
         double outputBias = randomBias();
         outputLayer[i] = new Neuron(outputWeights, outputBias);
      }
   }
   
   public void act()
   {  
      double[] inputs = new double[] {main.ship.faceAngle, main.north, main.northEast, main.east, main.southEast, main.south, main.southWest, main.west, main.northWest};
      //System.out.println(main.ship.faceAngle + ", " + main.north + ", " + main.northEast + ", " + main.east + ", " + main.southEast + ", " + main.south + ", " + main.southWest + ", " + main.west + ", " + main.northWest);
      
      double[] hiddenLayerResults = new double[9];
      
      for(int i = 0; i < 9; i++)
      {
         hiddenLayer[i].act(inputs);
         hiddenLayerResults[i] = hiddenLayer[i].output;
      }
      
      double[] hiddenLayer2Results = new double[9];
      
      for(int i = 0; i < 9; i++)
      {
         hiddenLayer2[i].act(hiddenLayerResults);
         hiddenLayer2Results[i] = hiddenLayer2[i].output;
      }
      
      double[] decisions = new double[4];
      
      for(int i = 0; i < 4; i++)
      {
         outputLayer[i].act(hiddenLayer2Results);
         decisions[i] = outputLayer[i].output;
      }
      
      //Execute the Decision
      
      if(decisions[0] > 0)
      {
         main.UP = true;
      }
      else
      {
         main.UP = false;
      }
      
      if(decisions[1] > 0)
      {
         main.LEFT = true;
      }
      else
      {
         main.LEFT = false;
      }
      
      if(decisions[2] > 0)
      {
         main.RIGHT = true;
      }
      else
      {
         main.RIGHT = false;
      }
      
      if(decisions[3] > 0)
      {
         main.SHOOT = true;
      }
      else
      {
         main.SHOOT = false;
      }
      
      //System.out.println(decisions[0] + ", " + decisions[1] + ", " + decisions[2] + ", " + decisions[3]);

   }
   
   public void mutate()
   {
      for(int i = 0; i < randomInt(1, 10); i++)
      {
         hiddenLayer[randomInt(0, 8)].mutate();
      }
      
      for(int i = 0; i < randomInt(1, 10); i++)
      {
         hiddenLayer2[randomInt(0, 8)].mutate();
      }
      
      for(int i = 0; i < randomInt(1, 3); i++)
      {
         outputLayer[randomInt(0, 3)].mutate();
      }
   }
   
   public void Cross(NeuralNetwork other)
   {
      for(int i = 0; i < hiddenLayer.length; i++)
      {
         int choice = randomInt(0, 1);
         
         if(choice == 1)
         {
            this.hiddenLayer[i] = other.hiddenLayer[i];
         }
      }
      
      for(int i = 0; i < hiddenLayer2.length; i++)
      {
         int choice = randomInt(0, 1);
         
         if(choice == 1)
         {
            this.hiddenLayer2[i] = other.hiddenLayer2[i];
         }
      }
      
      for(int i = 0; i < outputLayer.length; i++)
      {
         int choice = randomInt(0, 1);
         
         if(choice == 1)
         {
            this.outputLayer[i] = other.outputLayer[i];
         }
      }
   }
      
   public double randomWeight()
   {
      return (double)(Math.random() * 2) -1;
   }
   
   public double randomBias()
   {
      return (double)(Math.random() * 2) -1;
   }
   
   public int randomInt(int min, int max)
   {
      return (int)(Math.random()*(max - min + 1)) + min;
   }
   
   public double calcFitness(long lifeSpan)
   {
      double result = 0;
      result += (main.score + 1) * 10;
      result += (main.score + 1) * (((double)main.shotHits + 1) / ((double)main.totalShots + 1)) / 10;
      result += lifeSpan * 2;
      result /= 100;
      return result;
   }
}