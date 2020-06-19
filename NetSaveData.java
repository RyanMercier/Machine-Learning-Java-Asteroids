import java.io.*;

public class NetSaveData implements Serializable
{
   Neuron[] hiddenLayer;
   Neuron[] hiddenLayer2;
   Neuron[] outputLayer;
   
   public NetSaveData(Neuron[] h, Neuron[] h2, Neuron[] o)
   {
      hiddenLayer = h;
      hiddenLayer2 = h2;
      outputLayer = o;
   }
}