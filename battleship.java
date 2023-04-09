import java.util.*;

public class battleship {
   public static final String ANSI_RESET = "\u001B[0m";
   public static final String ANSI_RED = "\u001B[31m";
   public static final String ANSI_BLUE = "\033[0;36m";
   public static final String ANSI_WHITE = "\u001B[37m";
   public static final String ANSI_YELLOW = "\033[0;33m";
  
	public static char[][] gridUser = createGrid(10, true);
	public static char[][] gridComputer = createGrid(10, true);
	public static char[][] gridGuess = createGrid(10, false);
	
	public static void main(String[] args) {
		System.out.println("Your Ship Grid: ");
		showGrid(gridUser);
		
		while (true) {
			//User
			playerShot();
			boolean computerLife = checkAlive(gridComputer);
			if (computerLife == false) {
				System.out.println("You win!");
				break;
			}
			
			System.out.println("");
			
			//Computer
			computerShot();
			boolean userLife = checkAlive(gridUser);
			if (userLife == false) {
				System.out.println("You loose!");
				break;
			}
			
		}
		
	}
	
	public static char[][] createGrid(int boardLength, boolean ships) {
		char water = '~';
		char[][] grid = new char[boardLength][boardLength];
		
		for(char[] row : grid) {
			Arrays.fill(row, water);
		}
		
		if (ships == true) {
			grid = generateShips(grid);
		}
		return grid;
	}
	
	public static void showGrid(char[][] grid) {
       for (int i = 1; i <= grid.length; i++) {
           System.out.print(" " + i);
       }
       System.out.println(" ");
       for (int i = 0; i < grid.length; i++) {
           System.out.print((char) ('A' + i) + " ");
           for (int j = 0; j < grid[i].length; j++) {
           	if (grid[i][j] == '~') {
                   System.out.print(ANSI_BLUE + grid[i][j] + ANSI_RESET + " ");
               } else if (grid[i][j] == 'S') {
               	System.out.print(ANSI_YELLOW + grid[i][j] + ANSI_RESET + " ");
               } else if (grid[i][j] == '0') {
               	System.out.print(ANSI_WHITE + grid[i][j] + ANSI_RESET + " ");
               } else if (grid[i][j] == 'X') {
               	System.out.print(ANSI_RED + grid[i][j] + ANSI_RESET + " ");
           	}else {
                   System.out.print(grid[i][j] + " ");
               }
           }
           System.out.println(" ");
       }
	}
	
	public static char[][] generateShips(char[][] grid){
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
	
	public static char[][] generateShip(char[][]grid, int shipSize) {
	    char water = '~';
	    char ship = 'S';
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
	                if (newX < 0 || newX >= grid.length || newY < 0 || newY >= grid[0].length || grid[newX][newY] != water) {
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
		for (int i =0; i < coordinates.length; i++) {
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
		char ship = 'S';
		char hit = 'X';
		char miss = '0';
		char water = '~';
		Scanner inputCoordinates = new Scanner(System.in);
		
		System.out.print("Type in Coordinate to fire: ");
		String inputted = inputCoordinates.nextLine();
		System.out.println(" ");
	
		try {
			int[] fired = convertShotToGrid(inputted);
			
			if (gridGuess[fired[0]][fired[1]] == water) {
				if(gridComputer[fired[0]][fired[1]] == ship) {
					gridGuess[fired[0]][fired[1]] = hit;
					gridComputer[fired[0]][fired[1]] = hit;
					System.out.println("Your Guess Grid: ");
					showGrid(gridGuess);
					if (checkAlive(gridComputer) == true) {
						playerShot();
					}
				}else {
					gridGuess[fired[0]][fired[1]] = miss;
					System.out.println("Your Guess Grid: ");
					showGrid(gridGuess);
				}
			}else {
				System.out.println("You have already inputted this coordinate previously");
				playerShot();
			}
		} catch (Exception e) {
			System.out.println("This is not a valid coordinate...");
			playerShot();
		}
	}
	
	public static void computerShot() {
		char ship = 'S';
		char hit = 'X';
		char miss = '0';
		
		Random rand = new Random();
		int getNumXPos = ((char)('a')+ rand.nextInt(9));
		char xPos = (char)getNumXPos;
		int yPos = rand.nextInt(9);
		String fullPosition = xPos +""+ yPos;
		
		try {
			int[] fired = convertShotToGrid(fullPosition);
			
			if(gridUser[fired[0]][fired[1]] == ship){
				gridUser[fired[0]][fired[1]] = hit;
				if (checkAlive(gridUser) == true) {
					computerShot();
				}
			}else {
				gridUser[fired[0]][fired[1]] = miss;
				System.out.println("Your Ship Grid: ");
				showGrid(gridUser);
			}
		} catch (Exception e) {
			computerShot();
		}
	}
	
	public static boolean checkAlive(char[][] grid){
		boolean alive = false;
		
       for (int i = 0; i < grid.length; i++) {
           for (int j = 0; j < grid[i].length; j++) {
               if (grid[i][j] == 'S') {
               	alive = true;
               }
           }
       }
       return alive;
	}
}