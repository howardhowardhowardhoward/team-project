import entities.Deck;
import entities.Game;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.StartGame.StartGameDataAccess;
import frameworks_and_drivers.LoadGameDataAccess;
import frameworks_and_drivers.Homepage;
import interface_adapters.*;
import interface_adapters.StartGame.StartGameController;
import interface_adapters.StartGame.StartGamePresenter;
import interface_adapters.StartGame.StartGameViewModel;
import usecase.StartGame.*;
import usecase.LoadGame.*;

public class Main {
    public static void main(String[] args) {
        StartGameViewModel startGameViewModel = new StartGameViewModel();
        StartGamePresenter startGamePresenter = new StartGamePresenter(startGameViewModel);
        DeckApiService api = new DeckApiService();
        Deck deck = new Deck(api);
        Game game = new Game(deck);
        StartGameDataAccess startGameDataAccess = new StartGameDataAccess(deck, game);
        StartGameInteractor startGameInteractor = new StartGameInteractor(startGamePresenter,
                startGameDataAccess);
        StartGameController startGameController = new StartGameController(startGameInteractor);
        LoadGameViewModel loadGameViewModel = new LoadGameViewModel();
        LoadGamePresenter loadGamePresenter = new LoadGamePresenter(loadGameViewModel);
        LoadGameDataAccess jsonPlayerDataAccess = new LoadGameDataAccess();
        LoadGameInteractor loadGameInteractor = new LoadGameInteractor(jsonPlayerDataAccess,
                startGameDataAccess, loadGamePresenter);
        LoadGameController loadGameController = new LoadGameController(loadGameInteractor);

        new Homepage(startGameController, startGameViewModel, loadGameController, loadGameViewModel,
                jsonPlayerDataAccess);
    }
}
