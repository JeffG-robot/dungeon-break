/**
 * GameMain
 * This Program creates a game to play
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Scanner;
import java.awt.MouseInfo;
import java.io.*; 
import java.awt.image.*;
import javax.imageio.*;

class GameMain extends JPanel  {
  
  static int timePast;         //check how much time had past before the next loop
  BufferedImage[] enemysheet; 
  BufferedImage[] menuPicture;
  BufferedImage sheet;
  BufferedImage bosssheet ;
  final int imagewidth = 32;
  final int imageheight = 32;
  final int rows = 2;
  final int cols = 4;
  static boolean game;          //if false, the game will end
  String readMap;               //read the next thing in the mapbox.txt
  Scanner mapObject;            //scanner for the mapbox
  double lastX, lastY;          //record the last x so the player return to the last position when he/she hits a wall         
  boolean  painting;            //if it is painting now
  Clock time;                   //clock see the time for the main loop   
  int enemyType;                //see what is the enemy type
  int stageNumber;              //what is the stage level
  Wall stairs;                  //stairs
  double radiox;                //radio for x
  double radioy;                //radio for y
  static double point;          //point for the player
  Boss boss;                    //a boss
  BufferedImage[] menu;
  BufferedImage[] sprites;
  int currentSprite ;
  int timerForThePicture ;      //timer for the picture 
  int spriteface ;
  int currentbossSprite ;
  int bosstimer ;
  int bossface ;
  BufferedImage[][] enemysprites; 
  BufferedImage[] bosssprites;
  
  Boolean collectionRecord;       //check if the player collides with walls
  
  //constructor
  GameMain() {
    
    //set all the variables
    stageNumber=-5;
    radiox=DungeonBreak.mapSizeX/88;
    radioy=DungeonBreak.mapSizeY/88;
    DungeonBreak.player=new Player(0, 0, 10,  1,  2, 5, 100, 5,3);
    point=1000;
    time=new Clock();
    int currentSprite = 0;
    timerForThePicture = 0;
    spriteface = 0;
    currentbossSprite = 0;
    bosstimer = 0;
    bossface = 0;
    painting=false;
    game=true;
    
    try {
      
      enemysheet = new BufferedImage[5];
      menuPicture = new BufferedImage[5];
      sheet = ImageIO.read(new File("drowsprite.png"));
      menuPicture[0]= ImageIO.read(new File("homepage.png"));
      menuPicture[1]= ImageIO.read(new File("help1.png"));
      menuPicture[2]= ImageIO.read(new File("help2.png"));
      menuPicture[3]= ImageIO.read(new File("help3.png"));
      menuPicture[4]= ImageIO.read(new File("help4.png"));
      enemysheet[0] = ImageIO.read(new File("shotgunenemy2.png"));
      enemysheet[1] = ImageIO.read(new File("rifleenemy.png"));
      enemysheet[2] = ImageIO.read(new File("sniperenemy.png"));
      enemysheet[3] = ImageIO.read(new File("mageenemy.png"));
      enemysheet[4] = ImageIO.read(new File("mage2enemy.png"));
      bosssheet = ImageIO.read(new File("Boss.png"));
      
      
      sprites = new BufferedImage[rows * cols];
      enemysprites = new BufferedImage[5][rows * cols];
      bosssprites = new BufferedImage[rows * cols];
      menu= new BufferedImage[5];
      
      for(int i=0;i<5;i++){
        menu[i]=menuPicture[i];
      }
      for (int j = 0; j < rows; j++)
        for (int i = 0; i < cols; i++)
        sprites[(j * cols) + i] = sheet.getSubimage(i * imagewidth,j * imageheight,imagewidth,imageheight);
      
      for (int spritesheetnum = 0; spritesheetnum < 5; spritesheetnum ++){
        for (int j = 0; j < rows; j++)
          for (int i = 0; i < cols; i++)
          enemysprites[spritesheetnum][(j * cols) + i] = enemysheet[spritesheetnum].getSubimage(i * imagewidth, j * imageheight, imagewidth, imageheight);  
      }
      
      for (int j = 0; j < rows; j++)
        for (int i = 0; i < cols; i++)
        bosssprites[(j * cols) + i] = bosssheet.getSubimage(i * 128, j * 128, 128, 128); 
      
    } catch(Exception e) { System.out.println("error loading sheet");};
    
  }
  
  
  
  /**
   * paintComponent
   * this method overloads the paintComponent in java
   */
  public void paintComponent(Graphics g) {
    //print 
    super.paintComponent(g); //required
    if(stageNumber>0){
      g.setColor(new Color(150, 150,150));//sets background color
      g.fillRect(0,0,DungeonBreak.maxx,DungeonBreak.maxy);//draws background
      //print
      g.setColor(new Color(0, 0,0));//sets borderline colors, then draws map borders
      g.drawLine(1-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,                     1-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,                      1-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,     DungeonBreak.mapSizeY-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2);
      //print
      g.drawLine(1-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,                     1-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,                           DungeonBreak.mapSizeX-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,     1-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2);
      //print
      g.drawLine(DungeonBreak.mapSizeX-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,    1-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,                  DungeonBreak.mapSizeX-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,     DungeonBreak.mapSizeY-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2);
      //print
      g.drawLine(1-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,                     DungeonBreak.mapSizeY-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,     DungeonBreak.mapSizeX-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,     DungeonBreak.mapSizeY-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2);
      
      
      
      
      for(int i=0;i<DungeonBreak.enemyArray.size();i++){
        g.setColor(new Color(230, 230,0));//draws a line from player to all enemies off screen, for navigation
        if(Math.sqrt(Math.pow(DungeonBreak.player.getY()-DungeonBreak.enemyArray.get(i).getY(),2)+ Math.pow(DungeonBreak.player.getX()-DungeonBreak.enemyArray.get(i).getX(),2))>=700){
          g.drawLine((int)(DungeonBreak.enemyArray.get(i)).getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2, (int)(DungeonBreak.enemyArray.get(i).getY()-(int)DungeonBreak.player.getY())+(int)DungeonBreak.maxy/2,(int)DungeonBreak.maxx/2,(int)DungeonBreak.maxy/2);
        }  
        g.setColor(new Color(0, 255, 0));//draws the enemies
        if(DungeonBreak.enemyArray.get(i).inScreen()==true){
          if ((int)(DungeonBreak.enemyArray.get(i)).getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2 -14 >= DungeonBreak.maxx/2){//which direction should enemy be facing?
            DungeonBreak.enemyArray.get(i).setEnemyFace( 4);
          }else{
            DungeonBreak.enemyArray.get(i).setEnemyFace(0);
          }
          
          if (DungeonBreak.enemyArray.get(i).getTimeForChangeFace() >= 300){//time between sprite changes
            DungeonBreak.enemyArray.get(i).setCurrentenemySprite(DungeonBreak.enemyArray.get(i).getCurrentenemySprite()+1);
            DungeonBreak.enemyArray.get(i).setTimeForChangeFace();
            if (DungeonBreak.enemyArray.get(i).getCurrentenemySprite() >= 4){
              DungeonBreak.enemyArray.get(i).setCurrentenemySprite(0);
            }
          } 
          //draws appropriate sprite
          g.drawImage(enemysprites[DungeonBreak.enemyArray.get(i).getEnemyType()][DungeonBreak.enemyArray.get(i).getCurrentenemySprite() + DungeonBreak.enemyArray.get(i).getEnemyFace()],(int)(DungeonBreak.enemyArray.get(i)).getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,(int)(DungeonBreak.enemyArray.get(i).getY()-(int)DungeonBreak.player.getY())+(int)DungeonBreak.maxy/2,null);
          //end for loop
        }
        //end for loop
      }
      
      if (MouseInfo.getPointerInfo().getLocation().getX()-9 >= DungeonBreak.maxx/2){//what direction is player facing?
        spriteface = 0;
      } else {
        spriteface = 4;
      }
      
      if (DungeonBreak.nKey == 0){
        currentSprite = 0;//always draw standing sprite when player does not move
      } else {
        timerForThePicture ++;//time between each sprite
        if (timerForThePicture >= 4){
          currentSprite ++;
          timerForThePicture = 0;
          if (currentSprite >= 4){
            currentSprite = 0;
          }
        }
      } 
      g.drawImage(sprites[currentSprite + spriteface],(int)(DungeonBreak.maxx/2)-14,(int)(DungeonBreak.maxy/2)-14,null);//draw player sprite
      if(DungeonBreak.shiftKey==true){
        g.setColor(new Color(100, 200, 255));
        g.fillRect((int)DungeonBreak.maxx/2-2,(int)DungeonBreak.maxy/2-2,DungeonBreak.player.getWidth(), DungeonBreak.player.getLength());
      }
      if(stageNumber<6){
        g.setColor(new Color(80, 170, 235));
        g.fillRect((int)stairs.getx()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2, (int)stairs.gety()-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,stairs.getLength(),stairs.getWidth());
        if(DungeonBreak.enemyArray.size()==0){
          g.setColor(new Color(0, 0, 0));
          g.drawLine((int)stairs.getx()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,(int) stairs.gety()-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,(int)DungeonBreak.maxx/2,(int)DungeonBreak.maxy/2);
        }
      }else{
        g.setColor(new Color(0, 0, 0));//draws everything boss related, sprites as well as a line connecting the boss to the player, for navigation
        g.drawLine((int)boss.getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2,(int) boss.getY()-(int)DungeonBreak.player.getY()+(int)DungeonBreak.maxy/2,(int)DungeonBreak.maxx/2,(int)DungeonBreak.maxy/2);
        if ((int)boss.getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2 >= DungeonBreak.maxx/2){
          bossface = 4;
        } else {
          bossface = 0;
        }
        
        
        bosstimer ++;
        if (bosstimer >= 30){
          currentbossSprite ++;
          bosstimer = 0;
          if (currentbossSprite >= 4){
            currentbossSprite = 0;
          }
        }
        //} 
        g.drawImage(bosssprites[currentbossSprite + bossface],(int)(boss.getX()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2),(int)(boss.getY()-(int)DungeonBreak.player.getY())+(int)DungeonBreak.maxy/2,null);
        g.drawString("boss health:"+boss.getHealth(), 10, 130);
      }
      g.setColor(new Color(255, 0,0));
      for(int i=0;i<DungeonBreak.bulletArray.size();i++){
        //draw bullet
        if(DungeonBreak.bulletArray.get(i).inScreen()==true){
          g.fillRect((int)(DungeonBreak.bulletArray.get(i)).getx()-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2, (int)(DungeonBreak.bulletArray.get(i).gety()-(int)DungeonBreak.player.getY())+(int)DungeonBreak.maxy/2,DungeonBreak.bulletArray.get(i).getLength(), DungeonBreak.bulletArray.get(i).getWidth());
        }
        //end for loop
      }
      g.setColor(new Color(255,(int)( 255-stageNumber*255/6),0));
      for(int i=0;i<DungeonBreak.wallArray.size();i++){
        //draw bullet
        if(DungeonBreak.wallArray.get(i).inScreen()==true){
          g.fillRect((int)(DungeonBreak.wallArray.get(i).getx())-(int)DungeonBreak.player.getX()+(int)DungeonBreak.maxx/2, (int)(DungeonBreak.wallArray.get(i).gety()-(int)DungeonBreak.player.getY())+(int)DungeonBreak.maxy/2,DungeonBreak.wallArray.get(i).getLength(), DungeonBreak.wallArray.get(i).getWidth());
          //end for loop
        }
      }
      g.setColor(new Color(0, 0, 0));
      g.drawString("Enemy Number:"+DungeonBreak.enemyArray.size(), 10, 10);
      g.drawString("Your Point:"+(int)point, 10, 30);
      g.drawString("Life:"+DungeonBreak.player.getHealth(), 10, 50);
      g.drawString("Attack angle:"+DungeonBreak.player.getBulletAngle(), 10, 70);
      g.drawString("Bomb Number:"+DungeonBreak.player.getBombNumber(), 10, 90);
      g.drawString("attack Speed:"+(double)1000/DungeonBreak.player.getCoolDownTime()+"per second", 10, 110);
      
      if(DungeonBreak.fKey==true){
        g.setColor(new Color(0, 160, 0));
        g.fillRect(100, 100, DungeonBreak.maxx-200,DungeonBreak.maxy-200 );
        g.setColor(new Color(255, 255, 255));
        g.fillRect(200, 200, 250,50);
        g.fillRect(200, 400, 250,50);
        g.fillRect(200, 600,250,50);
        g.fillRect(110,110,150,50);
        g.fillRect(500, 200,250,50);
        g.fillRect(500, 400,250,50);
        g.fillRect(500, 600,250,50);
        g.setColor(new Color(0,0,0));
        g.drawString("more health:"+ 100*stageNumber,200, 230 );
        g.drawString("shoot more bullet"+ 400*stageNumber,200, 430 );
        g.drawString("increase attacking speed"+ 100,200, 630 );
        g.drawString("increase attacking angle"+ 1,500, 230);
        g.drawString("decrease attacking angle"+ 10,500, 430 );
        g.drawString("increase bomb number"+ 150*stageNumber,500, 630 );
        g.drawString("exit the game",110, 120 );
        
        
        
      }
      
      painting=false;
    }else if(stageNumber<=0){
      g.setColor(new Color(0,0,0));
      g.fillRect(0,0,DungeonBreak.maxx,DungeonBreak.maxy);
      g.drawImage(menu[stageNumber+4], DungeonBreak.maxx/2 - 750, DungeonBreak.maxy/2 - 425,null);
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  /**
   * animate
   * this methord is the main loop of the game
   */
  
  public void animate(){
    
    //when it is in the menu screen
    while(stageNumber<0){
      stageNumber++;
      this.repaint();
      //stage+1 when mouse press
      
      while(DungeonBreak.mousePress==false){
        try{Thread.sleep(1);    //These two lines add a small delay
        } catch (Exception exc){}
      }
      
      DungeonBreak.mousePress=false;
    }
    //when stage>0, run the change stage code
    changeStage();
    
    //turn the update key to false
    DungeonBreak.fKey=false;
    //start the main loop
    
    
    
    
    
    while(game==true){
      
      if(painting==false){
        //get time
        timePast=time.timePast();
        //update time
        time.upDate();
        //run player movement code
        this.playerMovement();
        
        //if it is boss, then do the boss movement
        if(stageNumber==6){
          boss.move();
          boss.attack();
          //is boss dead, then end the agme
          if(boss.getHealth()<=0){
            boss=null;
            game=false;
          }
        }
        
        
        //for loop to see if any thing hite the bullet
        for(int i=0;i<DungeonBreak.bulletArray.size();i++){
          
          
          for(int e=0;e<DungeonBreak.wallArray.size();e++){
            if( DungeonBreak.bulletArray.get(i).getBoundingBox().intersects(DungeonBreak.wallArray.get(e).getBoundingBox())){
              //if bullet hit the wall, then run remove on the bullet
              DungeonBreak.bulletArray.get(i).remove();
            }
          }
          //if the bullet is in the map
          if( DungeonBreak.bulletArray.get(i).inMap()==false){
            DungeonBreak.bulletArray.get(i).move(timePast);
            if(DungeonBreak.bulletArray.get(i).getIsPlayerBullet()==false){
              
              //if hit player, then player hp-1, run remove on all the bullet, so change the remove verable to true so they will be remove from the list later on
              if(DungeonBreak.bulletArray.get(i).getBoundingBox().intersects(DungeonBreak.player.getBoundingBox())){
                DungeonBreak.player.setHealth(DungeonBreak.player.getHealth()-1);
                for(int sizeOfBullet=0;sizeOfBullet<DungeonBreak.bulletArray.size();sizeOfBullet++){
                  DungeonBreak.bulletArray.get(sizeOfBullet).remove();
                }
              }
              
              
            }else if(DungeonBreak.bulletArray.get(i).getIsPlayerBullet()==true){
              for(int e=0;e<DungeonBreak.enemyArray.size();e++){
                //if hit enemy, then enemy hp-1, run remove on the bullet, get points ; 
                if(DungeonBreak.bulletArray.get(i).getBoundingBox().intersects(DungeonBreak.enemyArray.get(e).getBoundingBox())){
                  DungeonBreak.enemyArray.get(e).setHealth(DungeonBreak.enemyArray.get(e).getHealth()-1);
                  DungeonBreak.bulletArray.get(i).remove();
                  point+=Math.random()*4*stageNumber;
                }
              }
              
              //if hit boss, then boss hp-1, run remove on the bullet, get points ; 
              if(stageNumber==6){
                if(DungeonBreak.bulletArray.get(i).getBoundingBox().intersects(boss.getBoundingBox())){
                  boss.setHealth(boss.getHealth()-1);
                  DungeonBreak.bulletArray.get(i).remove();
                  point+=Math.random();
                }
                
              }
              
            }
          }else{
            
            // if the bullet is outsied the map, then change the remove verable to true so it will be remove from the list later on
            DungeonBreak.bulletArray.get(i).remove();
          }
          
        }
        
        // loop over all the bullet, if the remove verable is true, then remove the bullet;
        for(int i=0;i<DungeonBreak.bulletArray.size();i++){
          if  (DungeonBreak.bulletArray.get(i).ifRemove()==true){
            DungeonBreak.bulletArray.remove(i);
            i--;
          }
        }
        
        
        //end the game when player have no health
        if(DungeonBreak.player.getHealth()<=0){
          game=false;
        }
        
        //remove the enemy when it have no health
        for(int i=0;i<DungeonBreak.enemyArray.size();i++){
          if(DungeonBreak.enemyArray.get(i).getHealth()<=0){
            DungeonBreak.enemyArray.remove(i);
            
            //change to get some update when remove a enemy
            if((Math.random()*10)<2){
              DungeonBreak.player.setHealth(DungeonBreak.player.getHealth()+1);
            }
            if((Math.random()*10)<1){
              DungeonBreak.player.setBombNumber(DungeonBreak.player.getBombNumber()+1);
            }
            if((Math.random()*100)<5){
              DungeonBreak.player.setCoolDownTime(DungeonBreak.player.getCoolDownTime()-1);
            }
            if((Math.random()*100)<1){
              DungeonBreak.player.setShootBulletNumber(DungeonBreak.player.getShootBulletNumber()+1);
            }
            i--;
          }
        }
        // make enemy do there movement
        for(int i=0;i<DungeonBreak.enemyArray.size();i++){
          DungeonBreak.enemyArray.get(i).move(timePast);
          DungeonBreak.enemyArray.get(i).attack();
          if(DungeonBreak.enemyArray.get(i).getX()<0||DungeonBreak.enemyArray.get(i).getX()>DungeonBreak.mapSizeX||DungeonBreak.enemyArray.get(i).getY()<0||DungeonBreak.enemyArray.get(i).getY()>DungeonBreak.mapSizeY){
            DungeonBreak.enemyArray.remove(i);
            i--;
            
          }
          //end for loop
        }
        
        //get points when change the stage
        if(stageNumber!=6){
          if(stairs.getBoundingBox().intersects(DungeonBreak.player.getBoundingBox())){
            point+=500*stageNumber;
            changeStage();
          }
        }
        //tell the program that it is painting
        painting=true;
        this.repaint(); // repaint the screen 
        
        
        //repaint
        
      }
      
      
      try{Thread.sleep(1);    // a small delay
      } catch (Exception exc){}
      
    }
    try{Thread.sleep(1000);    //These two lines add a small delay
    } catch (Exception exc){}
    //end the frame
    DungeonBreak.thisFrame.dispose();
  }
  
  
  
  
  
  
  /*pleyerMovement
   * this methord response when a key press, and move the player
   */
  public void playerMovement(){
    
    //change the current speed of the player if it press more then 1 key
    DungeonBreak.player.changeCurrentSpeed();
    //if left press then place a bomb
    if (DungeonBreak.rightMouse == true) {
      DungeonBreak.player.bomb();
      DungeonBreak.rightMouse = false;
    }
    
    //if f key press, then pen the menu
    if (DungeonBreak.fKey == true) {
      
      
      if(DungeonBreak.mousePress==true){
        //if mouse move in the the position and press, then point - 150, and bomb number +1
        
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=500&&MouseInfo.getPointerInfo().getLocation().getX()-9<=750&&MouseInfo.getPointerInfo().getLocation().getY()-9>=600&&MouseInfo.getPointerInfo().getLocation().getY()-9<=650){
          if(point>=150*stageNumber){
            DungeonBreak.player.setBombNumber( DungeonBreak.player.getBombNumber()+1);
            point=point-150*stageNumber;
            try{Thread.sleep(100);    //These two lines add a small delay
            } catch (Exception exc){}
          }
        }
        //if mouse move in the the position and press, then point - 10, and increase the Accuratic of the bullet
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=500&&MouseInfo.getPointerInfo().getLocation().getX()-9<=750&&MouseInfo.getPointerInfo().getLocation().getY()-9>=200&&MouseInfo.getPointerInfo().getLocation().getY()-9<=250){
          if(point>=10){
            DungeonBreak.player.setBulletAngle( DungeonBreak.player.getBulletAngle()+1);
            point=point-10;
            try{Thread.sleep(100);    //These two lines add a small delay
            } catch (Exception exc){}
          }
        }
        //if mouse move in the the position and press, then point - 10, and decrease the Accuratic of the bullet
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=500&&MouseInfo.getPointerInfo().getLocation().getX()-9<=750&&MouseInfo.getPointerInfo().getLocation().getY()-9>=400&&MouseInfo.getPointerInfo().getLocation().getY()-9<=450){
          if(point>=1){
            if(DungeonBreak.player.getBulletAngle()>0){
              DungeonBreak.player.setBulletAngle( DungeonBreak.player.getBulletAngle()-1);
              point=point-1;
              try{Thread.sleep(100);    //These two lines add a small delay
              } catch (Exception exc){}
            }
          }
        }
        
        //if mouse move in the the position and press, then exit
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=100&&MouseInfo.getPointerInfo().getLocation().getX()-9<=250&&MouseInfo.getPointerInfo().getLocation().getY()-9>=100&&MouseInfo.getPointerInfo().getLocation().getY()-9<=150){
          DungeonBreak.thisFrame.dispose();
          game=false;
        }
        
        //if mouse move in the the position and press, then health++, decrease point
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=200&&MouseInfo.getPointerInfo().getLocation().getX()-9<=450&&MouseInfo.getPointerInfo().getLocation().getY()-9>=200&&MouseInfo.getPointerInfo().getLocation().getY()-9<=250){
          if(point>=100*stageNumber){
            DungeonBreak.player.setHealth( DungeonBreak.player.getHealth()+1);
            point=point-100*stageNumber;
            try{Thread.sleep(100);    //These two lines add a small delay
            } catch (Exception exc){}
          }
        }
        
        //if mouse move in the the position and press, then shoot number++, decrease point
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=200&&MouseInfo.getPointerInfo().getLocation().getX()-9<=450&&MouseInfo.getPointerInfo().getLocation().getY()-9>=400&&MouseInfo.getPointerInfo().getLocation().getY()-9<=450){
          if(point>=400*stageNumber){
            DungeonBreak.player. setShootBulletNumber( DungeonBreak.player.getShootBulletNumber()+1);
            point=point-400*stageNumber;
            try{Thread.sleep(100);    //These two lines add a small delay
            } catch (Exception exc){}
          }
        }
        //if mouse move in the the position and press, then attack speed++, decrease point
        if(MouseInfo.getPointerInfo().getLocation().getX()-9>=200&&MouseInfo.getPointerInfo().getLocation().getX()-9<=450&&MouseInfo.getPointerInfo().getLocation().getY()-9>=600&&MouseInfo.getPointerInfo().getLocation().getY()-9<=650){
          if(point>=100){
            if(DungeonBreak.player.getCoolDownTime()>0){
              DungeonBreak.player.setCoolDownTime( DungeonBreak.player.getCoolDownTime()-1);
              point=point-100;
              try{Thread.sleep(100);    //These two lines add a small delay
              } catch (Exception exc){}
            }
          }
        }
      }
    }else{
      //if the menu did not open, and mouse or z key pressed, then shoot bullet
      if (DungeonBreak.zKey == true||DungeonBreak.mousePress==true){
        
        DungeonBreak.player.attack();
      }
    }
    
    //if leftkey press, then if there are no wall and not on the edge, then move left
    if (DungeonBreak.leftKeyp == 1) {
      //if x > 0
      if(DungeonBreak.player.getX()>0){
        lastX=DungeonBreak.player.getX();
        DungeonBreak.player.setX(DungeonBreak.player.getX()-DungeonBreak.player.getCurrentSpeed()*timePast/20);
        collectionRecord=false;
        for(int i=0;i<DungeonBreak.wallArray.size();i++){
          if(DungeonBreak.player.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
            collectionRecord=true;
          }
        }
        if(collectionRecord==true){
          DungeonBreak.player.setX(lastX);
        }
      }
      //end if
    }
    
    
    //if right key press, then if there are no wall and not on the edge, then move right
    if (DungeonBreak.rightKeyp == 1){
      //x < maxx
      if(DungeonBreak.player.getX()<DungeonBreak.mapSizeX){
        lastX=DungeonBreak.player.getX();
        DungeonBreak.player.setX(DungeonBreak.player.getX()+DungeonBreak.player.getCurrentSpeed()*timePast/20);
        collectionRecord=false;
        for(int i=0;i<DungeonBreak.wallArray.size();i++){
          if(DungeonBreak.player.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
            collectionRecord=true;
          }
        }
        if(collectionRecord==true){
          DungeonBreak.player.setX(lastX);
        }
      }
      //end if
    }
    //if upkey press, then if there are no wall and not on the edge, then move up
    if (DungeonBreak.upKeyp == 1){
      //if y>0
      if(DungeonBreak.player.getY()>0){
        lastY=DungeonBreak.player.getY();
        DungeonBreak.player.setY(DungeonBreak.player.getY()-DungeonBreak.player.getCurrentSpeed()*timePast/20);
        collectionRecord=false;
        for(int i=0;i<DungeonBreak.wallArray.size();i++){
          if(DungeonBreak.player.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
            collectionRecord=true;
          }
        }
        if(collectionRecord==true){
          DungeonBreak.player.setY(lastY);
        }
      }
      //end if
    }
    
    
    //if down key press, then if there are no wall and not on the edge, then move down
    if (DungeonBreak.downKeyp == 1){
      //y<maxy
      
      if(DungeonBreak.player.getY()<DungeonBreak.mapSizeX){
        lastY=DungeonBreak.player.getY();
        DungeonBreak.player.setY(DungeonBreak.player.getY()+DungeonBreak.player.getCurrentSpeed()*timePast/20);
        collectionRecord=false;
        for(int i=0;i<DungeonBreak.wallArray.size();i++){
          if(DungeonBreak.player.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
            collectionRecord=true;
          }
        }
        if(collectionRecord==true){
          DungeonBreak.player.setY(lastY);
        }
      }
      //end if
    }
    //creat bullet
    
    
  }
  
  
  
  /*change Stage
   * this methord read the mapbox.txt and creat the walls and enemy
   */
  
  public void changeStage(){
    //update the stage level
    stageNumber++;
    
    //clear everything
    DungeonBreak.enemyArray.clear();
    DungeonBreak.wallArray.clear();
    DungeonBreak.bulletArray.clear();
    
    //read the map
    try{
      mapObject=new Scanner(new File("mapBox"+stageNumber+".txt"));
    } catch (IOException e) {};
    
    while (mapObject.hasNext()){
      readMap=mapObject.next();
      DungeonBreak.wallArray.add(new Wall(0, DungeonBreak.mapSizeY-(int)radioy,DungeonBreak.mapSizeX, (int)radioy));
      DungeonBreak.wallArray.add(new Wall( DungeonBreak.mapSizeX-(int)radiox, 0, (int)radiox,DungeonBreak.mapSizeY));
      if (readMap.equals("newWall")){
        DungeonBreak.wallArray.add(new Wall((int)(mapObject.nextInt()*radiox), (int)(mapObject.nextInt()*radioy),(int)(mapObject.nextInt()*radiox), (int)(mapObject.nextInt()*radioy)));
      } else if (readMap.equals("newExit")){
        stairs=new Wall((int)(mapObject.nextInt()*radiox), (int)(mapObject.nextInt()*radioy),(int)(mapObject.nextInt()*radiox), (int)(mapObject.nextInt()*radioy));
      }
      
    }
    //randomly set player position
    do{
      DungeonBreak.player.setX((int)(Math.random()*DungeonBreak.maxx));
      DungeonBreak.player.setY((int)(Math.random()*DungeonBreak.maxy));
      collectionRecord=false;
      for(int n=0;n<DungeonBreak.wallArray.size();n++){
        if(DungeonBreak.player.getBoundingBox().intersects(DungeonBreak.wallArray.get(n).getBoundingBox())){
          collectionRecord=true;
        }
      }
      if(stageNumber<6){
          if(DungeonBreak.player.getBoundingBox().intersects(stairs.getBoundingBox())){
          collectionRecord=true;
        }
      }
    }while(collectionRecord==true);
    
    //spawn enemy
    if(stageNumber<5){
      for(int i=0;i<20*stageNumber+20;i++){
        enemyType=(int)(Math.random()*5);
        if(enemyType==0){
          DungeonBreak.enemyArray.add(new Enemy((Math.random()*DungeonBreak.mapSizeX), (Math.random()*DungeonBreak.mapSizeY),30,30,(int)(Math.random()*(10+(stageNumber-1)*5))+10,(int)(Math.random()*(2+stageNumber*2))+4,0.7,10,600,(Math.random()*0.5)+0.5,1800,(int)(Math.random()*(3+stageNumber))+2, 1, 0));
        }else if(enemyType==1){
          DungeonBreak.enemyArray.add(new Enemy((Math.random()*DungeonBreak.mapSizeX), (Math.random()*DungeonBreak.mapSizeY),30,30,(int)(Math.random()*(10+(stageNumber-1)*5))+10,1,1,1,150,(Math.random()*0.5)+0.5,1800,(int)(Math.random()*(10+stageNumber*3))+5, 1, 1));    //machanegun
        }else if(enemyType==2){
          DungeonBreak.enemyArray.add(new Enemy((Math.random()*DungeonBreak.mapSizeX), (Math.random()*DungeonBreak.mapSizeY),30,30,(int)(Math.random()*(10+(stageNumber-1)*5))+10,1,(Math.random()*(1+stageNumber*0.5))+1,1,1400,(int)(Math.random()*0.5)+0.5,0,1, 1, 2));   //snaper
        }else if(enemyType==3){
          DungeonBreak.enemyArray.add(new Enemy((Math.random()*DungeonBreak.mapSizeX), (Math.random()*DungeonBreak.mapSizeY),30,30,(int)(Math.random()*(10+(stageNumber-1)*5))+10,(int)(Math.random()*(7+stageNumber*3))+6,0.4,360,300,(Math.random()*0.5)+0.5,4000,4, 1, 3)); // shortgan
        }else if(enemyType==4){
          DungeonBreak.enemyArray.add(new Enemy((Math.random()*DungeonBreak.mapSizeX), (Math.random()*DungeonBreak.mapSizeY),30,30,(int)(Math.random()*(10+(stageNumber-1)*5))+10,1,0.2,0,300,(Math.random()*0.5)+0.5,4000,2, 2, 4)); // chase
        }
        //remove the enemy if it get spawn in to a wall
        for(int n=0;n<DungeonBreak.wallArray.size();n++){
          if(DungeonBreak.enemyArray.get(DungeonBreak.enemyArray.size()-1).getBoundingBox().intersects(DungeonBreak.wallArray.get(n).getBoundingBox())||Math.sqrt(Math.pow(DungeonBreak.player.getY()-DungeonBreak.enemyArray.get(i).getY(),2)+ Math.pow(DungeonBreak.player.getX()-DungeonBreak.enemyArray.get(i).getX(),2))<=700){
            DungeonBreak.enemyArray.remove(DungeonBreak.enemyArray.size()-1);
            i--;
            break;
          }
        }
        
      }
    }else{
      //if stage is 6, then spawn boss
      boss=new Boss(500, 500, 1000, 3000);
    }
  }
  
}