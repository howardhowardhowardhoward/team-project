package frameworks_and_drivers;

import entities.Card;
import entities.DeckProvider;
import okhttp3.*;
import org.json.*;

import java.io.IOException;

public class DeckApiService implements DeckProvider {
    private final String API_URL = "https://deckofcardsapi.com/api/deck";
    private final OkHttpClient client = new OkHttpClient();
    String deckId;

    public DeckApiService() throws IOException {
        Request request = new Request.Builder()
                .url(API_URL + "/new/shuffle/?deck_count=6")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(response.body().string());
        deckId = json.getString("deck_id");
    }

    @Override
    public void shuffleDeck() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL + "/" + deckId + "/shuffle/")
                    .build();
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Card drawCard() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL + "/" + deckId + "/draw/?count=1")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            JSONArray cardArray = json.getJSONArray("cards");
            JSONObject cardJson = cardArray.getJSONObject(0);

            String value = cardJson.getString("value");
            int valueInt = Integer.parseInt(value);
            String suit = cardJson.getString("suit");
            String code = cardJson.getString("code");
            String image = cardJson.getString("image");

            return new Card(code, suit, image, valueInt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
