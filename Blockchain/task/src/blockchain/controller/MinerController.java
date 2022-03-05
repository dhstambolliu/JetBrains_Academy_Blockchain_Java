package blockchain.controller;

import blockchain.model.client.Miner;
import blockchain.service.MinerService;

import java.util.Optional;

public final class MinerController extends Controller<Miner, Long> {
    private static MinerController instance;

    private MinerController() {
        super(MinerService.getInstance());
    }

    public static MinerController getInstance() {
        if (Optional.ofNullable(instance).isEmpty()) {
            synchronized (MinerController.class) {
                if (Optional.ofNullable(instance).isEmpty()) {
                    instance = new MinerController();
                }
            }
        }

        return instance;
    }

    @Override
    public void submit(Long number, Miner miner) {
        service.submit(number, miner);
    }
}