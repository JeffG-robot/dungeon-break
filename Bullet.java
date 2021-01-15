/*Bullet
 * this program is an abstract class of bulletType1 and 2
 */
import java.awt.*;
public abstract class Bullet {
  private  double x;
  private  double y;
  private  Rectangle boundingBox;
  private   int length;
  private double speed;
  private   int width;
  private   double endx;
  private   double endy;
  private  double deltaX;
  private  double deltaY;
  private  double angle;
  private int existTime;
  private  boolean isPlayerBullet;
  private Clock trackTime;
  private  boolean remove=false;
  
  /*inScreen
   * this method checks if the wall is in the screen
   */
  public boolean inScreen(){
    if(x-DungeonBreak.player.getX()+DungeonBreak.maxx/2+length>=0&&x-DungeonBreak.player.getX()+DungeonBreak.maxx/2<=DungeonBreak.maxx&&y-DungeonBreak.player.getY()+DungeonBreak.maxy/2+width>=0&&y-DungeonBreak.player.getY()+DungeonBreak.maxy/2<=DungeonBreak.maxy){
      return true;
    }else{
      return false;
    }
  }
  
  
  /*move
   * this method moves the bullet
   */
  public void move(long timePast){
    if( trackTime.timePast()>=existTime){
      remove=true;
    }else{
      x+=  speed * Math.cos((angle))*timePast/3;
      y+= speed * Math.sin((angle))*timePast/3;
      boundingBox.x=(int)x;
      boundingBox.y=(int)y;
    }
  }
//getter
  public Rectangle getBoundingBox( ){
    return boundingBox;
  }
  public Clock getTrackTime(){
    return trackTime;
  }
  public double getSpeed(){
    return speed;
  }
  public boolean getIsPlayerBullet(){
    return isPlayerBullet;
  }
  public double getAngle(){
    return angle;
  }
  public double getx(){
    return x;
  }
  public double gety(){
    return y;
  }
  public double getEndx(){
    return endx;
  }
  public double getEndy(){
    return endy;
  }
  
  public int getLength(){
    return length;
  }
  public int getWidth(){
    return width;
  }
  
  
  
  //setter
  public void setIsPlayerBullet(boolean isPlayerBullet){
    this.isPlayerBullet=isPlayerBullet;
  }
  public void setExistTime(int existTime){
    this.existTime=existTime;
  }
  public void  setTrackTime(Clock trackTime){
    this.trackTime=trackTime;
  }  
  public void setDeltaX(double deltaX){
    this.deltaX=deltaX;
  }
  public void setDeltaY(double deltaY){
    this.deltaY=deltaY;
  }
  public void setx(double x){
    boundingBox.x=(int)x;
    this.x=x;
  }
  public void sety(double y){
    boundingBox.x=(int)x;
    this.y=y;
  }
  public void setLength(int length){
    this.length=length;
  }
  public void setEndx(double endx){
    this.endx=endx;
  }
  public void setSpeed(double speed){
    this.speed=speed;
  }
  public void setEndy(double endy){
    this.endy=endy;
  }
  public void setWeight(int width){
    this.width=width;
  }
  public void setBoundingBox( Rectangle boundingBox){
    this.boundingBox=boundingBox;
  }
  
  
  /*remove
   * this method changes remove variable to true, so in the main loop, the program can remove the bullet
   */
  public void remove(){
    remove=true;
  }
  
  
  
  /*ifRemove
   * this method returns the value of remove 
   */
  public boolean ifRemove(){
    return remove;
  }
  
  /*setAngle
   * this method sets the angle of the movement
   */
  public void setAngle( double changeAngle){
    angle=(double)Math.atan2(deltaY, deltaX);
    angle=angle*180 /Math.PI+changeAngle;
    angle=angle* Math.PI/180;
  }
  
  
  /* inMap
   * this method checks if the bullet is out of the map
   */
  public Boolean inMap(){
    if(x>=0&&x<=DungeonBreak.mapSizeX&&y>=0&&y<=DungeonBreak.mapSizeX){
      return false;
    }else{
      return true;
    }
  }
  
  
  
}
