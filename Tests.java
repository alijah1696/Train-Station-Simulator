import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.junit.*;

public class Tests {

  public static void main(String[] args) {
    MBTA orignal = new MBTA();
    MBTA load = new MBTA();
    orignal.loadConfig("shared.json");
    load.loadConfig("shared.json");
    Log l = new Log();
    Sim.run_sim(orignal, l);
    Verify.verify(load, l);
    System.out.println(l.events().size());
  }
}
