package usecase.updatebalance;

import entities.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateBalanceInteractorTest {

    @Test
    void successTest() {
        UpdateBalanceDataAccessInterface fakeDataAccess = new UpdateBalanceDataAccessInterface() {
            private Player p = new Player(100.0); // 初始 100 块

            @Override
            public Player get(String username) {
                return p;
            }

            @Override
            public void save(Player player) {
                System.out.println("Saved player with balance: " + player.getBalance());
            }
        };

        class TestPresenter implements UpdateBalanceOutputBoundary {
            double resultingBalance;
            String failMessage;

            @Override
            public void prepareSuccessView(UpdateBalanceOutputData outputData) {
                this.resultingBalance = outputData.getNewBalance();
            }

            @Override
            public void prepareFailView(String error) {
                this.failMessage = error;
            }
        }

        TestPresenter presenter = new TestPresenter();

        UpdateBalanceInteractor interactor = new UpdateBalanceInteractor(fakeDataAccess, presenter);

        UpdateBalanceInputData inputData = new UpdateBalanceInputData("Howard", 50.0);
        interactor.execute(inputData);

        assertEquals(150.0, presenter.resultingBalance);
        assertNull(presenter.failMessage);
    }

    @Test
    void failTest() {
        UpdateBalanceDataAccessInterface failDataAccess = new UpdateBalanceDataAccessInterface() {
            @Override
            public Player get(String username) {
                return null;
            }

            @Override
            public void save(Player player) {
            }
        };

        class TestPresenter implements UpdateBalanceOutputBoundary {
            String failMessage;

            @Override
            public void prepareSuccessView(UpdateBalanceOutputData outputData) {
                fail("Should not succeed");
            }

            @Override
            public void prepareFailView(String error) {
                this.failMessage = error;
            }
        }

        TestPresenter presenter = new TestPresenter();
        UpdateBalanceInteractor interactor = new UpdateBalanceInteractor(failDataAccess, presenter);

        interactor.execute(new UpdateBalanceInputData("Ghost", 10.0));

        assertNotNull(presenter.failMessage);
        assertEquals("Player not found.", presenter.failMessage);
    }
}
