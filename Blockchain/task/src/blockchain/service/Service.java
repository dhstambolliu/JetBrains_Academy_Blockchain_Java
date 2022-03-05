package blockchain.service;

import blockchain.Submittable;
import blockchain.model.client.Client;
import blockchain.repository.Blockchain;

public abstract class Service<T extends Client<?, ?>, S> implements Submittable<T, S> {
    protected final Blockchain repo;

    public Service() {
        this.repo = Blockchain.getInstance();
    }
}