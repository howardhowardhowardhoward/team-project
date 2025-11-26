package usecase.updatebalance;

public interface UpdateBalanceOutputBoundary {
    void prepareSuccessView(UpdateBalanceOutputData outputData);
    void prepareFailView(String error);
}