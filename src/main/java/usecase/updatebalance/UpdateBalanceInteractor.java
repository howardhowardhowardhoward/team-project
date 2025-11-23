package usecase.updatebalance;

import entities.Player;

public class UpdateBalanceInteractor implements UpdateBalanceInputBoundary {

    final UpdateBalanceDataAccessInterface dataAccessObject;
    final UpdateBalanceOutputBoundary outputBoundary;

    public UpdateBalanceInteractor(UpdateBalanceDataAccessInterface dataAccessObject,
                                   UpdateBalanceOutputBoundary outputBoundary) {
        this.dataAccessObject = dataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UpdateBalanceInputData inputData) {
        String username = inputData.getUsername();
        double amount = inputData.getAmount();

        Player player = dataAccessObject.get(username);

        if (player == null) {
            outputBoundary.prepareFailView("Player not found.");
            return;
        }

        player.adjustBalance(amount);

        dataAccessObject.save(player);

        UpdateBalanceOutputData outputData = new UpdateBalanceOutputData(
                player.getBalance()
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}