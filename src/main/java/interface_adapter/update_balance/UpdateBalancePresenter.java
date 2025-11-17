package interface_adapter.update_balance;

import usecase.updatebalance.UpdateBalanceOutputBoundary;
import usecase.updatebalance.UpdateBalanceOutputData;

/**
 * Presenter for the UpdateBalance use case.
 * This class receives the output from the Interactor and formats it
 * for the View (UI) to display.
 */
public class UpdateBalancePresenter implements UpdateBalanceOutputBoundary {

    @Override
    public void prepareSuccessView(UpdateBalanceOutputData outputData) {
        // TODO: In the future, this will update a ViewModel or ViewManager.
        // For now, we print to the console to verify the flow works.
        System.out.println("Success! New Balance for " + outputData.getUsername() + ": " + outputData.getNewBalance());
    }

    @Override
    public void prepareFailView(String error) {
        // TODO: In the future, this will trigger an error popup in the UI.
        System.out.println("Error: " + error);
    }
}