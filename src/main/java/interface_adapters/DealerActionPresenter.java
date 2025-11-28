package interface_adapters;

import usecase.dealeraction.DealerActionOutputBoundary;
import usecase.dealeraction.DealerActionOutputData;

public class DealerActionPresenter implements DealerActionOutputBoundary {
    private final DealerActionViewModel viewModel;

    public DealerActionPresenter(DealerActionViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(DealerActionOutputData outputData) {
        viewModel.setPlayerCardImages(outputData.getPlayerCardImages());
        viewModel.setDealerCardImages(outputData.getDealerCardImages());
        viewModel.setPlayerTotal(outputData.getPlayerTotal());
        viewModel.setDealerTotal(outputData.getDealerTotal());
        viewModel.setDealerBust(outputData.isDealerBust());
        viewModel.setDealerBlackjack(outputData.isDealerBlackjack());
        viewModel.setActionComplete(outputData.isActionComplete());
    }
    @Override
    public void presentError(String message) {
        viewModel.setError(message);
    }
}
