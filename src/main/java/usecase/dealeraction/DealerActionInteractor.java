package usecase.dealeraction;
import entities.*;

public class DealerActionInteractor {
    private Game game;
    private Dealer dealer;

    public DealerActionInteractor(Game game){
        this.game = game;
        this.dealer = game.getDealer();
    }

    public void execute(DealerActionInputBoundary dealerActionInputBoundary){
        dealer.play();
        DealerActionOutputBoundary.present();
    }

}
