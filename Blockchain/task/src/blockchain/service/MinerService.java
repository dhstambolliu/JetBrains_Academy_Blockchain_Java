package blockchain.service;

import blockchain.model.client.Miner;
import blockchain.repository.Blockchain;

import java.util.Optional;

public final class MinerService extends Service<Miner, Long> {
    private static MinerService instance;

    public static MinerService getInstance() {
        if (Optional.ofNullable(instance).isEmpty()) {
            synchronized (MinerService.class) {
                if (Optional.ofNullable(instance).isEmpty()) {
                    instance = new MinerService();
                }
            }
        }

        return instance;
    }

    public void submit(Long number, Miner miner) {
        if (repo.getCurrentBlock().hash(number).startsWith(repo.prefix())) {
            synchronized (Blockchain.class) {
                if (repo.getCurrentBlock().hash(number).startsWith(repo.prefix())) {
                    repo.createBlock(number, miner);
                }
            }
        }
    }
}