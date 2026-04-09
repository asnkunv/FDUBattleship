package org.fdu;

public record AttackResponseDTO(
        String[][] grid,     // "water", "hit", "miss"
        int guessesLeft,
        String gameStatus,   // "IN_PROGRESS", "WIN", "LOSS"
        String message,
        boolean isError){}
