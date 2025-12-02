package frameworks_and_drivers.loadsavegame;

import org.json.JSONObject;
import usecase.loadsavegame.LoadGameDataAccessInterface;

import java.io.*;

public class LoadGameDataAccess implements LoadGameDataAccessInterface {
    private static final String FILE_PATH = "data.json";  // to be filled in later

    @Override
    public void saveBalance(double balance) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("balance", balance);
            writer.write(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double loadBalance() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 1000;  // default player balance
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            return jsonObject.getDouble("balance");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load player data", e);
        }
    }
}
