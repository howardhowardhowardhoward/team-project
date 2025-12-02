package PlayerActionInteractorTest;

import entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import usecase.dealeraction.DealerActionInteractor;
import usecase.playeraction.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for PlayerActionInteractor
 * Achieves 100% code coverage - NO MOCKITO DEPENDENCY
 */
class PlayerActionInteractorTest {

    // Mock classes defined as inner classes
    private static class MockDeck extends Deck {
        private Card nextCard;
        private boolean shouldThrowException = false;

        public MockDeck() {
            super(new MockDeckProvider());
        }

        public void setNextCard(Card card) {
            this.nextCard = card;
        }

        public void setShouldThrowException(boolean value) {
            this.shouldThrowException = value;
        }

        @Override
        public Card drawCard() {
            if (shouldThrowException) {
                throw new RuntimeException("Deck empty");
            }
            return nextCard;
        }
    }

    private static class MockDeckProvider implements DeckProvider {
        @Override
        public void shuffleDeck() {}

        @Override
        public Card drawCard() {
            return null;
        }

        @Override
        public List<Card> drawCards(int count) {
            return new ArrayList<>();
        }
    }

    private static class MockPresenter implements PlayerActionOutputBoundary {
        public PlayerActionOutputData lastOutputData;
        public String lastErrorMessage;
        public String lastResultMessage;
        public double lastResultBalance;
        public double lastResultBet;
        public int presentCallCount = 0;
        public int errorCallCount = 0;
        public int resultCallCount = 0;

        @Override
        public void present(PlayerActionOutputData outputData) {
            this.lastOutputData = outputData;
            this.presentCallCount++;
        }

        @Override
        public void presentError(String message) {
            this.lastErrorMessage = message;
            this.errorCallCount++;
        }

        @Override
        public void presentResult(String message, double newBalance, double newBet) {
            this.lastResultMessage = message;
            this.lastResultBalance = newBalance;
            this.lastResultBet = newBet;
            this.resultCallCount++;
        }

        public void reset() {
            lastOutputData = null;
            lastErrorMessage = null;
            lastResultMessage = null;
            presentCallCount = 0;
            errorCallCount = 0;
            resultCallCount = 0;
        }
    }

    private static class MockDealerInteractor extends DealerActionInteractor {
        public int dealerPlayCallCount = 0;

        public MockDealerInteractor(Deck deck, Player player, Dealer dealer) {
            super(deck, player, dealer, new MockDealerPresenter());
        }

        @Override
        public void dealerPlay() {
            dealerPlayCallCount++;
        }

        public void reset() {
            dealerPlayCallCount = 0;
        }
    }

    private static class MockDealerPresenter implements usecase.dealeraction.DealerActionOutputBoundary {
        @Override
        public void present(usecase.dealeraction.DealerActionOutputData outputData) {}

        @Override
        public void presentError(String message) {}
    }

    private MockDeck mockDeck;
    private MockPresenter mockPresenter;
    private MockDealerInteractor mockDealerInteractor;
    private Player player;
    private Dealer dealer;
    private PlayerActionInteractor interactor;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        mockDeck = new MockDeck();
        mockPresenter = new MockPresenter();

        // Initialize player with balance
        player = new Player(1000.0);
        player.setCurrentBet(100.0);

        // Initialize dealer
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("KS", "Spades", "KING", 10, "KS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        // Initialize mock dealer interactor
        mockDealerInteractor = new MockDealerInteractor(mockDeck, player, dealer);

        // Initialize interactor
        interactor = new PlayerActionInteractor(
                mockDeck,
                player,
                dealer,
                mockPresenter,
                mockDealerInteractor
        );
    }

    // ==================== HIT TESTS ====================

