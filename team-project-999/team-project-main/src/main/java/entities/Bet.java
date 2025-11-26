package entities;

import java.util.UUID;

public class Bet {
    private final String betId;
    private double amount;
    private BetType type;
    private BetStatus status;

    public Bet(double amount, BetType type){
        this.betId = UUID.randomUUID().toString();
        this.amount = amount;
        this.type = type;
        this.status = BetStatus.PENDING;
    }

    public String getBetId() {
        return betId;
    }

    public double getAmount() {
        return amount;
    }

    public BetType getType(){
        return  type;
    }
    public BetStatus getStatus(){
        return status;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public void setType(BetType type){
        this.type = type;
    }
    public void setStatus(BetStatus status){
        this.status = status;
    }

    public void markWon(){
        this.status = BetStatus.WON;
    }
    public void markLost(){
        this.status = BetStatus.LOST;
    }
    public void markPush(){
        this.status = BetStatus.PUSH;
    }
}
