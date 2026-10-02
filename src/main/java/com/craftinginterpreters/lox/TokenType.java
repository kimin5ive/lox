package com.craftinginterpreters.lox;

/**
 * JLox Token Type
 */
public enum TokenType {
    // 단일 문자 토큰
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    COMMA,
    DOT,
    MINUS,
    PLUS,
    STAR,
    PERCENT,
    SEMICOLON,

    // 조건부 단일-다중 문자 토큰,
    SLASH,
    SLASH_SLASH,
    BANG,
    BANG_EQUAL,
    EQUAL,
    EQUAL_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // keywords 예약어
    AND,
    OR,
    IF,
    ELSE,
    FOR,
    WHILE,
    TRUE,
    FALSE,
    FUN,
    VAR,
    CLASS,
    SUPER,
    THIS,
    RETURN,
    PRINT,
    NIL,

    // literals
    NUMBER,
    IDENTIFIER,
    STRING,

    EOF
}
