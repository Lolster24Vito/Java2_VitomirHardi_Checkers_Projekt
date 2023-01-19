package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;

public interface MatchReplayable {
    void replayNextMove(PlayerMove playerMove);
}
