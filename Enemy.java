
/*enemy
 * this program is the object of enemy
 */

import java.awt.*;
public  class Enemy extends Box {
  private double angleOfMovement;     //angle to move in
  private double lastX, lastY;                 //record the last x and y so the enemy returns to the last position if he hits a wall   
  private boolean collectionRecord;   //check if it collides with any walls
  private double shootIntervel;           //how long it takes to start shooting again
  private int shootTurn;                          //how many bullets it shoots within an interval
  private int currentShootTurn;           // how many times it can still shoot
  private Clock coolDown;                     //cool down time, fire rate
  private double randomAngle;          //random angle to make the bullet less Accurate
  private Clock clockForShootingInterval;         //clock to track time for the next shooting interval
  private int shootBulletType;                     //type of bullet shot
  private int enemyFace;                           //direction sprite is facing
  private int enemyType;                     //what sprite sheet to use
  private int currentenemySprite;      //current sprite for enemy
  private Clock timerForChangeFace; //clock for the time it will change images
  //constructor
  Enemy(double x, double y,int length, int width, int health,  int shootBulletNumber,  double bulletSpeed, double bulletChangeAngle, int coolDownTime, double movementSpeed, double shootIntervel, int shootTurn, int shootBulletType, int enemyType){
    //set all the values
    super.setBoundingBox(new Rectangle((int)x, (int)y, length, width));
    currentShootTurn=0;
    super.setX(x);
    super.setY(y);
    super.setLength(length);
    super.setWidth(width);
    super.setHealth(health);
    this.shootIntervel=shootIntervel;
    this.shootTurn=shootTurn;
    super.setShootBulletNumber(shootBulletNumber);
    super.setBulletSpeed(bulletSpeed);
    super.setBulletAngle(bulletChangeAngle);
    super.setCoolDownTime(coolDownTime);
    currentenemySprite=0;
    enemyFace=0;
    this.enemyType=enemyType;
    super.setMovementSpeed(movementSpeed);
    //chase player
    angleOfMovement=(double)Math.atan2(DungeonBreak.player.getY()-y, DungeonBreak.player.getX()-x);
    coolDown=new Clock();
    this.shootBulletType=shootBulletType;
    clockForShootingInterval=new Clock();
    timerForChangeFace=new Clock();
  }
  //getter
  public int getEnemyFace(){
    return enemyFace;
  }
  public int getEnemyType(){
    return enemyType;
  }
  public int getCurrentenemySprite(){
    return currentenemySprite;
  }
  public int getTimeForChangeFace(){
    return timerForChangeFace.timePast();
  }
  //setter
  public void setEnemyFace(int enemyface){
    this.enemyFace=enemyFace;
  }
  public void setCurrentenemySprite(int currentenemySprite){
    this.currentenemySprite=currentenemySprite;
  }
  
  public void setTimeForChangeFace(){
    timerForChangeFace.upDate();;
  }
  
  
  /*inScreen
   * this method check if the enemy is in the screen
   */
  public boolean inScreen(){
    if(super.getX()-DungeonBreak.player.getX()+DungeonBreak.maxx/2+super.getLength()>=0&&super.getX()-DungeonBreak.player.getX()+DungeonBreak.maxx/2<=DungeonBreak.maxx&&super.getY()-DungeonBreak.player.getY()+DungeonBreak.maxy/2+super.getWidth()>=0&&super.getY()-DungeonBreak.player.getY()+DungeonBreak.maxy/2<=DungeonBreak.maxy){
      return true;
    }else{
      return false;
    }
  }
  
  
  /*move
   * this method move the enemy
   */
  public void move(long timePast){
    
    //chase the player
    if(Math.sqrt(Math.pow(DungeonBreak.player.getY()-super.getY(),2)+ Math.pow(DungeonBreak.player.getX()-super.getX(),2))<=1000){
      if(Math.sqrt(Math.pow(DungeonBreak.player.getY()-super.getY(),2)+ Math.pow(DungeonBreak.player.getX()-super.getX(),2))>=100){
        angleOfMovement=(double)Math.atan2(DungeonBreak.player.getY()-super.getY(), DungeonBreak.player.getX()-super.getX());
      }
      
      
      //move in y direction, and if it goes in to the wall, then return to the last position
      lastY=super.getY();
      super.setY((super.getY()+super.getMovementSpeed() * Math.sin((angleOfMovement))*timePast/10));
      collectionRecord=false;
      for(int i=0;i<DungeonBreak.wallArray.size();i++){
        if(super.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
          collectionRecord=true;
        }
      }
      if(collectionRecord==true){
        super.setY(lastY);
      }
      //move in x direction, and if it goes in to the wall, then return to the last position
      lastX=super.getX();
      super.setX((super.getX()+super.getMovementSpeed() * Math.cos((angleOfMovement))*timePast/10));
      collectionRecord=false;
      for(int i=0;i<DungeonBreak.wallArray.size();i++){
        if(super.getBoundingBox().intersects(DungeonBreak.wallArray.get(i).getBoundingBox())){
          collectionRecord=true;
        }
      }
      if(collectionRecord==true){
        super.setX(lastX);
      }
    }
  }
  
  /*attack
   * this method lets the enemy attack
   */
  public void attack(){
    
    //if it is near player, then it the current shoot turn >0 or can start a new shoot intercal, then shoot
    //(if player is within range, start shooting immediately)
    if(Math.sqrt(Math.pow(DungeonBreak.player.getY()-super.getY(),2)+ Math.pow(DungeonBreak.player.getX()-super.getX(),2))<=800){
      if(currentShootTurn>0||clockForShootingInterval.timePast()>=shootIntervel){
        if(currentShootTurn<=0){
          currentShootTurn=shootTurn;
        }
        //check the cooldown time per shot
        if(coolDown.timePast()>=super.getCoolDownTime()){
          currentShootTurn--;
          //shoot the bullet at the player angle+random angle
          for(int i=0;i<super.getShootBulletNumber();i++){
            //randomize the angle and shoot the bullet
            randomAngle=Math.random()*super.getBulletAngle()*2-super.getBulletAngle();
            if(shootBulletType==1){
              (DungeonBreak.bulletArray).add(new BulletType1((super.getX()+super.getWidth()/2), super.getY()+super.getLength()/2,DungeonBreak.player.getX(),DungeonBreak.player.getY(),   15,15, super.getBulletSpeed(), super.getBulletAngle()+ randomAngle,false, 6000));
            }else if(shootBulletType==2){
              (DungeonBreak.bulletArray).add(new BulletType2((super.getX()+super.getWidth()/2), super.getY()+super.getLength()/2,  15,15, super.getBulletSpeed(), super.getBulletAngle()+ randomAngle,false, 12000));
            }
          }
          
          coolDown.upDate();
        }
        clockForShootingInterval.upDate();
      }
    }
  }
}
