import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class battleship {
    public static int[] computerFirstHit;
    public static int[] computerHits;
    public static boolean clearedSide1 = false;
    public static boolean clearedSide2 = false;
    public static boolean skipComputer = false;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\033[0;36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\033[0;33m";

    public static final char ship = 'S';
    public static final char water = '~';
    public static final char hit = 'X';
    public static final char miss = '0';

    public static char[][] gridUser = createGrid(10, true);
    public static char[][] gridComputer = createGrid(10, true);
    public static char[][] gridGuess = createGrid(10, false);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gameMode;
        do {
            System.out.println("Game Modes:");
            System.out.println("1. Easy");
            System.out.println("2. Hard");
            System.out.println("3. GOD");
            System.out.print("Select game mode: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                System.out.print("Select game mode: ");
                scanner.next();
            }
            gameMode = scanner.nextInt();
        } while (gameMode < 1 || gameMode > 3);

        System.out.println("Your Ship Grid: ");
        showGrid(gridUser);

        while (true) {
            // User
            playerShot();
            if (!checkAlive(gridComputer)) {
                System.out.println("You win!");
                break;
            }

            System.out.println("");

            // Computer
            if (gameMode == 1) {
                computerShotEasy();
            } else if (gameMode == 2) {
                computerShotHard();
            } else if (gameMode == 3) {
                computerShotGOD();
            }

            if (!checkAlive(gridUser)) {
                System.out.println("You lose!");
                break;
            }
        }
    }

    public static char[][] createGrid(int boardLength, boolean ships) {
        char[][] grid = new char[boardLength][boardLength];

        for (char[] row : grid) {
            Arrays.fill(row, water);
        }

        if (ships) {
            grid = generateShips(grid);
        }
        return grid;
    }

    public static void showGrid(char[][] grid) {
        System.out.print(" |");
        for (int i = 1; i <= grid.length; i++) {
            System.out.print(" " + i);
        }
        System.out.println(" ");
        for (int i = 0; i < grid.length; i++) {
            System.out.print((char) ('A' + i) + "| ");
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == water) {
                    System.out.print(ANSI_BLUE + grid[i][j] + ANSI_RESET + " ");
                } else if (grid[i][j] == ship) {
                    System.out.print(ANSI_YELLOW + grid[i][j] + ANSI_RESET + " ");
                } else if (grid[i][j] == miss) {
                    System.out.print(ANSI_WHITE + grid[i][j] + ANSI_RESET + " ");
                } else if (grid[i][j] == hit) {
                    System.out.print(ANSI_RED + grid[i][j] + ANSI_RESET + " ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println(" ");
        }
    }

    public static char[][] generateShips(char[][] grid) {
        int Destroyer = 2;
        int Submarine = 3;
        int Cruiser = 3;
        int Battleship = 4;
        int Carrier = 5;

        generateShip(grid, Destroyer);
        generateShip(grid, Submarine);
        generateShip(grid, Cruiser);
        generateShip(grid, Battleship);
        generateShip(grid, Carrier);

        return grid;
    }

    public static char[][] generateShip(char[][] grid, int shipSize) {
        boolean shipPlaced = false;
        while (!shipPlaced) {
            int[] location = generateShipCoordinate(grid.length);
            int[][] fullLocation = new int[shipSize][2];
            char possiblePlacement = grid[location[0]][location[1]];
            if (possiblePlacement == water) {
                int locationX = location[0];
                int locationY = location[1];
                int allignment = new Random().nextInt(2);
                boolean canPlace = true;
                for (int i = 0; i < shipSize; i++) {
                    int newX = locationX + (allignment == 0 ? i : 0);
                    int newY = locationY + (allignment == 1 ? i : 0);
                    if (newX < 0 || newX >= grid.length || newY < 0 || newY >= grid[0].length
                            || grid[newX][newY] != water) {
                        canPlace = false;
                        break;
                    }
                }
                if (canPlace) {
                    for (int i = 0; i < shipSize; i++) {
                        int newX = locationX + (allignment == 0 ? i : 0);
                        int newY = locationY + (allignment == 1 ? i : 0);
                        grid[newX][newY] = ship;
                        fullLocation[i][0] = newX;
                        fullLocation[i][1] = newY;
                    }
                    shipPlaced = true;
                }
            }
        }
        return grid;
    }

    public static int[] generateShipCoordinate(int boardLength) {
        int[] coordinates = new int[2];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = new Random().nextInt(boardLength);
        }
        return coordinates;
    }

    public static int[] convertShotToGrid(String firingLocation) {
        int[] location = new int[2];
        location[0] = firingLocation.toUpperCase().charAt(0) - 'A';
        location[1] = Integer.parseInt(firingLocation.substring(1)) - 1;
        return location;
    }

    public static void playerShot() {
        Scanner inputCoordinates = new Scanner(System.in);

        System.out.print("Type in Coordinate to fire: ");
        String inputted = inputCoordinates.nextLine();
        System.out.println(" ");

        try {
            int[] fired = convertShotToGrid(inputted);

            if (gridGuess[fired[0]][fired[1]] == water) {
                if (gridComputer[fired[0]][fired[1]] == ship) {
                    gridGuess[fired[0]][fired[1]] = hit;
                    gridComputer[fired[0]][fired[1]] = hit;
                    System.out.println("Your Guess Grid: ");
                    showGrid(gridGuess);
                } else {
                    gridGuess[fired[0]][fired[1]] = miss;
                    System.out.println("Your Guess Grid: ");
                    showGrid(gridGuess);
                }
            } else {
                System.out.println("You have already inputted this coordinate previously");
                playerShot();
            }
        } catch (Exception e) {
            System.out.println("This is not a valid coordinate...");
            playerShot();
        }
    }

    public static void computerShotEasy() {
        while (true) {
            int computerFired = computerRandFire();
            if (computerFired != 2 && computerFired != 3 && computerFired != 4) {
                break;
            }
        }
    }

    public static void computerShotHard() {
        if (clearedSide1 && clearedSide2) {
            computerFirstHit = null;
            computerHits = null;
            clearedSide1 = false;
            clearedSide2 = false;
            computerShotHard();
        }

        int[] fullPosition = null;
        if (computerFirstHit == null) {
            int computerFired = computerRandFire();
            if (computerFired == 2 || computerFired == 3 || computerFired == 4) {
                computerShotHard();
            }
        } else {
            if (checkAlive(gridUser)) {
                if (computerHits == null) {
                    int X = computerFirstHit[0];
                    int Y = computerFirstHit[1];
                    int num = new Random().nextInt(4);

                    if (num == 0) {
                        fullPosition = generateFullLocation(X - 1, Y);
                    } else if (num == 1) {
                        fullPosition = generateFullLocation(X + 1, Y);
                    } else if (num == 2) {
                        fullPosition = generateFullLocation(X, Y - 1);
                    } else if (num == 3) {
                        fullPosition = generateFullLocation(X, Y + 1);
                    }

                    if (fullPosition != null) {
                        if (isValidPosition(fullPosition)) {
                            int computerFired = computerFire(fullPosition);
                            if (computerFired == 0) {
                                computerHits = fullPosition;
                            } else if (computerFired == 2 || computerFired == 3 || computerFired == 4) {
                                computerShotHard();
                            }
                        } else {
                            computerShotHard();
                        }
                    } else {
                        computerShotHard();
                    }
                } else {
                    int x_diff = computerHits[0] - computerFirstHit[0];
                    int y_diff = computerHits[1] - (computerFirstHit[1]);

                    if (fullPosition == null && !skipComputer) {
                        if (x_diff != 0) {
                            if (x_diff > 0) {
                                fullPosition = generateFullLocation(computerHits[0] + 1, computerHits[1]);
                            } else if (x_diff < 0) {
                                fullPosition = generateFullLocation(computerHits[0] - 1, computerHits[1]);
                            }
                        } else if (y_diff != 0) {
                            if (y_diff > 0) {
                                fullPosition = generateFullLocation(computerHits[0], computerHits[1] + 1);
                            } else if (y_diff < 0) {
                                fullPosition = generateFullLocation(computerHits[0], computerHits[1] - 1);
                            }
                        }
                    } else {
                        fullPosition = generateFullLocation(computerHits[0], computerHits[1]);
                        skipComputer = false;
                    }

                    if (fullPosition != null) {
                        if (isValidPosition(fullPosition)) {
                            int computerFired = computerFire(fullPosition);
                            if (computerFired == 0) {
                                computerHits = fullPosition;
                            } else if (computerFired == 3 || computerFired == 4) {
                                if (!clearedSide1) {
                                    clearedSide1 = true;
                                    skipComputer = true;
                                    computerHits = reverseLocation(x_diff, y_diff);
                                } else {
                                    clearedSide2 = true;
                                }
                                computerShotHard();
                            } else if (computerFired == 1) {
                                if (!clearedSide1) {
                                    clearedSide1 = true;
                                    skipComputer = true;
                                    computerHits = reverseLocation(x_diff, y_diff);
                                } else {
                                    clearedSide2 = true;
                                }
                            } else if (computerFired == 2) {
                                computerShotHard();
                            }
                        } else {
                            if (!clearedSide1) {
                                clearedSide1 = true;
                                skipComputer = true;
                                computerHits = reverseLocation(x_diff, y_diff);
                            } else {
                                clearedSide2 = true;
                            }
                            computerShotHard();
                        }
                    }
                }
            }
        }
    }

    public static int[] reverseLocation(int x_diff, int y_diff) {
        if (x_diff != 0) {
            if (x_diff > 0) {
                return new int[] { (computerFirstHit[0] - 1), (computerFirstHit[1]) };
            } else if (x_diff < 0) {
                return new int[] { (computerFirstHit[0] + 1), (computerFirstHit[1]) };
            }
        } else if (y_diff != 0) {
            if (y_diff > 0) {
                return new int[] { (computerFirstHit[0]), (computerFirstHit[1] - 1) };
            } else if (y_diff < 0) {
                return new int[] { (computerFirstHit[0]), (computerFirstHit[1] + 1) };
            }
        }
        return null;
    }

    public static void computerShotGOD() {
        Random rand = new Random();
        int chance = rand.nextInt(9);

        if (chance != 8) {
            for (int i = 0; i < gridUser.length; i++) {
                for (int j = 0; j < gridUser[i].length; j++) {
                    if (gridUser[i][j] == ship) {
                        int fireResult = computerFire(generateFullLocation(i, j));
                        if (fireResult == 0) {
                            return;
                        } else if (fireResult == 1) {
                            break;
                        }
                    }
                }
            }
        } else {
            if (computerRandFire() == 2) {
                computerShotGOD();
            }
        }
    }

    public static boolean isValidPosition(int[] position) {
        if (position.length != 2) {
            return false;
        }

        if (position[0] < 0 || position[0] >= gridComputer.length) {
            return false;
        } else if (position[1] < 0 || position[1] >= gridComputer.length) {
            return false;
        }

        return true;
    }

    public static int[] generateFullLocation(int xPos, int yPos) {
        int[] Location = new int[2];
        Location[0] = xPos;
        Location[1] = yPos;

        return Location;
    }

    public static int computerRandFire() {
        int xPos = new Random().nextInt(gridComputer.length);
        int yPos = new Random().nextInt(gridComputer.length);

        int fired = computerFire(generateFullLocation(xPos, yPos));
        return fired;
    }

    public static int computerFire(int[] fired) {
        try {
            if (gridUser[fired[0]][fired[1]] == ship) {
                gridUser[fired[0]][fired[1]] = hit;
                if (computerFirstHit == null) {
                    computerFirstHit = new int[2];
                    computerFirstHit[0] = fired[0];
                    computerFirstHit[1] = fired[1];
                }
                System.out.println("Your Ship Grid: ");
                showGrid(gridUser);
                return 0;
            } else if (gridUser[fired[0]][fired[1]] == hit) {
                return 2;
            } else if (gridUser[fired[0]][fired[1]] == miss) {
                return 3;
            } else {
                gridUser[fired[0]][fired[1]] = miss;
                System.out.println("Your Ship Grid: ");
                showGrid(gridUser);
                return 1;
            }
        } catch (Exception e) {
            return 4;
        }
    }

    public static boolean checkAlive(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    return true;
                }
            }
        }
        return false;
    }
}