package usecase.updatebalance;

import entities.Player;

/**
 * The Interactor for the UpdateBalance use case.
 * This class contains the core business logic and implements the InputBoundary.
 */
public class UpdateBalanceInteractor implements UpdateBalanceInputBoundary {

    // Dependencies required for the business logic:
    // 1. Data Access: To retrieve and save player data.
    final UpdateBalanceDataAccessInterface dataAccessObject;
    // 2. Output Boundary: To communicate the result back to the Presenter.
    final UpdateBalanceOutputBoundary outputBoundary;

    /**
     * Constructor.
     * @param dataAccessInterface The interface for accessing data (Dependency Injection).
     * @param outputBoundary The interface for presenting output (Dependency Injection).
     */
    public UpdateBalanceInteractor(UpdateBalanceDataAccessInterface dataAccessInterface,
                                   UpdateBalanceOutputBoundary outputBoundary) {
        this.dataAccessObject = dataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the business logic for updating the balance.
     * @param inputData The input data containing the player ID and amount.
     */
    @Override
    public void execute(UpdateBalanceInputData inputData) {
        // 1. Extract information from the Input Data
        // FIXED: Changed from getPlayerId() to getUsername() to match your InputData class
        String username = inputData.getUsername();
        double amount = inputData.getAmount();

        // 2. Retrieve the Player entity from the database using the DAO
        Player player = dataAccessObject.get(username);

        // Safety check: Handle the case where the player is not found
        if (player == null) {
            outputBoundary.prepareFailView("Player not found with username: " + username);
            return;
        }

        // 3. Core Business Logic: Adjust the player's balance
        player.adjustBalance(amount);

        // 4. Save the changes back to the database
        // This is crucial; otherwise, the balance update won't persist.
        dataAccessObject.save(player);

        // 5. Prepare the Output Data (Response Model)
        UpdateBalanceOutputData outputData = new UpdateBalanceOutputData(
                player.getUsername(),
                player.getBalance()
        );

        // 6. Notify the Presenter to update the view with the success data
        outputBoundary.prepareSuccessView(outputData);
    }
}