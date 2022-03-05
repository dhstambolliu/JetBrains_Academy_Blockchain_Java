package blockchain;

import blockchain.model.client.Miner;
import blockchain.model.client.User;
import blockchain.repository.Blockchain;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        final Blockchain blockchain = Blockchain.getInstance();
        int threadCount = Runtime.getRuntime().availableProcessors();

        List<String> names = List.of("Evelio", "Charles", "Armando", "Aramys");
        ExecutorService esUsers = Executors.newFixedThreadPool(names.size());

        IntStream.rangeClosed(1, names.size())
                .mapToObj(i -> new User(i, names.get(i - 1)))
                .parallel()
                .forEach(esUsers::submit);

        List<Runnable> miners = IntStream.rangeClosed(1, threadCount)
                .mapToObj(Miner::new)
                .collect(Collectors.toList());

        for (int i = 0, size = blockchain.size(); i < 5; i++, size++) {
            ExecutorService esMiners = Executors.newFixedThreadPool(threadCount);
            miners.parallelStream().forEach(esMiners::submit);

            while (blockchain.size() == size) {
                Thread.onSpinWait();
            }

            esMiners.shutdownNow();
        }

        esUsers.shutdownNow();

        System.out.println(blockchain.toStringLast(5));
    }
}