package blockchain;

import blockchain.model.client.Client;

public interface Submittable<T extends Client<?, ?>, S> {

    void submit(S s, T client);
}