import java.lang.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TrainThread extends Thread {

    Train train;
    TrainInfo info;
    MBTA mbta;
    Log log;
    int index = 0;
    
    TrainThread(Train t, TrainInfo i ,MBTA m, Log l){
        mbta = m;
        train = t;
        info = i;
        log = l;

    }

    @Override
    public void run(){
        //loops until complete 
        while(!mbta.config.Completed()){
            try{
                //wakes up passengers and lets them on
                synchronized(mbta){
                    arriving();
                    if(mbta.config.Completed()) break;
                }
                //sleeps at statino to allow time to get on
                this.sleep(10);
                boolean moved = false;
                //checks if next station is free to move
                synchronized(mbta){
                    if(!mbta.config.isStationOccupied(next())){
                        move();
                        moved = true;
                    }   
                } 
                //if it did move there is a delay so another train can sync
                if(moved)this.sleep(5);
            }catch(InterruptedException e){
                throw new RuntimeException("Train thread interrupted");
            }
        
        }
    } 

//Helper methods for calculating next stop and waking up passengers
    public synchronized void move(){
            log.train_moves(train, current(), next());
            mbta.config.move(train, current(), next());
    }

    public void arriving(){
        ArrayList<Passenger> wake = new ArrayList<>();
        
        for(Passenger p : info.passengers){
            wake.add(p);
        }
        for(Passenger p : mbta.config.PassengersAtStation(current())){
            wake.add(p);
        }

        for(Passenger p : wake){
            mbta.config.P.get(p).lock.lock();
            mbta.config.P.get(p).condition.signalAll();
            mbta.config.P.get(p).lock.unlock();
        }
    }

    public Station next(){
        return info.route.get(getIndex() + (info.forward ? 1 : -1));
    }

    public Station current(){
        return info.route.get(getIndex());
    }

    public boolean cycleCompleted(){
        if(getIndex() == 0) return true;
        return false;
    }

    public int getIndex(){
        return info.route.indexOf(info.location);
    }

}