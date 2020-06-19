import java.util.Timer;
import java.lang.Math;
import java.util.*;

public class GameUpdate extends java.util.TimerTask
{
    MyGraphics g;
    Timer myTimer;
    Main main;
    SpaceShip ship;
    
    private int lastDiffStep = 0;
    
    //timing
    long lastTime = System.nanoTime();
    long lastShot = System.currentTimeMillis();
    long lastAsteroid = System.currentTimeMillis();
    
    final int rayResolution = 5;
    
    public GameUpdate(Main main, Timer myTimer, MyGraphics g)
    {
      this.myTimer = myTimer;
      this.g = g;
      this.main = main;
      this.ship = main.ship;
    }
    
    public void run() //loop
    {
        //calculate delta time
        long time = System.nanoTime();
        int deltaTime = (int) ((time - lastTime) / 1000000);
        main.deltaTime = deltaTime;
        lastTime = time;
        
        Update(deltaTime);
        main.myBrain.act();
        
        g.render();

        if (!main.isRunning)
        {
            myTimer.cancel();
        }
    }
    
    private void Update(int deltaTime)
    {
         main.myFrame.setScore(main.score);
         
         //------------------------Difficulty-Control------------------------//
         if(main.score / 10 > lastDiffStep)
         {
            main.maxAsteroids++;
            lastDiffStep++;
         }
         
         //---------------------------Ship-Control---------------------------//
         if(main.LEFT)
         {
            ship.incrementFaceAngle(-10);
         }
         
         if(main.RIGHT)
         {
            ship.incrementFaceAngle(10);
         }
         
         if(main.UP)
         {
            
            ship.moveAngle = ship.faceAngle;
            
            if(ship.currentSpeed <= 1.0)
            {
               ship.currentSpeed += 0.02;
            }
         }
         
         else if(ship.currentSpeed >= 0)
         {
            ship.currentSpeed -= 0.02;
         }
         
         if(ship.currentSpeed > 0)
         {
            ship.xPos += ship.BASE_SPEED * ship.currentSpeed * Math.cos(Math.toRadians(ship.moveAngle)) * deltaTime;
            ship.yPos += ship.BASE_SPEED * ship.currentSpeed * Math.sin(Math.toRadians(ship.moveAngle)) * deltaTime;
         }
         
         //wrap screen
         if(ship.xPos < 0)
         {
            ship.xPos = main.frameWidth;
         }
         
         if(ship.xPos > main.frameWidth)
         {
            ship.xPos = 0;
         }
         
         if(ship.yPos < 0)
         {
            ship.yPos = main.frameHeight;
         }
         
         if(ship.yPos > main.frameHeight)
         {
            ship.yPos = 0;
         }
         
         //Ship-Asteroid Collission
          for(int j = 0; j < main.asteroids.size(); j++)
          {
             int x1 = main.asteroids.get(j).x;
             int x2 = ship.xPos;
             int y1 = main.asteroids.get(j).y;
             int y2 = ship.yPos;
             double rSqr = (main.asteroids.get(j).radius + 15) * (main.asteroids.get(j).radius + 15);
                  
             if((double)((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) <= rSqr)
             {
               main.asteroids.get(j).takeDamage();
               main.myFrame.setTitle("GAME OVER   SCORE: " + main.score);
               ship.alive = false;
               main.stopUpdate(System.currentTimeMillis() / 1000);
             }
          }
         
         //--------------------------Bullet-Control--------------------------//
         
         if(main.SHOOT)
         {
            long currentTime = System.currentTimeMillis();
      
            if((int)(currentTime - lastShot) >= ship.fireRate)
            {
               ship.shoot();  
               lastShot = System.currentTimeMillis();
            }
         }
         
         if(ship.bullets.size() > 0)
         {
            for(int i = 0; i < ship.bullets.size(); i++)
            {
               ship.bullets.get(i).move(deltaTime);
               
               //Asteroid-Bullet Collsion
               for(int j = 0; j < main.asteroids.size(); j++)
               {
                  int x1 = main.asteroids.get(j).x;
                  int x2 = ship.bullets.get(i).x;
                  int y1 = main.asteroids.get(j).y;
                  int y2 = ship.bullets.get(i).y;
                  double rSqr = main.asteroids.get(j).radius * main.asteroids.get(j).radius;
                  
                  if((double)((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) <= rSqr)
                  {
                     main.asteroids.get(j).takeDamage();
                     ship.bullets.get(i).alive = false;
                     main.score++;
                     main.shotHits++;
                     main.myFrame.setTitle("Java Asteroids (Ryan Mercier)   SCORE: " + main.score + "    Generation: " + main.gen.genIndex);
                  }
               }
               
               if(ship.bullets.get(i).x < 0 || ship.bullets.get(i).x > main.frameWidth || ship.bullets.get(i).y < 0 || ship.bullets.get(i).y > main.frameHeight || ship.bullets.get(i).alive == false)
               {
                  ship.bullets.remove(i);
                  i--;
               }
            }
         }
         
         //-------------------------Asteroid-Control-------------------------//
         
         if(main.asteroids.size() > 0)
         {
            for(int i = 0; i < main.asteroids.size(); i++)
            {
               if(main.asteroids.get(i).alive)
               {
                  main.asteroids.get(i).move(deltaTime);
               }
               
               else
               {
                  main.asteroids.remove(i);
                  i--;
               }
            }
         }
         
         if(main.asteroids.size() < main.maxAsteroids)
         {
            long currentTime = System.currentTimeMillis();
      
            if((int)(currentTime - lastAsteroid) >= main.asteroidUpdateTime)
            {
               main.asteroids.add(new Asteroid(main, main.randomInt(0, main.frameWidth), main.randomInt(0, main.frameHeight), main.randomInt(1, 3), main.randomDouble(main.minAsteroidSpeed, main.maxAsteroidSpeed), main.randomDouble(0, 180), false));  
               lastAsteroid = System.currentTimeMillis();
            }
         }
         
         //--------------------------RayCast-Control-------------------------//
         
         main.north = Ray(ship.xPos, ship.yPos, 90, 0, -rayResolution);
         main.northEast = Ray(ship.xPos, ship.yPos, 45, rayResolution, -rayResolution);
         main.east = Ray(ship.xPos, ship.yPos, 0, rayResolution, 0);
         main.southEast = Ray(ship.xPos, ship.yPos, 315, rayResolution, -rayResolution);
         main.south = Ray(ship.xPos, ship.yPos, 270, 0, -rayResolution);
         main.southWest = Ray(ship.xPos, ship.yPos, 225, rayResolution, -rayResolution);
         main.west = Ray(ship.xPos, ship.yPos, 180, rayResolution, 0);
         main.northWest = Ray(ship.xPos, ship.yPos, 135, rayResolution, -rayResolution);
    }
    
    public int Ray(int x, int y, double angle, int xResolution, int yResolution)
    {      
      double currentX = x;
      double currentY = y;
      int step = 0;
      
      while(currentX > 0 && currentX < main.frameWidth && currentY > 0 && currentY < main.frameHeight)
      {
         currentX += xResolution * Math.cos(Math.toRadians(angle));
         currentY += yResolution * Math.sin(Math.toRadians(angle));
         
         step++;
         
         for(int j = 0; j < main.asteroids.size(); j++)
         {
            double x1 = main.asteroids.get(j).x;
            double x2 = currentX;
            double y1 = main.asteroids.get(j).y;
            double y2 = currentY;
            double rSqr = main.asteroids.get(j).radius * main.asteroids.get(j).radius;
                     
            //if collission
            if((double)((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) <= rSqr)
            {
               return step * rayResolution; //(int)Math.sqrt((currentX - ship.xPos) * (currentX - ship.xPos) + (currentY - ship.yPos) * (currentY - ship.yPos));
            }
         }
      }
      
      return main.frameWidth;
   }
}