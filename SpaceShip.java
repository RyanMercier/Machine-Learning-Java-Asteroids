import java.lang.Math;
import java.util.ArrayList;
import java.awt.Polygon;

public class SpaceShip
{
   Main main;
   
   boolean alive = true;
   
   int xPos = 0;
   int yPos = 0;
   
   final double BASE_SPEED = 0.3;
   double currentSpeed = 0.0;
   
   double faceAngle = 270.0;
   double moveAngle = faceAngle;
   
   Triangle body = new Triangle();
   //Triangle Points
   //(10, 0) (-10, -10) (-10, 10)
   
   Polygon shipPolygon;
   
   final int fireRate = 230; //Higher = lower fireRate
   ArrayList<Bullet> bullets = new ArrayList<Bullet>();
   
   public SpaceShip(Main main)
   {
      this.main = main;
      xPos = 300;
      yPos = 300;
   }
   
   public SpaceShip(Main main, int _xPos, int _yPos)
   {
      this.main = main;
      xPos = _xPos;
      yPos = _yPos;
   }
   
   public void incrementFaceAngle(double deltaF)
   {
      faceAngle += deltaF;
      if(faceAngle > 360){faceAngle -= 360;}
      else if(faceAngle < 0){faceAngle += 360;}
   }
   
   public void shoot()
   {
      bullets.add(new Bullet(xPos, yPos, faceAngle));
      main.addShot();
   }
   
}