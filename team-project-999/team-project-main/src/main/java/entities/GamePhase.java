package entities;

/**
 * High-level phase of a round of Blackjack.
 * Use cases will update this as the game progresses.
 */
public enum GamePhase {
    WAITING_FOR_BET,
    DEALING,
    PLAYER_TURN,
    DEALER_TURN,
    ROUND_COMPLETE
}

