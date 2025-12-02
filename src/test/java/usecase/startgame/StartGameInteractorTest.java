package usecase.startgame;

import entities.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartGameInteractorTest {

    // ------------------------
    // Fake DeckProvider
    // ------------------------
    static class FakeDeckProvider implements DeckProvider {
        boolean shuffleCalled = false;

        @Override
        public void shuffleDeck() {
            shuffleCalled = true;
        }

        @Override
        public Card drawCard() { return null; }

        @Override
        public List<Card> drawCards(int count) {
            return Collections.emptyList();
        }
    }

    // ------------------------
    // Fake Deck
    // ------------------------
    static class FakeDeck extends Deck {

        private final FakeDeckProvider provider;

        public FakeDeck() {
            this(new FakeDeckProvider());
        }

        private FakeDeck(FakeDeckProvider provider) {
            super(provider);
            this.provider = provider;
        }

        public boolean wasShuffled() {
            return provider.shuffleCalled;
        }
    }

    // ------------------------
    // Fake Game
    // ------------------------
    static class FakeGame extends Game {
        boolean resetCalled = false;

        public FakeGame(Deck deck) {
            super(deck);
        }

        @Override
        public void reset() {
            resetCalled = true;
            super.reset();
        }
    }

    // ------------------------
    // Fake presenter
    // ------------------------
    static class FakePresenter implements StartGameOutputBoundary {
        boolean presented = false;

        @Override
        public void present(StartGameOutputData data) {
            presented = true;
        }
    }

    // ------------------------
    // Fake data access
    // ------------------------
    static class FakeDataAccess implements StartGameDataAccessInterface {
        FakeDeck deck = new FakeDeck();
        FakeGame game = new FakeGame(deck);

        @Override
        public Deck getDeck() { return deck; }

        @Override
        public Game getGame() { return game; }
    }


    // ==== ACTUAL TESTS ==============================================================

    @Test
    void testExecuteNormalFlow() {
        FakePresenter presenter = new FakePresenter();
        FakeDataAccess data = new FakeDataAccess();
        StartGameInteractor interactor = new StartGameInteractor(presenter, data);

        interactor.execute(new StartGameInputData());

        assertTrue(data.game.resetCalled);
        assertTrue(data.deck.wasShuffled());
        assertTrue(presenter.presented);
    }

    @Test
    void testExecuteExceptionPath() {
        StartGameOutputBoundary presenter = d -> fail("Presenter must not be called");

        StartGameDataAccessInterface data = new StartGameDataAccessInterface() {

            @Override
            public Deck getDeck() {
                throw new RuntimeException("Forced exception");
            }

            @Override
            public Game getGame() {
                return new FakeGame(new FakeDeck());
            }
        };

        StartGameInteractor interactor = new StartGameInteractor(presenter, data);

        assertDoesNotThrow(() -> interactor.execute(new StartGameInputData()));
    }

    @Test
    void testGetDeck() {
        FakePresenter presenter = new FakePresenter();
        FakeDataAccess data = new FakeDataAccess();
        StartGameInteractor interactor = new StartGameInteractor(presenter, data);

        assertEquals(data.deck, interactor.getDeck());
    }

    @Test
    void testGetPlayer() {
        FakePresenter presenter = new FakePresenter();
        FakeDataAccess data = new FakeDataAccess();
        StartGameInteractor interactor = new StartGameInteractor(presenter, data);

        assertEquals(data.game.getPlayer(), interactor.getPlayer());
    }

    @Test
    void testGetDealer() {
        FakePresenter presenter = new FakePresenter();
        FakeDataAccess data = new FakeDataAccess();
        StartGameInteractor interactor = new StartGameInteractor(presenter, data);

        assertEquals(data.game.getDealer(), interactor.getDealer());
    }
}
