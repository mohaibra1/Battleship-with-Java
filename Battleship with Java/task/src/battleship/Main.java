package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int SIZE = 10;
    private static final char WATER = '~';
    private static final char SHIP = 'O';
    private static final char[] ROWS = "ABCDEFGHIJ".toCharArray();
    private static final String ERROR_MSG = "Error!";

    private static char[][] field = new char[SIZE][SIZE];

    public static void main(String[] args) {
        initField();
        printField();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the ship:");
        String input = scanner.nextLine();
        String[] coords = input.trim().toUpperCase().split("\\s+");

        if (coords.length != 2) {
            System.out.println(ERROR_MSG);
            return;
        }

        int[] start = parseCoordinate(coords[0]);
        int[] end = parseCoordinate(coords[1]);

        if (start == null || end == null) {
            System.out.println(ERROR_MSG);
            return;
        }

        // Check alignment: must be horizontal or vertical
        if (start[0] != end[0] && start[1] != end[1]) {
            System.out.println(ERROR_MSG);
            return;
        }

        // Place ship and collect its parts
        List<String> parts = new ArrayList<>();
        int length;

        if (start[0] == end[0]) {
            // Horizontal ship
            int row = start[0];
            int minCol = Math.min(start[1], end[1]);
            int maxCol = Math.max(start[1], end[1]);
            length = maxCol - minCol + 1;

            for (int c = minCol; c <= maxCol; c++) {
                field[row][c] = SHIP;
                parts.add("" + ROWS[row] + (c + 1));
            }
        } else {
            // Vertical ship
            int col = start[1];
            int minRow = Math.min(start[0], end[0]);
            int maxRow = Math.max(start[0], end[0]);
            length = maxRow - minRow + 1;

            for (int r = minRow; r <= maxRow; r++) {
                field[r][col] = SHIP;
                parts.add("" + ROWS[r] + (col + 1));
            }
        }

        System.out.println("Length: " + length);
        System.out.print("Parts: ");
        System.out.println(String.join(" ", parts));
        //printField();
    }

    private static void initField() {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(field[i], WATER);
        }
    }

    private static void printField() {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print(ROWS[i] + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[] parseCoordinate(String coord) {
        if (coord.length() < 2 || coord.length() > 3) return null;

        char rowChar = coord.charAt(0);
        int row = rowChar - 'A';
        int col;

        try {
            col = Integer.parseInt(coord.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return null;
        }

        return new int[]{row, col};
    }
}
