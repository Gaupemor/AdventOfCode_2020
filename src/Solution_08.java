import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Solution_08 {

    enum Op { acc, jmp, nop }

    static int accumulator = 0;

    static class Instruction {
        Op operation;
        int argument;
        boolean executed = false;

        Instruction(Op operation, int argument) {
            this.operation = operation;
            this.argument = argument;
        }

        Instruction(String operationStr, Integer argument) {
            this(Op.valueOf(operationStr), argument);
        }
    }

    private static Instruction[] parseInput() {
        try {
            return Files.lines(Path.of(InputPath.input_08.getPath()))
                    .map(i -> {
                        String[] instruction = i.split(" ");
                        return new Instruction(instruction[0], Integer.parseInt(instruction[1]));
                    }).toArray(Instruction[]::new);
        } catch (IOException e) {
            return new Instruction[0];
        }
    }

    private static boolean execute(Instruction[] program) {
        accumulator = 0;
        for(int i = 0; i < program.length; i++) {
            Instruction inst = program[i];
            if(inst.executed) return false;
            program[i].executed = true;
            switch(inst.operation) {
                case acc -> accumulator += inst.argument;
                case jmp -> i += inst.argument - 1;
                case nop -> { }
                default -> { return false; }
            }
        }
        return true;
    }

    // answer: 1317
    public static int solve1() {
        if(!execute(parseInput()))
            return accumulator;
        else return -1;
    }

    // answer: 1033
    public static int solve2() {
        Instruction[] program = parseInput();
        for(int i = 0; i < program.length; i++) {
            Instruction[] fixedProgram = parseInput();
            if(fixedProgram[i].operation == Op.nop) fixedProgram[i].operation = Op.jmp;
            else if(fixedProgram[i].operation == Op.jmp) fixedProgram[i].operation = Op.nop;
            if(execute(fixedProgram)) return accumulator;
        }
        return -1;
    }

    public static void solveAll() {
        System.out.println(solve1());
        System.out.println(solve2());
    }
}
