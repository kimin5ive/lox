package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * JLox Interpreter
 */
public class Lox {
    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Usage: jlox [script]");
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /**
     * 파일 경로에 있는 lox 소스 코드를 실행한다.
     *
     * @param path 파일 경로
     * @throws IOException 잘못된 경로를 입력한 경우 예외
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            System.exit(65);
        }
    }

    /**
     * 소스 코드를 실행한다.
     *
     * @param source 소스 코드
     */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /**
     * 명령줄에서 코드를 입력받아 실행한다.
     *
     * @throws IOException
     */
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }

    }

    /**
     * 예외를 처리한다.
     * @param line 에러가 발생한 라인
     * @param message 에러 메시지
     */
    protected static void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * 에러가 발생한 위치를 출력한다.
     * @param line 에러가 발생한 라인
     * @param where 에러 발생 위치
     * @param message 에러 메시지
     */
    private static void report(int line, String where, String message) {
        System.err.printf("[line %d] Error%s: %s%n", line, where, message);
        hadError = true;
    }
}