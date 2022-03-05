package blockchain.model.client;

import blockchain.controller.UserController;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class User extends Client<User, String> {
    private final String name;

    public User(long id, String name) {
        super(id, UserController.getInstance());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        do {
            controller.submit(chat(), this);

            try {
                TimeUnit.MILLISECONDS.sleep(numberGenerator.nextInt(50));
            } catch (InterruptedException e) {
                return;
            }
        } while (!Thread.interrupted());
    }

    private String chat() {
        List<String> messages = List.of(
                "Hello", "How are you?", "How bout' those Dolphins?", "Life is good", "I love Java",
                "I love basketball", "It's sunny outside here", "It's raining here", "What is the meaning of life?",
                "I am getting old!", "Will I ever make decent money?", "I hate mowing my lawn!",
                "Interesting...", "This chat is garbage"
        );

        return messages.get(numberGenerator.nextInt(messages.size()));
    }
}