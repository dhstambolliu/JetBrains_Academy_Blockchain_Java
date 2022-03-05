package blockchain.service;

import blockchain.model.client.User;
import blockchain.repository.Blockchain;

import java.util.Optional;

public final class UserService extends Service<User, String> {
    private static UserService instance;

    public static UserService getInstance() {
        if (Optional.ofNullable(instance).isEmpty()) {
            synchronized (UserService.class) {
                if (Optional.ofNullable(instance).isEmpty()) {
                    instance = new UserService();
                }
            }
        }

        return instance;
    }

    @Override
    public void submit(String message, User user) {
        synchronized (Blockchain.class) {
            repo.postMessage(message, user);
        }
    }
}