package frameworks_and_drivers;

import entities.Card;
import entities.DeckProvider;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.*;

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
            JSONObject json = new JSONObject(response.body().string());
            deckId = json.getString("deck_id");
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

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

            return getCard(cardJson);
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
            JSONObject json = new JSONObject(response.body().string());
            JSONArray cardArray = json.getJSONArray("cards");

            List<Card> cardList = new ArrayList<>();
            for (int i = 0; i < cardArray.length(); i++) {
                JSONObject cardJson = cardArray.getJSONObject(i);
                Card card = getCard(cardJson);
                cardList.add(card);
            }
            return cardList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static Card getCard(JSONObject cardJson) {
        String value = cardJson.getString("value");
        int valueInt = switch (value) {
            case "ACE" -> 11;
            case "KING", "QUEEN", "JACK" -> 10;
            default -> Integer.parseInt(value);
        };
        String suit = cardJson.getString("suit");
        String code = cardJson.getString("code");
        String image = cardJson.getString("image");

        return new Card(code, suit, image, valueInt);
    }

}
