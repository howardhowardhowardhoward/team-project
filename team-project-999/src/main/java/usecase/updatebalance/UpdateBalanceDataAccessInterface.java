package usecase.updatebalance;

import entities.Player;

public interface UpdateBalanceDataAccessInterface {
    Player get(String username);
    void save(Player player);
}