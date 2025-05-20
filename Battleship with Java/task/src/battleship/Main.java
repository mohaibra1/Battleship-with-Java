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
    static String[] coordinates = new String[50];
    static int index;
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
        //printField(field);
    }

    static void game(String input){
        String[] copyOfCoordinates = new String[]{Arrays.toString(coordinates)};
        while (true){
            boolean valid = isValidCoordinate(input);
            if (!valid){
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                input = Main.scanner.nextLine();
                continue;
            }

            int[] parseCoordinate = parseCoordinate(input);
            char checkPosition = field[parseCoordinate[0]][parseCoordinate[1]];
            if(checkPosition == SHIP){
                //field[parseCoordinate[0]][parseCoordinate[1]] = 'X';
                fieldFogOfWar[parseCoordinate[0]][parseCoordinate[1]] = 'X';
                printField(fieldFogOfWar);
                //check if ship sunk
                boolean sank = sunk(parseCoordinate[0]+""+parseCoordinate[1]);
                if (sank && !winner()){
                    System.out.println("You sank a ship! Specify a new target:");
                }else if (!winner() && !sank){
                    System.out.println("You hit a ship! Try again:");
                }
            }else if (checkPosition == WATER){
                //field[parseCoordinate[0]][parseCoordinate[1]] = 'M';
                fieldFogOfWar[parseCoordinate[0]][parseCoordinate[1]] = 'M';
                printField(fieldFogOfWar);
                System.out.println("You missed. Try again:");
            }
            if(winner()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            }
            input = scanner.nextLine();
        }

    }

    static boolean sunk(String position){
        //take the position of the ship, then check if the positions are hit
        for (String coordinate : coordinates) {
            if(coordinate != null) {
                String[] hold = coordinate.split(" ");
                for(String s: hold){
                    String start = String.valueOf(s.charAt(0));
                    String end = String.valueOf(s.charAt(1));
                    String checkPosition = start + end;
                    if(checkPosition.equals(position)){
                        for (String string : hold) {
                            int start1 = Integer.parseInt(String.valueOf(string.charAt(0)));
                            int end1 = Integer.parseInt(String.valueOf(string.charAt(1)));
                            char positionChecking = fieldFogOfWar[start1][end1];
                            //if this ship is not all hit false
                            if (positionChecking != 'X') {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static boolean winner(){
        for (String coordinate : coordinates) {
            if(coordinate != null) {
                String[] hold = coordinate.split(" ");
                for (String s : hold) {
                    int start = Integer.parseInt(String.valueOf(s.charAt(0)));
                    int end = Integer.parseInt(String.valueOf(s.charAt(1)));
                    char checkPosition = fieldFogOfWar[start][end];
                    if (checkPosition != 'X') {
                        return false;
                    }
                }
            }
        }
        return true;
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
        String store = "";
        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                field[r][c] = SHIP;
                //store coordinates
                store = store + r + c + " ";
            }
        }
        coordinates[index++] = store;
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

