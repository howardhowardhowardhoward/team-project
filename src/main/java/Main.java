import entities.Deck;
import entities.Game;
import frameworks_and_drivers.DeckApiService;
import frameworks_and_drivers.startgame.StartGameDataAccess;
import frameworks_and_drivers.loadsavegame.LoadGameDataAccess;
import frameworks_and_drivers.Homepage;
import interface_adapters.loadsavegame.LoadGameController;
import interface_adapters.loadsavegame.LoadGamePresenter;
import interface_adapters.loadsavegame.LoadGameViewModel;
import interface_adapters.startgame.StartGameController;
import interface_adapters.startgame.StartGamePresenter;
import interface_adapters.startgame.StartGameViewModel;
import usecase.startgame.*;
import usecase.loadsavegame.*;

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
