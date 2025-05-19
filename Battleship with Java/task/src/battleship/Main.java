package battleship;

import java.util.*;

class Ship {
    String name;
    int size;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }
}

public class Main {
    static final int SIZE = 10;
    static final char WATER = '~';
    static final char SHIP = 'O';
    static final Scanner scanner = new Scanner(System.in);
    static char[][] field = createEmptyField();
    static char[][] fieldFogOfWar = createEmptyField();
    public static void main(String[] args) {

        List<Ship> ships = List.of(
                new Ship("Aircraft Carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Submarine", 3),
                new Ship("Cruiser", 3),
                new Ship("Destroyer", 2)
        );

        printField(field);

        for (Ship ship : ships) {
            boolean placed = false;
            while (!placed) {
                System.out.printf("Enter the coordinates of the %s (%d cells):%n", ship.name, ship.size);
                String input = scanner.nextLine();
                String[] parts = input.split(" ");

                if (parts.length != 2 || !isValidCoordinate(parts[0]) || !isValidCoordinate(parts[1])) {
                    System.out.println("Error! Invalid coordinates. Try again:");
                    continue;
                }

                int[] start = parseCoordinate(parts[0]);
                int[] end = parseCoordinate(parts[1]);

                if (!isStraightLine(start, end)) {
                    System.out.println("Error! Wrong ship location! Try again:");
                    continue;
                }

                int len = calculateLength(start, end);
                if (len != ship.size) {
                    System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.name);
                    continue;
                }

                if (isTooCloseToOtherShips(field, start, end)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    continue;
                }

                placeShip(field, start, end);
                printField(field);
                placed = true;
            }
        }
        System.out.println();
        System.out.println("The game starts!");
        printField(fieldFogOfWar);
        System.out.println("Take a shot!");
        String input = scanner.nextLine();
        game(input);
        printField(field);
    }

    static void game(String input){
        while (true){
            boolean valid = isValidCoordinate(input);
            if (!valid){
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                input = Main.scanner.nextLine();
                continue;
            }

            int[] parseCoordinate = parseCoordinate(input);
            char checkPosition = field[parseCoordinate[0]][parseCoordinate[1]];
            if(checkPosition != WATER){
                field[parseCoordinate[0]][parseCoordinate[1]] = 'X';
                fieldFogOfWar[parseCoordinate[0]][parseCoordinate[1]] = 'X';
                printField(fieldFogOfWar);
                System.out.println("You hit a ship");
            }else{
                field[parseCoordinate[0]][parseCoordinate[1]] = 'M';
                fieldFogOfWar[parseCoordinate[0]][parseCoordinate[1]] = 'X';
                printField(fieldFogOfWar);
                System.out.println("You missed!");
            }
            break;
        }

    }

    static char[][] createEmptyField() {
        char[][] field = new char[SIZE][SIZE];
        for (char[] row : field) {
            Arrays.fill(row, WATER);
        }
        return field;
    }

    static void printField(char[][] field) {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    static boolean isValidCoordinate(String coord) {
        return coord.matches("[A-J](10|[1-9])");
    }

    static int[] parseCoordinate(String coord) {
        int row = coord.charAt(0) - 'A';
        int col = Integer.parseInt(coord.substring(1)) - 1;
        return new int[]{row, col};
    }

    static boolean isStraightLine(int[] start, int[] end) {
        return start[0] == end[0] || start[1] == end[1];
    }

    static int calculateLength(int[] start, int[] end) {
        return Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]) + 1;
    }

    static void placeShip(char[][] field, int[] start, int[] end) {
        int r1 = Math.min(start[0], end[0]);
        int r2 = Math.max(start[0], end[0]);
        int c1 = Math.min(start[1], end[1]);
        int c2 = Math.max(start[1], end[1]);

        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                field[r][c] = SHIP;
            }
        }
    }

    static boolean isTooCloseToOtherShips(char[][] field, int[] start, int[] end) {
        int r1 = Math.min(start[0], end[0]) - 1;
        int r2 = Math.max(start[0], end[0]) + 1;
        int c1 = Math.min(start[1], end[1]) - 1;
        int c2 = Math.max(start[1], end[1]) + 1;

        for (int r = Math.max(0, r1); r <= Math.min(SIZE - 1, r2); r++) {
            for (int c = Math.max(0, c1); c <= Math.min(SIZE - 1, c2); c++) {
                if (field[r][c] == SHIP) {
                    return true;
                }
            }
        }
        return false;
    }
}

