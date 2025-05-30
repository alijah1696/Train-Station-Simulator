import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class Sim {

public static ArrayList<Thread> threads = new ArrayList<>();

  public static void run_sim(MBTA mbta, Log log) {
      List<PassengerThread> passengerThreads = new ArrayList<>();
      List<TrainThread> trainThreads = new ArrayList<>();

      for (Passenger p : mbta.config.P.keySet()) {
          PassengerThread thread = new PassengerThread(p, mbta.config.P.get(p), mbta, log);
          thread.start();
          passengerThreads.add(thread);
      }

      for (Train t : mbta.config.T.keySet()) {
          TrainThread thread = new TrainThread(t, mbta.config.T.get(t), mbta, log);
          thread.start();
          trainThreads.add(thread);
      }

      for (PassengerThread passengerThread : passengerThreads) {
          try {
              passengerThread.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
  }


  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
