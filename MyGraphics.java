import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.Polygon;
import java.awt.geom.Area;
import javax.swing.*;
import java.lang.Math;
import java.util.ArrayList;

public class MyGraphics extends JPanel
{
   private Graphics2D g2D;
   Main main;
   SpaceShip ship;
   
   public MyGraphics(Main main)
   {
      this.main = main;
      ship = main.ship;
   }
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      setBackground(Color.BLACK);
      g2D = (Graphics2D)g;
      g2D.setColor(Color.WHITE);
      
      drawShip();
      drawBullets();
      drawAsteroids();
      
      if(main.drawGizmos)
      {
         drawGizmos();
      }
      
      g2D.dispose();
   }
   
   public void drawShip()
   {
      //Triangle Points
      //(0, -10) (-10, 10) (10, 10)
      
      AffineTransform at = new AffineTransform();
      Dimension size = ship.body.getTriangleSize();
      at.translate(ship.xPos, ship.yPos);
      at.rotate(Math.toRadians(ship.faceAngle));
      g2D.setTransform(at);
      g2D.drawPolygon(ship.body.xCoords, ship.body.yCoords, 3);
   }
   
   public void drawBullets()
   {
      //Create Bullet
      for(int i = 0; i < ship.bullets.size(); i++)
         {
            AffineTransform at = new AffineTransform();
            at.translate(ship.bullets.get(i).x, ship.bullets.get(i).y);
            g2D.setTransform(at);
            g2D.fill(new Ellipse2D.Double(0, 0, 3, 3));
         }
   }
   
   public void drawAsteroids()
   {
      //Create Asteroid
      for(int i = 0; i < main.asteroids.size(); i++)
         {
            AffineTransform at = new AffineTransform();
            at.translate(main.asteroids.get(i).x, main.asteroids.get(i).y);
            g2D.setTransform(at);
            g2D.drawPolygon(main.asteroids.get(i).xCoords, main.asteroids.get(i).yCoords, 12);
         }
         
   }
   
   private void drawGizmos()
   {
      //main Directions
      g2D.setColor(Color.GREEN);
      
      AffineTransform nt = new AffineTransform();
      nt.translate(ship.xPos, ship.yPos - main.north);
      g2D.setTransform(nt);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      AffineTransform et = new AffineTransform();
      et.translate(ship.xPos + main.east, ship.yPos);
      g2D.setTransform(et);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      AffineTransform st = new AffineTransform();
      st.translate(ship.xPos, ship.yPos + main.south);
      g2D.setTransform(st);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      AffineTransform wt = new AffineTransform();
      wt.translate(ship.xPos - main.west, ship.yPos);
      g2D.setTransform(wt);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      //Sub Directions
      g2D.setColor(Color.BLUE);
      
      AffineTransform net = new AffineTransform();
      net.translate(ship.xPos + Math.cos(Math.PI / 4) * main.northEast, ship.yPos - Math.sin(Math.PI / 4) * main.northEast);
      g2D.setTransform(net);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      g2D.setColor(Color.RED);
      
      AffineTransform set = new AffineTransform();
      set.translate(ship.xPos + Math.cos(7 * Math.PI / 4) * main.southEast, ship.yPos - Math.sin(7 * Math.PI / 4) * main.southEast);
      g2D.setTransform(set);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      g2D.setColor(Color.YELLOW);
      
      AffineTransform swt = new AffineTransform();
      swt.translate(ship.xPos + Math.cos(5 * Math.PI / 4) * main.southWest, ship.yPos - Math.sin(5 * Math.PI / 4) * main.southWest);
      g2D.setTransform(swt);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));
      
      g2D.setColor(Color.ORANGE);
      
      AffineTransform nwt = new AffineTransform();
      nwt.translate(ship.xPos + Math.cos(3 * Math.PI / 4) * main.northWest, ship.yPos - Math.sin(3 * Math.PI / 4) * main.northWest);
      g2D.setTransform(nwt);
      g2D.fill(new Ellipse2D.Double(0, 0, 10, 10));

   }
   
   public void render()
   {
      repaint();
   }
   
}