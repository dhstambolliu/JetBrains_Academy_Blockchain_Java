package blockchain.model.client;

import blockchain.controller.MinerController;

public final class Miner extends Client<Miner, Long> {

    public Miner(int id) {
        super(id, MinerController.getInstance());
    }

    @Override
    public void run() {
        do {
            controller.submit(numberGenerator.nextLong(), this);
        } while (!Thread.interrupted());
    }
}