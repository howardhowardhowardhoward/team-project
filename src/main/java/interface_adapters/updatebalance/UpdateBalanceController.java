package interface_adapters.updatebalance;

import usecase.updatebalance.UpdateBalanceInputBoundary;
import usecase.updatebalance.UpdateBalanceInputData;

/**
 * Controller for the UpdateBalance use case.
 * This class acts as an adapter, receiving input from the UI (View)
 * and passing it to the Interactor (Use Case).
 */
public class UpdateBalanceController {

    final UpdateBalanceInputBoundary updateBalanceUseCase;

    /**
     * Constructor.
     * @param updateBalanceUseCase The use case interactor to call.
     */
    public UpdateBalanceController(UpdateBalanceInputBoundary updateBalanceUseCase) {
        this.updateBalanceUseCase = updateBalanceUseCase;
    }

    /**
     * Executes the Update Balance use case.
     * This method is usually called by the UI (e.g., when a button is clicked).
     *
     * @param username The ID/Username of the player.
     * @param amount   The amount to add (positive) or deduct (negative).
     */
    public void execute(String username, double amount) {
        // 1. Wrap the raw data from the UI into an Input Data object
        UpdateBalanceInputData inputData = new UpdateBalanceInputData(username, amount);

        // 2. Pass the data to the Use Case Interactor to execute logic
        updateBalanceUseCase.execute(inputData);
    }
}
