package blockchain.repository;

import blockchain.model.Block;
import blockchain.model.client.Miner;
import blockchain.model.client.User;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Optional;
import java.util.stream.Collectors;

public class Blockchain {
    private static final String INITIAL_HASH = "0";
    private static final long INITIAL_ID = 1L;
    private static Blockchain instance;
    private static StateManager stateManager;

    private Deque<Block> blocks;
    private Block currentBlock;
    private Deque<String> messageBuffer;
    private int n;

    private Blockchain() {
        blocks = new ArrayDeque<>();
        currentBlock = generateBlock();
        messageBuffer = new ArrayDeque<>();
    }

    public static Blockchain getInstance() {
        if (Optional.ofNullable(instance).isEmpty()) {
            synchronized (Blockchain.class) {
                if (Optional.ofNullable(instance).isEmpty()) {
                    instance = new Blockchain();
                    stateManager = new StateManager("blockchain.sav");
                }
            }
        }

        return instance;
    }

    public int size() {
        return blocks.size();
    }

    private Block generateBlock() {
        return Optional.ofNullable(blocks.peekLast())
                .map(lastBlock -> Block.newInstance(blocks.size() + 1L, lastBlock.hash(), new Date().getTime()))
                .orElse(Block.newInstance(INITIAL_ID, INITIAL_HASH, new Date().getTime()));
    }

    public void createBlock(long number, Miner miner) {
        long duration = (new Date().getTime() - currentBlock.getTimestamp()) / 1000;
        Block.ProofOfWork proofOfWork = new Block.ProofOfWork(number, duration, updateN(duration), miner);
        currentBlock.setProofOfWork(proofOfWork);

        if (blocks.offerLast(currentBlock)) {
            currentBlock = generateBlock();
            currentBlock.setData(messageBuffer);
            messageBuffer.clear();
            stateManager.save();
        }
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public String prefix() {
        return INITIAL_HASH.repeat(n);
    }

    private String updateN(long duration) {
        return duration < 10 ? "N was increased to " + ++n
                : duration > 60 && n > 0 ? "N was decreased to " + --n
                : "N stays the same";
    }

    public synchronized void postMessage(String message, User user) {
        messageBuffer.push(user.getName() + ": " + message);
    }

    public String toStringLast(int n) {
        return getState().isValidState() ? blocks.stream()
                .skip(Math.max(0, blocks.size() - n))
                .map(Block::toString)
                .collect(Collectors.joining("\n")) : "Invalid Blockchain.";
    }

    @Override
    public String toString() {
        return toStringLast(blocks.size());
    }

    public State getState() {
        return new State(blocks, messageBuffer);
    }

    public void setState(State state) {
        blocks = state.blocks;
        messageBuffer = state.messageBuffer;
        currentBlock = generateBlock();
    }

    static class State implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Deque<Block> blocks;
        private final Deque<String> messageBuffer;

        public State(Deque<Block> blocks, Deque<String> messageBuffer) {
            this.blocks = new ArrayDeque<>(blocks);
            this.messageBuffer = new ArrayDeque<>(messageBuffer);
        }

        public boolean isValidState() {
            String prevHash = INITIAL_HASH;

            for (Block block : blocks) {
                if (!block.getPrevHash().equals(prevHash)) {
                    return false;
                }

                prevHash = block.hash();
            }

            return true;
        }
    }
}