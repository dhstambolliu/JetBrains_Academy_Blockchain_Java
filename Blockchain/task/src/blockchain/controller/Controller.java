package blockchain.controller;

import blockchain.Submittable;
import blockchain.model.client.Client;
import blockchain.service.Service;

public abstract class Controller<T extends Client<?, ?>, S> implements Submittable<T, S> {
    protected final Service<T, S> service;

    public Controller(Service<T, S> service) {
        this.service = service;
    }
}