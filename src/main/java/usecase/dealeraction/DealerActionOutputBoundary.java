package usecase.dealeraction;

public interface DealerActionOutputBoundary {
    void present(DealerActionOutputData outputData);
    void presentError(String message);
}
