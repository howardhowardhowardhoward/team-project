package interface_adapters.dealeraction;

import usecase.dealeraction.DealerActionInputBoundary;

public class DealerActionController {
    private final DealerActionInputBoundary interactor;

    public DealerActionController(DealerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void dealerPlay() {
        interactor.dealerPlay();
    }
}
