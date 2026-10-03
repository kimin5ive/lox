package com.craftinginterpreters.lox;

import lombok.RequiredArgsConstructor;

/**
 * JLox Token
 */
@RequiredArgsConstructor
public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
