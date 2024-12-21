package com.stemlaur.secretsanta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

record Participant(String name, String receiver) {
}
record Couple(String one, String two) {
    public boolean matches(String first, String second) {
        return (one.equalsIgnoreCase(first) && two.equalsIgnoreCase(second))
                || (two.equalsIgnoreCase(first) && one.equalsIgnoreCase(second));
    }
}

public class Main {
    private static final Collector<?, ?, ?> SHUFFLER = Collectors.collectingAndThen(
            Collectors.toCollection(ArrayList::new),
            list -> {
                Collections.shuffle(list);
                return list;
            }
    );

    @SuppressWarnings("unchecked")
    private static <T> Collector<T, ?, List<T>> toShuffledList() {
        return (Collector<T, ?, List<T>>) SHUFFLER;
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("couples.txt"));
        List<Couple> couples = extractCouples(lines);
        final Set<Participant> finalPairs = new HashSet<>();
        final List<String> shuffledParticipants = new ArrayList<>(extractParticipants(lines).stream().collect(toShuffledList()));
        final List<String> shuffledPossibleReceivers = new ArrayList<>(extractParticipants(lines).stream().collect(toShuffledList()));

        while (!shuffledParticipants.isEmpty()) {
            String currentParticipantToPair = shuffledParticipants.removeFirst();
            String chosenReceiver = shuffledPossibleReceivers.stream()
                    .filter(possibleReceiver -> isNotAlreadyReceiver(possibleReceiver, finalPairs))
                    .filter(possibleReceiver -> !possibleReceiver.equals(currentParticipantToPair))
                    .filter(possibleReceiver -> !areCouples(couples, currentParticipantToPair, possibleReceiver))
                    .findFirst()
                    .orElseThrow();
            finalPairs.add(new Participant(currentParticipantToPair, chosenReceiver));
        }

        finalPairs.forEach(System.out::println);
    }

    private static List<String> extractParticipants(List<String> lines) {
        List<String> result = new ArrayList<>();
        lines.forEach(line -> {
            String[] split = line.split("/");
            if (split.length == 1) {
                result.add(line.trim());
            } else {
                result.add(split[0].trim());
                result.add(split[1].trim());
            }
        });
        return result;
    }

    private static List<Couple> extractCouples(List<String> lines) {
        List<Couple> result = new ArrayList<>();
        lines.forEach(line -> {
            String[] split = line.split("/");
            if (split.length != 1) {
                result.add(new Couple(split[0].trim(), split[1].trim()));
            }
        });
        return result;
    }

    private static boolean areCouples(List<Couple> couples,
                                      String firstParticipant,
                                      String receiverName) {
        for (Couple couple : couples) {
            if (couple.matches(firstParticipant, receiverName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNotAlreadyReceiver(String r, Set<Participant> pairs) {
        return pairs.stream().noneMatch(p -> p.receiver().equals(r));
    }
}