package interface_adapter.dealeraction;

import usecase.DealerAction.DealerActionOutputBoundary;
import usecase.DealerAction.DealerActionOutputData;

/**
 * Presenter for the DealerAction use case.
 * Formats the dealer's final state and game result for the console output.
 */
public class DealerActionPresenter implements DealerActionOutputBoundary {
    
    @Override
    public void present(DealerActionOutputData outputData) {
        System.out.println("\n--- DEALER'S TURN COMPLETE ---");
        System.out.println("Dealer Cards: " + outputData.getDealerCards());
        System.out.println("Dealer Total: " + outputData.getDealerTotal());
        System.out.println("Dealer Bust: " + outputData.isDealerBust());
        System.out.println("------------------------------");
        System.out.println(outputData.getGameResultSummary());
        System.out.printf("New Balance: %.2f\n", outputData.getFinalBalance());
        System.out.println("------------------------------\n");
    }
}