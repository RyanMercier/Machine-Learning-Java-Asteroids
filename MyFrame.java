import javax.swing.*;
public class MyFrame extends JFrame
{     
   public int score;
   public int genIndex;
   
   public MyFrame(MyGraphics graphic, int width, int height, int genIndex)
   {
      this.setSize(width, height);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      super.setTitle("Java Asteroids (Ryan Mercier)   SCORE: " + score + "    Generation: " + genIndex);
      this.add(graphic);
      this.setVisible(true);
   }
   
   public void setScore(int score)
   {
      this.score = score;
   }
}