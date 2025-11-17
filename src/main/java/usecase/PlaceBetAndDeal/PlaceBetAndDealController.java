package usecase.PlaceBetAndDeal;

import entities.Bet;

public class PlaceBetAndDealController {
    private PlaceBetAndDealInteractor interactor;

    public PlaceBetAndDealController(PlaceBetAndDealInteractor interactor) {
        this.interactor = interactor;
    }

    public void placeBetAndDeal(double amount) {
        try {
            Bet bet = interactor.execute(amount);
            System.out.println("Bet placed: $" + bet.getAmount());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid bet: " + e.getMessage());
        }
    }
}
