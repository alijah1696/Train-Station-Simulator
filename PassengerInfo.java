import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.*;

public class PassengerInfo {

    public Station location;
    public ArrayList<Station> journey;
    public boolean onTrain;
    public Condition condition; 
    public Lock lock;

    PassengerInfo(Station l, ArrayList<Station> j){
        location = l;
        journey = j;
        onTrain = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    //Helper methods
    public String toString(){
        String s = "";
        s+= " Location : " + location.toString() + "\n";
        s+= " Journey : " + journey.toString() + "\n";
        s+= " State : " + (onTrain ? " on Train " : " at Station ");
        return s;
    }
}
