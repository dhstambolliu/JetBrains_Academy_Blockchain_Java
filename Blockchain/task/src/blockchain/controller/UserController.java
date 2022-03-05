package blockchain.controller;

import blockchain.model.client.User;
import blockchain.service.UserService;

import java.util.Optional;

public final class UserController extends Controller<User, String> {
    private static UserController instance;

    private UserController() {
        super(UserService.getInstance());
    }

    public static UserController getInstance() {
        if (Optional.ofNullable(instance).isEmpty()) {
            synchronized (UserController.class) {
                if (Optional.ofNullable(instance).isEmpty()) {
                    instance = new UserController();
                }
            }
        }

        return instance;
    }

    @Override
    public void submit(String message, User user) {
        service.submit(message, user);
    }
}