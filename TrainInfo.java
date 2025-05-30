import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.*;

public class TrainInfo {

    public ArrayList<Passenger> passengers;
    public Station location;
    public ArrayList<Station> route;
    public boolean forward;
    
    public static Lock TrainLock = new ReentrantLock();
    public static Condition Traincondition =TrainLock.newCondition();

    TrainInfo(ArrayList<Passenger> p, Station l, ArrayList<Station> r){
        passengers = p;
        location = l;
        route = r; 
        forward = true;
    }

    public String toString(){
        String s = "";
        s+= " Passengers : " + passengers.toString() + "\n";
        s+= " Location : " + location.toString() + "\n";
        s+= " Route : " + route.toString() + "\n";
        s+= " Direction " + (forward ? " Forward" : "Backward");
        return s;
    }
}
