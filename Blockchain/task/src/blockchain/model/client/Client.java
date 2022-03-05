package blockchain.model.client;


import blockchain.Submittable;

import java.io.Serializable;
import java.util.Random;

public abstract class Client<T extends Client<?, ?>, S> implements Runnable, Serializable {
    protected final long id;
    protected transient final Submittable<T, S> controller;
    protected transient final Random numberGenerator;

    public Client(long id, Submittable<T, S> controller) {
        this.id = id;
        this.controller = controller;
        numberGenerator = new Random();
    }

    public long getId() {
        return id;
    }
}
