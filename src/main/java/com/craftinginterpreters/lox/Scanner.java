package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

/**
 * 토큰 스캐너
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords = new HashMap<>();

    public Scanner(String source) {
        this.source = source;
        keywords.put("and", AND);
        keywords.put("or", OR);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("for", FOR);
        keywords.put("while", WHILE);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("fun", FUN);
        keywords.put("var", VAR);
        keywords.put("class", CLASS);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("return", RETURN);
        keywords.put("print", PRINT);
        keywords.put("nil", NIL);
    }

    /**
     * 토큰을 스캔한다.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case ';': addToken(SEMICOLON); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case '*': addToken(STAR); break;
            case '%': addToken(PERCENT); break;

            case '!': addToken(match('=') ? BANG_EQUAL : BANG);
            case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<': addToken(match('=') ? LESS_EQUAL : LESS);
            case '>': addToken(match('=') ? GREATER_EQUAL : GREATER);

            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n': line++; break;

            case '"': string(); break;

            default:
                if (isDigit(c)) {
                    number(); break;
                }
                if (isAlpha(c)) {
                    identifier(); break;
                }
                Lox.error(line, "Unexpected character.");
        }
    }

    /**
     * 토큰을 스캔한다.
     *
     * @return tokens
     */
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
     * 토큰의 마지막에 다달았는지 확인한다.
     *
     * @return true if current is at the end of tokens
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * 입력값이 숫자인지 확인한다.
     *
     * @param c 입력값
     * @return true if digit
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * 입력값이 알파벳 또는 언더스코어인지 확인한다.
     *
     * @param c 입력값
     * @return true if alphabet or underscore
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || c == '_';
    }

    /**
     * 입력값이 알파벳 똔느 언더스코어 또는 숫자인지 확인한다.
     *
     * @param c 입력값
     * @return true if alphabet, underscore or digit
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * current 가 다음 토큰을 바라보고 현재 위치를 반환한다.
     *
     * @return current position
     */
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    /**
     * 토큰을 목록에 추가한다.
     *
     * @param type 토큰 타입
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * 토큰을 목록에 추가한다.
     *
     * @param type 토큰 타입
     * @param literal 입력 리터럴
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * 현재 입력값이 기대값과 동일한지 확인한다.
     *
     * @param expected 기대값
     * @return true if current matches expected
     */
    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    /**
     * 바로 다음 입력값을 확인한다.
     *
     * @return 다음 입력값
     */
    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    /**
     * 두 번째 다음 입력값을 확인한다.
     *
     * @return 두 번째 다음 입력값
     */
    private char peekNext() {
        int nextPosition = current + 1;
        return (nextPosition >= source.length()) ? '\0' : source.charAt(nextPosition);
    }

    /**
     * 문자열을 처리한다.
     */
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }
        if (isAtEnd()) {
            Lox.error(line, "Unterminated String.");
            return;
        }
        advance();
        // 따옴표 제외한 문자열만 추가하기 위해 앞뒤로 하나씩 제외
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /**
     * 숫자를 처리한다.
     */
    private void number() {
        while(isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * 예약어를 처리한다.
     */
    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, IDENTIFIER);
        addToken(type);
    }
}
