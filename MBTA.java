import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MBTA {

  // Creates an initially empty simulation
  public MBTA() { }

  //config file that holds all mbta state information
  public Config config = new Config();

  // Adds a new transit line with given name and stations
  public void addLine(String name, List<String> stations) {
    config.addLine(name, stations);
  }

  // Adds a new planned journey to the simulation
  public void addJourney(String name, List<String> stations) {
    config.addJourney(name, stations);
  }

  // Return normally if initial simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkStart() {
    config.checkStart();
  }

  // Return normally if final simulation conditions are satisfied, otherwise
  // raises an exception
  public void checkEnd() {
    config.checkEnd();
  }

  // reset to an empty simulation
  public void reset() {
    config = new Config();
  }

  // adds simulation configuration from a file
  public void loadConfig(String filename) {
    try{
      Gson gson = new Gson();
      Reader reader = Files.newBufferedReader(Paths.get(filename));
      config = gson.fromJson(reader, Config.class);
      config.setUp();
    }catch(IOException e){
      throw new RuntimeException(e);
    }
  }

  public String toString(){
    return config.toString();
  }

  public static MBTA createCopy(MBTA target){
    MBTA replica = new MBTA();
    for(String l : target.config.lines.keySet()){  
        replica.addLine(l, target.config.lines.get(l));
    }
    for(String t : target.config.trips.keySet()){  
      replica.addLine(t, target.config.trips.get(t));
    }
    replica.config.setUp();
    return replica;
}
}
