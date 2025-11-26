package frameworks_and_drivers;

import entities.Card;
import okhttp3.*;
import org.json.*;
import usecase.DeckProvider; // IMPORT FIX

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class DeckApiService implements DeckProvider {
    private final String API_URL = "https://deckofcardsapi.com/api/deck";
    private final OkHttpClient client = new OkHttpClient();
    String deckId;

    public DeckApiService() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL + "/new/shuffle/?deck_count=1")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                JSONObject json = new JSONObject(response.body().string());
                deckId = json.getString("deck_id");
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed to initialize deck API", e);
        }
    }

    @Override
    public void shuffle() {
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
            if (response.body() != null) {
                JSONObject json = new JSONObject(response.body().string());
                JSONArray cardArray = json.getJSONArray("cards");
                JSONObject cardJson = cardArray.getJSONObject(0);
                return getCard(cardJson);
            }
            throw new RuntimeException("Empty response from Draw Card API");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Card> drawCards(int count) {
        try {
            Request request = new Request.Builder()
                    .url(API_URL + "/" + deckId + "/draw/?count=" + count)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                JSONObject json = new JSONObject(response.body().string());
                JSONArray cardArray = json.getJSONArray("cards");

                List<Card> cardList = new ArrayList<>();
                for (int i = 0; i < cardArray.length(); i++) {
                    JSONObject cardJson = cardArray.getJSONObject(i);
                    cardList.add(getCard(cardJson));
                }
                return cardList;
            }
            throw new RuntimeException("Empty response from Draw Cards API");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Card getCard(JSONObject cardJson) {
        String value = cardJson.getString("value");
        int valueInt = switch (value) {
            case "ACE" -> 11;
            case "KING", "QUEEN", "JACK" -> 10;
            default -> Integer.parseInt(value);
        };
        String suit = cardJson.getString("suit");
        String code = cardJson.getString("code");
        return new Card(code, suit, value, valueInt);
    }
}