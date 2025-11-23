package usecase.DealerAction;

public class DealerActionController {
    private final DealerActionInputBoundary interactor;

    public DealerActionController(DealerActionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        DealerActionRequestModel req = new DealerActionRequestModel();
        interactor.execute(req);
    }
}