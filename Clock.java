/*Clock
 * this program is a timer that can update the time and tell how much time had past after the last update
 */
public class Clock {
  long lastTimeCheck; //last time update
  long currentTime;    //current time
  //constructor
  Clock(){
    lastTimeCheck=System.currentTimeMillis();
  }
  
  /* upDate
   * this method restarts the counting
   */
  public void upDate(){
    lastTimeCheck=System.currentTimeMillis();
  }
  /* timePast
   * this method return the amount of time past 
   */
  public int timePast(){
    currentTime=System.currentTimeMillis();
    return (int)(currentTime-lastTimeCheck);
  }
}
