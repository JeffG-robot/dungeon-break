/*Wall
 *the object of the wall
 */

import java.awt.*;
public class Wall {
  private int x;    //x position of the wall
  private int  y; //y position of the wall
  private int length;  //length of the wall
  private int  width; //width of the wall
  private Rectangle boundingBox;  //bounding box of the wall
  //constructor
  Wall(int x, int y,int length, int width ){
    //set the x, y, length, and width and boundingbox
    this.x=x;
    this.y=y;
    this.length=length;
    this.width=width;
    boundingBox=new Rectangle(x, y, length, width);
  }
  
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
  
  //getter
  public Rectangle getBoundingBox(){
    return boundingBox;
  }
  public double getx(){
    return x;
  }
  public double gety(){
    return y;
  }
  public int getLength(){
    return length;
  }
  public int getWidth(){
    return width;
  }
}
