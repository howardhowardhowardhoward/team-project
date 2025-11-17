package usecase.dealeraction;

/**
 * Interface for triggering dealer actions.
 *
 * TO BE IMPLEMENTED BY: Howard (DealerAction Use Case)
 *
 * This interface allows PlayerActionInteractor to trigger the dealer's turn
 * when all player hands are complete.
 *
 * NOTE: If Howard doesn't implement this, we can use Dealer.play() directly
 *
 * @author Wentai Zhang (eurekoko) - Interface Definition
 */
public interface DealerController {

    /**
     * Execute the dealer's turn (dealer draws cards until >= 17)
     *
     * @return true if dealer completed successfully
     */
    boolean executeDealerTurn();

    /**
     * Get the dealer's final score after their turn
     *
     * @return the dealer's total points
     */
    int getDealerFinalScore();

    /**
     * Check if dealer busted
     *
     * @return true if dealer exceeded 21
     */
    boolean isDealerBust();
}
