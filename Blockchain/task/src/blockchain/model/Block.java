package blockchain.model;

import blockchain.model.client.Miner;
import blockchain.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long id;
    private final String prevHash;
    private final long timestamp;
    private Deque<String> data;
    private Block.ProofOfWork proofOfWork;

    private Block(long id, String prevHash, long timestamp) {
        this.id = id;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
        data = new ArrayDeque<>();
    }

    public static Block newInstance(long id, String prevHash, long timestamp) {
        return new Block(id, prevHash, timestamp);
    }

    public long getId() {
        return id;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setData(Deque<String> data) {
        this.data = new ArrayDeque<>(data);
    }

    public void setProofOfWork(ProofOfWork proofOfWork) {
        this.proofOfWork = proofOfWork;
    }

    public String hash() {
        return hash(proofOfWork.magicNumber);
    }

    public String hash(long number) {
        return StringUtil.applySha256(getId() + getPrevHash() + getTimestamp() + number);
    }

    private String dataToString() {
        return data.isEmpty() ? "no messages" : "\n" + String.join("\n", data);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(proofOfWork).map(
                        pow -> String.format("Block:%n" +
                                        "Created by miner # %d%n" +
                                        "Id: %d%n" +
                                        "Timestamp: %d%n" +
                                        "Magic number: %d%n" +
                                        "Hash of the previous block: %n" +
                                        "%s%n" +
                                        "Hash of the block: %n" +
                                        "%s%n" +
                                        "Block data: %s%n" +
                                        "Block was generating for %d seconds%n" +
                                        "%s%n",
                                pow.miner.getId(), getId(), getTimestamp(), pow.magicNumber,
                                getPrevHash(), hash(), dataToString(), pow.duration, pow.response))
                .orElse(String.format("Block:%n" +
                                "Id: %d%n" +
                                "Timestamp: %d%n" +
                                "Hash of the previous block: %n" +
                                "%s%n" +
                                "Block data: %s%n",
                        getId(), getTimestamp(), getPrevHash(), dataToString()));
    }

    public static class ProofOfWork implements Serializable {
        private final Miner miner;
        private final long magicNumber;
        private final long duration;
        private final String response;

        public ProofOfWork(long magicNumber, long duration, String response, Miner miner) {
            this.magicNumber = magicNumber;
            this.duration = duration;
            this.response = response;
            this.miner = miner;
        }

    }
}
