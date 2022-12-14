package by.bsu.algorithms.labs.lab3.task5.entity.participant;

public sealed interface Participant permits Receiver, Requester {
    void makeMove();
}
