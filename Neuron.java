import java.io.*;
import java.util.Random;

public class Neuron implements Serializable
{
   public double[] inWeights;
   double bias;
   double output;
   
   public Neuron(double[] inWeights, double bias)
   {
      this.inWeights = inWeights;
      this.bias = bias;
   }
      
   public void act(double[] inputs)
   {
      double result = 0;
      
      for(int i = 0; i < inputs.length; i++)
      {
         result += inputs[i] * this.inWeights[i];
      }
      
      result += this.bias;
      result = tanh(result);
      this.output = result;
   }
   
   public void mutate()
   {
      Random r = new Random();
      
      for(int i = 0; i < inWeights.length; i++)
      {
         inWeights[i] += r.nextGaussian();
      }
      
      bias += r.nextGaussian();
   }
   
   public double sigmoid(double num)
   {
      return (1 / (1 + Math.pow(Math.E, (-1 * num))));
   }
   
   public double tanh(double num)
   {
      return 2 * sigmoid(2 * num) - 1;
   }
}