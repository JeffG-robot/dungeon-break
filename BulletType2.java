/*BulletType2
 * this program is a object of the bullet that can chase player
 */


import java.awt.*;
public class BulletType2 extends Bullet{
  //constructor
  BulletType2(double x, double y,int length, int weight, double speed, double changeAngle, boolean isPlayerBullet, int existTime){
    super.setBoundingBox(new Rectangle((int)x, (int)y, length, weight));
    super.setx(x);
    super.sety(y);
    super.setSpeed(speed);
    super.setEndx(x);
    super.setEndy(y);
    super.setLength(length);
    super.setWeight(weight);
    super.setDeltaX(0);
    super.setDeltaY(0);
    super.setAngle(changeAngle);
    super.setTrackTime(new Clock());
    super.setIsPlayerBullet(isPlayerBullet);
    super.setExistTime(existTime);
  }
  /*move
   * this method overwrites the move method in Bullet, so it can chase the player while moving
   */
  public void move(long timePast){
    //change the endding position of the bullet to the player position
    if(DungeonBreak.player.getX()< super.getEndx()+10){
      super.setEndx(super.getEndx()-super.getSpeed()*timePast/3);
    }else if(DungeonBreak.player.getX()> super.getEndx()-10){
      super.setEndx(super.getEndx()+super.getSpeed()*timePast/3);
    }
    if(DungeonBreak.player.getY()< super.getEndy()+10){
      super.setEndy(super.getEndy()-super.getSpeed()*timePast/3);
    }else if(DungeonBreak.player.getY()> super.getEndy()-10){
      super.setEndy(super.getEndy()+super.getSpeed()*timePast/3);
    }
    
    //set angle and move
    super.setDeltaX(super.getEndx() - super.getx());
    super.setDeltaY(super.getEndy()  - super.gety());
    super.setAngle(0);
    super.move(timePast);
  }
  
}
