package model;

import chess.ChessGame;
import chess.ChessPosition;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) { }
