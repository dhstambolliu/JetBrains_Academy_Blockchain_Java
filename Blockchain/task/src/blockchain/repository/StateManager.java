package blockchain.repository;

import blockchain.util.SerializationUtil;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class StateManager {
    private final String filename;
    private final Blockchain blockchain;
    private final Deque<Blockchain.State> history;

    public StateManager(String filename) {
        this.filename = filename;
        history = new ArrayDeque<>();
        blockchain = Blockchain.getInstance();
        loadStateFromFile();
    }

    public void save() {
        history.push(blockchain.getState());
        saveStateToFile();
    }

    public void undo() {
        if (!history.isEmpty()) {
            blockchain.setState(history.pop());
        }
    }

    private void loadStateFromFile() {
        Blockchain.State state = blockchain.getState();

        try {
            Blockchain.State loadedState = (Blockchain.State) SerializationUtil.deserialize(filename);

            if (loadedState.isValidState()) {
                state = loadedState;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No valid blockchain found: new blockchain created.\n");
        }

        history.push(state);
        blockchain.setState(state);
    }

    private void saveStateToFile() {
        try {
            SerializationUtil.serialize(blockchain.getState(), filename);
        } catch (IOException e) {
            System.out.println("Unable to save current state: reverted to previous state.\n");
            undo();
        }
    }
}