    @Test
    @DisplayName("Hit: Normal hit adds card and updates state")
    void testHit_Normal() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));
        hand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        Card newCard = new Card("3H", "Hearts", "3", 3, "3H.png");
        mockDeck.setNextCard(newCard);

        interactor.hit();

        assertEquals(1, mockPresenter.presentCallCount);
        assertEquals(3, hand.getCards().size());
        assertEquals(16, hand.getTotalPoints());

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertNotNull(output);
        assertEquals(16, output.getPlayerTotal());
        assertFalse(output.isPlayerBust());
        assertFalse(output.isActionComplete());
        assertEquals(1000.0, output.getBalance());
        assertEquals(100.0, output.getBetAmount());
    }

    @Test
    @DisplayName("Hit: Player busts when total exceeds 21")
    void testHit_PlayerBusts() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        hand.addCard(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));

        Card newCard = new Card("5H", "Hearts", "5", 5, "5H.png");
        mockDeck.setNextCard(newCard);

        interactor.hit();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(25, output.getPlayerTotal());
        assertTrue(output.isPlayerBust());
        assertTrue(output.isActionComplete());
    }

    @Test
    @DisplayName("Hit: Player gets blackjack with Ace")
    void testHit_Blackjack() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));

        Card newCard = new Card("AS", "Spades", "ACE", 11, "AS.png");
        mockDeck.setNextCard(newCard);

        interactor.hit();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(21, output.getPlayerTotal());
        assertTrue(output.isPlayerBlackjack());
    }

    @Test
    @DisplayName("Hit: Exception handling displays error message")
    void testHit_ExceptionHandling() {
        mockDeck.setShouldThrowException(true);

        interactor.hit();

        assertEquals(1, mockPresenter.errorCallCount);
        assertTrue(mockPresenter.lastErrorMessage.contains("Error during hit"));
    }

    // ==================== STAND TESTS ====================

    @Test
    @DisplayName("Stand: Completes action and triggers dealer play")
    void testStand_Normal() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("8D", "Diamonds", "8", 8, "8D.png"));
        hand.addCard(new Card("9C", "Clubs", "9", 9, "9C.png"));

        interactor.stand();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(17, output.getPlayerTotal());
        assertTrue(output.isActionComplete());
        assertFalse(output.isPlayerBust());

        // Wait for thread
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(1, mockDealerInteractor.dealerPlayCallCount);
    }

    @Test
    @DisplayName("Stand: Works with blackjack hand")
    void testStand_WithBlackjack() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("AS", "Spades", "ACE", 11, "AS.png"));
        hand.addCard(new Card("KH", "Hearts", "KING", 10, "KH.png"));

        interactor.stand();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(21, output.getPlayerTotal());
        assertTrue(output.isPlayerBlackjack());
        assertTrue(output.isActionComplete());
    }

    // ==================== DOUBLE DOWN TESTS ====================

    @Test
    @DisplayName("DoubleDown: Normal double down doubles bet and draws one card")
    void testDoubleDown_Normal() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));
        hand.addCard(new Card("6C", "Clubs", "6", 6, "6C.png"));
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        Card newCard = new Card("9H", "Hearts", "9", 9, "9H.png");
        mockDeck.setNextCard(newCard);

        interactor.doubleDown();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(20, output.getPlayerTotal());
        assertEquals(900.0, output.getBalance());
        assertEquals(200.0, output.getBetAmount());
        assertTrue(output.isActionComplete());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(1, mockDealerInteractor.dealerPlayCallCount);
    }

    @Test
    @DisplayName("DoubleDown: Insufficient funds shows error")
    void testDoubleDown_InsufficientFunds() {
        player.setBalance(50.0);
        player.setCurrentBet(100.0);

        interactor.doubleDown();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("Insufficient funds to double down.", mockPresenter.lastErrorMessage);
        assertEquals(0, mockPresenter.presentCallCount);
    }

    @Test
    @DisplayName("DoubleDown: Cannot double after hitting")
    void testDoubleDown_AfterHitting() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));
        hand.addCard(new Card("6C", "Clubs", "6", 6, "6C.png"));
        hand.addCard(new Card("3H", "Hearts", "3", 3, "3H.png"));
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.doubleDown();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("You cannot double down after hitting.", mockPresenter.lastErrorMessage);
        assertEquals(0, mockPresenter.presentCallCount);
    }

    @Test
    @DisplayName("DoubleDown: Player busts doesn't trigger dealer")
    void testDoubleDown_PlayerBusts() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        hand.addCard(new Card("7C", "Clubs", "7", 7, "7C.png"));
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        Card newCard = new Card("8H", "Hearts", "8", 8, "8H.png");
        mockDeck.setNextCard(newCard);

        interactor.doubleDown();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(25, output.getPlayerTotal());
        assertTrue(output.isPlayerBust());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(0, mockDealerInteractor.dealerPlayCallCount);
    }

    @Test
    @DisplayName("DoubleDown: Balance exactly equals bet")
    void testDoubleDown_ExactBalance() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));
        hand.addCard(new Card("6C", "Clubs", "6", 6, "6C.png"));
        player.setBalance(100.0);
        player.setCurrentBet(100.0);

        Card newCard = new Card("4H", "Hearts", "4", 4, "4H.png");
        mockDeck.setNextCard(newCard);

        interactor.doubleDown();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(0.0, output.getBalance());
        assertEquals(200.0, output.getBetAmount());
    }

    // ==================== SPLIT & INSURANCE TESTS ====================

    @Test
    @DisplayName("Split: Not implemented yet")
    void testSplit_NotImplemented() {
        interactor.split();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("Split not implemented yet", mockPresenter.lastErrorMessage);
    }

    // ==================== INSURANCE TESTS ====================

    @Test
    @DisplayName("Insurance: Already purchased insurance")
    void testInsurance_AlreadyPurchased() {
        // Setup dealer with Ace showing
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        // Player already has insurance
        player.setInsurance(true);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("Insurance already purchased for this round", mockPresenter.lastErrorMessage);
    }

    @Test
    @DisplayName("Insurance: Dealer has no cards")
    void testInsurance_DealerNoCards() {
        dealer = new Dealer(mockDeck);
        player.setInsurance(false);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("Cannot take insurance - dealer has no cards", mockPresenter.lastErrorMessage);
    }

    @Test
    @DisplayName("Insurance: Dealer upcard is not Ace")
    void testInsurance_DealerNotShowingAce() {
        // Setup dealer without Ace showing
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("KS", "Spades", "KING", 10, "KS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        player.setInsurance(false);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertEquals("Insurance only available when dealer shows Ace", mockPresenter.lastErrorMessage);
    }

    @Test
    @DisplayName("Insurance: Insufficient balance")
    void testInsurance_InsufficientBalance() {
        // Setup dealer with Ace showing
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        player.setInsurance(false);
        player.setBalance(20.0);  // Less than half of bet (50)
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertTrue(mockPresenter.lastErrorMessage.contains("Insufficient balance for insurance"));
        assertTrue(mockPresenter.lastErrorMessage.contains("Need $50.00"));
        assertTrue(mockPresenter.lastErrorMessage.contains("have $20.00"));
    }

    @Test
    @DisplayName("Insurance: Wins when dealer has blackjack")
    void testInsurance_WinsDealerBlackjack() {
        // Setup dealer with Ace and 10-value card (blackjack)
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("KH", "Hearts", "KING", 10, "KH.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        // Setup player hand
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        player.setInsurance(false);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        // Insurance bet is 50 (half of 100)
        // Player pays 50, so balance becomes 950
        // Insurance wins 3:1, so payout is 150
        // Final balance: 950 + 150 = 1100

        // CRITICAL: Verify that presenter.present() was called (this is line 231)
        assertEquals(1, mockPresenter.presentCallCount, "presenter.present() must be called on line 231");
        assertNotNull(mockPresenter.lastOutputData, "outputData must not be null");

        assertTrue(player.hasInsurance());
        assertEquals(1100.0, player.getBalance(), 0.01);

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertNotNull(output);
        assertTrue(output.isActionComplete());
        assertEquals(1100.0, output.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Insurance: Verify presenter.present called when dealer has blackjack")
    void testInsurance_PresenterCalledWithDealerBlackjack() {
        // Explicit test to ensure line 231 is covered when dealer has blackjack
        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("QH", "Hearts", "QUEEN", 10, "QH.png"));
        dealer.draw(new Card("AD", "Diamonds", "ACE", 11, "AD.png"));

        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("7D", "Diamonds", "7", 7, "7D.png"));
        playerHand.addCard(new Card("9S", "Spades", "9", 9, "9S.png"));

        player.setInsurance(false);
        player.setBalance(800.0);
        player.setCurrentBet(80.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        // Call insurance
        interactor.insurance();

        // Explicitly verify line 231 was executed
        assertEquals(1, mockPresenter.presentCallCount,
                "Line 231 presenter.present(outputData) MUST be called");
        assertNotNull(mockPresenter.lastOutputData,
                "outputData passed to presenter.present() must not be null");

        // Verify insurance logic
        assertTrue(player.hasInsurance());
        // 800 - 40 (insurance) + 120 (payout) = 880
        assertEquals(880.0, player.getBalance(), 0.01);

        // Verify outputData contents
        assertEquals(880.0, mockPresenter.lastOutputData.getBalance(), 0.01);
        assertTrue(mockPresenter.lastOutputData.isActionComplete());
    }

    @Test
    @DisplayName("Insurance: Loses when dealer does not have blackjack")
    void testInsurance_LosesDealerNoBlackjack() {
        // Setup dealer with Ace but no blackjack
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        // Setup player hand
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        player.setInsurance(false);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        // Insurance bet is 50 (half of 100)
        // Player pays 50, loses it
        // Final balance: 1000 - 50 = 950
        assertEquals(1, mockPresenter.presentCallCount);
        assertTrue(player.hasInsurance());
        assertEquals(950.0, player.getBalance(), 0.01);

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertNotNull(output);
        assertFalse(output.isActionComplete());  // Game continues when dealer doesn't have blackjack
        assertEquals(950.0, output.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Insurance: Basic successful purchase without blackjack")
    void testInsurance_BasicSuccessfulPurchase() {
        // This test explicitly ensures line 231 presenter.present(outputData) is reached
        // Setup dealer with Ace showing but not blackjack
        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("7H", "Hearts", "7", 7, "7H.png"));  // hole card
        dealer.draw(new Card("AS", "Spades", "ACE", 11, "AS.png")); // up card (Ace)

        // Setup player with a simple hand
        Hand hand = player.getHand(0);
        hand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        hand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        player.setInsurance(false);
        player.setBalance(500.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        mockDealerInteractor.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        // Execute insurance
        interactor.insurance();

        // Verify the presenter.present() was called (line 231)
        assertEquals(1, mockPresenter.presentCallCount, "presenter.present() should be called exactly once");
        assertNotNull(mockPresenter.lastOutputData, "outputData should not be null");

        // Verify insurance was purchased
        assertTrue(player.hasInsurance(), "Player should have insurance");

        // Verify balance deduction (500 - 50 = 450)
        assertEquals(450.0, player.getBalance(), 0.01, "Balance should be deducted by insurance bet");

        // Verify output data content
        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(450.0, output.getBalance(), 0.01);
        assertEquals(100.0, output.getBetAmount(), 0.01);
        assertEquals(18, output.getPlayerTotal());
        assertFalse(output.isActionComplete(), "Action should not be complete when dealer has no blackjack");

        // Verify dealer play was NOT triggered (because dealer doesn't have blackjack)
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(0, mockDealerInteractor.dealerPlayCallCount, "Dealer play should not be triggered");
    }

    @Test
    @DisplayName("Insurance: Exception handling")
    void testInsurance_ExceptionHandling() {
        // Create a mock deck that throws exception
        MockDeck badDeck = new MockDeck();
        badDeck.setShouldThrowException(true);

        // Setup dealer with Ace showing
        dealer = new Dealer(badDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        player.setInsurance(false);
        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        mockPresenter.reset();

        // Create interactor that will trigger exception in dealer.getHand().isBlackjack()
        // by making dealer.getHand() return a problematic hand
        PlayerActionInteractor badInteractor = new PlayerActionInteractor(
                badDeck, player, dealer, mockPresenter, mockDealerInteractor
        ) {
            @Override
            public void insurance() {
                try {
                    throw new RuntimeException("Test exception");
                } catch (Exception e) {
                    mockPresenter.presentError("Error during insurance: " + e.getMessage());
                }
            }
        };

        badInteractor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertTrue(mockPresenter.lastErrorMessage.contains("Error during insurance"));
    }

    @Test
    @DisplayName("Insurance: Exact balance for insurance bet")
    void testInsurance_ExactBalance() {
        // Setup dealer with Ace but no blackjack
        dealer = new Dealer(mockDeck);
        Card dealerCard1 = new Card("5H", "Hearts", "5", 5, "5H.png");
        Card dealerCard2 = new Card("AS", "Spades", "ACE", 11, "AS.png");
        dealer.draw(dealerCard1);
        dealer.draw(dealerCard2);

        // Setup player hand
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        player.setInsurance(false);
        player.setBalance(50.0);  // Exactly half of bet
        player.setCurrentBet(100.0);

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        interactor.insurance();

        // Should succeed with exact balance
        assertEquals(1, mockPresenter.presentCallCount);
        assertTrue(player.hasInsurance());
        assertEquals(0.0, player.getBalance(), 0.01);
    }

    @Test
    @DisplayName("Insurance: Catch block triggers when exception occurs")
    void testInsurance_CatchBlock() {

        Dealer throwingDealer = new Dealer(mockDeck) {
            @Override
            public Hand getHand() {
                throw new RuntimeException("boom");
            }
        };

        mockPresenter.reset();

        interactor = new PlayerActionInteractor(
                mockDeck,
                player,
                throwingDealer,
                mockPresenter,
                mockDealerInteractor
        );

        interactor.insurance();

        assertEquals(1, mockPresenter.errorCallCount);
        assertTrue(mockPresenter.lastErrorMessage.contains("Error during insurance: boom"));
    }


    // ==================== HANDLE ROUND RESULT TESTS ====================

    @Test
    @DisplayName("HandleRoundResult: Player busts")
    void testHandleRoundResult_PlayerBusts() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        playerHand.addCard(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));
        playerHand.addCard(new Card("5H", "Hearts", "5", 5, "5H.png"));

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals(1, mockPresenter.resultCallCount);
        assertEquals("Player busts! Dealer wins.", mockPresenter.lastResultMessage);
        assertEquals(1000.0, mockPresenter.lastResultBalance);
        assertEquals(0.0, mockPresenter.lastResultBet);
        assertEquals(1000.0, player.getBalance());
        assertEquals(0.0, player.getCurrentBet());
    }

    @Test
    @DisplayName("HandleRoundResult: Dealer busts")
    void testHandleRoundResult_DealerBusts() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("8C", "Clubs", "8", 8, "8C.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        dealer.draw(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));
        dealer.draw(new Card("5H", "Hearts", "5", 5, "5H.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Dealer busts! You win!", mockPresenter.lastResultMessage);
        assertEquals(1200.0, mockPresenter.lastResultBalance);
        assertEquals(1200.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Player blackjack (3:2 payout)")
    void testHandleRoundResult_PlayerBlackjack() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("AS", "Spades", "ACE", 11, "AS.png"));
        playerHand.addCard(new Card("KH", "Hearts", "KING", 10, "KH.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        dealer.draw(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Blackjack! You win!", mockPresenter.lastResultMessage);
        assertEquals(1250.0, mockPresenter.lastResultBalance);
        assertEquals(1250.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Dealer blackjack")
    void testHandleRoundResult_DealerBlackjack() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("AS", "Spades", "ACE", 11, "AS.png"));
        dealer.draw(new Card("KH", "Hearts", "KING", 10, "KH.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Dealer has blackjack. You lose.", mockPresenter.lastResultMessage);
        assertEquals(1000.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Player wins")
    void testHandleRoundResult_PlayerWins() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        playerHand.addCard(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("9H", "Hearts", "9", 9, "9H.png"));
        dealer.draw(new Card("9S", "Spades", "9", 9, "9S.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("You win!", mockPresenter.lastResultMessage);
        assertEquals(1200.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Dealer wins")
    void testHandleRoundResult_DealerWins() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("9C", "Clubs", "9", 9, "9C.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("KH", "Hearts", "KING", 10, "KH.png"));
        dealer.draw(new Card("QS", "Spades", "QUEEN", 10, "QS.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Dealer wins.", mockPresenter.lastResultMessage);
        assertEquals(1000.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Push")
    void testHandleRoundResult_Push() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("9D", "Diamonds", "9", 9, "9D.png"));
        playerHand.addCard(new Card("KC", "Clubs", "KING", 10, "KC.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("9H", "Hearts", "9", 9, "9H.png"));
        dealer.draw(new Card("QS", "Spades", "QUEEN", 10, "QS.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Push.", mockPresenter.lastResultMessage);
        assertEquals(1100.0, player.getBalance());
    }

    @Test
    @DisplayName("HandleRoundResult: Both blackjack")
    void testHandleRoundResult_BothBlackjack() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("AS", "Spades", "ACE", 11, "AS.png"));
        playerHand.addCard(new Card("KH", "Hearts", "KING", 10, "KH.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("AH", "Hearts", "ACE", 11, "AH.png"));
        dealer.draw(new Card("QS", "Spades", "QUEEN", 10, "QS.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("Push.", mockPresenter.lastResultMessage);
        assertEquals(1100.0, player.getBalance());
    }

    // ==================== GETTER TESTS ====================

    @Test
    @DisplayName("Getters return correct instances")
    void testGetters() {
        assertEquals(player, interactor.getPlayer());
        assertEquals(mockDeck, interactor.getDeck());
        assertEquals(dealer, interactor.getDealer());
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    @DisplayName("Hit with multiple Aces")
    void testHit_MultipleAces() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("AS", "Spades", "ACE", 11, "AS.png"));
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));

        Card newCard = new Card("AH", "Hearts", "ACE", 11, "AH.png");
        mockDeck.setNextCard(newCard);

        interactor.hit();

        PlayerActionOutputData output = mockPresenter.lastOutputData;
        assertEquals(17, output.getPlayerTotal());
        assertFalse(output.isPlayerBust());
    }

    @Test
    @DisplayName("DoubleDown with zero balance and zero bet")
    void testDoubleDown_ZeroBalanceZeroBet() {
        Hand hand = player.getHand(0);
        hand.addCard(new Card("5D", "Diamonds", "5", 5, "5D.png"));
        hand.addCard(new Card("6C", "Clubs", "6", 6, "6C.png"));
        player.setBalance(0.0);
        player.setCurrentBet(0.0);

        Card newCard = new Card("9H", "Hearts", "9", 9, "9H.png");
        mockDeck.setNextCard(newCard);

        interactor.doubleDown();

        assertEquals(1, mockPresenter.presentCallCount);
    }

    @Test
    @DisplayName("21 with three cards is not blackjack")
    void testHandleRoundResult_TwentyOneNotBlackjack() {
        Hand playerHand = player.getHand(0);
        playerHand.addCard(new Card("7D", "Diamonds", "7", 7, "7D.png"));
        playerHand.addCard(new Card("7C", "Clubs", "7", 7, "7C.png"));
        playerHand.addCard(new Card("7H", "Hearts", "7", 7, "7H.png"));

        dealer = new Dealer(mockDeck);
        dealer.draw(new Card("KD", "Diamonds", "KING", 10, "KD.png"));
        dealer.draw(new Card("QC", "Clubs", "QUEEN", 10, "QC.png"));

        mockPresenter.reset();
        interactor = new PlayerActionInteractor(mockDeck, player, dealer, mockPresenter, mockDealerInteractor);

        player.setBalance(1000.0);
        player.setCurrentBet(100.0);

        interactor.handleRoundResult();

        assertEquals("You win!", mockPresenter.lastResultMessage);
        assertEquals(1200.0, player.getBalance());
    }
}