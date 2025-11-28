package interface_adapters.updatebalance;

import usecase.updatebalance.UpdateBalanceOutputBoundary;
import usecase.updatebalance.UpdateBalanceOutputData;

public class UpdateBalancePresenter implements UpdateBalanceOutputBoundary {

    @Override
    public void prepareSuccessView(UpdateBalanceOutputData outputData) {
        System.out.println("Success! New Balance: " + outputData.getNewBalance());
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("Error: " + error);
    }
}
