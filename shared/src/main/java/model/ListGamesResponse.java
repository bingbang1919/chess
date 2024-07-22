package model;

import chess.ChessGame;

import java.util.Collection;

public record ListGamesResponse(Collection<GameData> games) {}
