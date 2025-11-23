package interface_adapter.dealeraction;

import usecase.dealeraction.DealerActionInputBoundary;
import usecase.dealeraction.DealerActionInputData;

/**
 * Controller for the DealerAction use case.
 * Routes the request to execute the dealer's turn to the Interactor.
 */
public class DealerActionController {
    
    private final DealerActionInputBoundary interactor;
    private static final String PLAYER_ID = "mainPlayer"; 

    public DealerActionController(DealerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the dealer's turn.
     */
    public void executeDealerTurn() {
        // Create input data object (currently only contains player ID)
        DealerActionInputData inputData = new DealerActionInputData(PLAYER_ID);
        interactor.execute(inputData);
    }
}