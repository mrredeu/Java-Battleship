import java.util.*;

public class battleship {
	public static int [] computerFirstHit;
	public static int [] computerHits;
	public static int boatDestroyed = 0;

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
			boolean computerLife = checkAlive(gridComputer);
			if (computerLife == false) {
				System.out.println("You win!");
				break;
			}

			System.out.println("");

			// Computer
			if (gameMode == 1) {
				computerShotEasy();
			} else if (gameMode == 2) {
				computerShotHard();
			} else if (gameMode == 3){
				computerShotGOD();
			}

			boolean userLife = checkAlive(gridUser);
			if (userLife == false) {
				System.out.println("You lose!");
				break;
			}
    	}
	}
	
	public static char[][] createGrid(int boardLength, boolean ships) {
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
		System.out.print(" |");
       	for (int i = 1; i <= grid.length; i++) {
           System.out.print(" " + i);
       	}
       	System.out.println(" ");
       	for (int i = 0; i < grid.length; i++) {
           	System.out.print((char) ('A' + i) + "| ");
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
	
	public static void computerShotEasy() {
		while (true) {
			if (computerRandFire() != 2){
				break;
			}
		}
	}	
	
	public static void computerShotHard() {
		if(computerFirstHit == null){
			if (computerRandFire() == 2){
				computerShotHard();
			}
		}else{
			if (checkAlive(gridUser) == true) {
				String fullPosition = null;
				if (computerHits == null){
					Random rand = new Random();
					int X = computerFirstHit[0];
					int Y = computerFirstHit[1]+1;
					int num = rand.nextInt(4);
					if (num == 0 || num == 1){
						Integer newX = null;
						if (num == 0){
							newX = X-1;
							if (newX > 0 && newX <= gridUser.length){
								fullPosition = generateFullPosition(newX, Y);
								System.out.println("0: could fire on " + fullPosition);
							}
						}else if (num == 1){
							newX = X+1;
							if (newX > 0 && newX <= gridUser.length){
								fullPosition = generateFullPosition(newX, Y);
								System.out.println("1: could fire on " + fullPosition);
							}
						}
						if (fullPosition != null && newX != null){
							if (isValidPosition(fullPosition) && !isAlreadyFired(fullPosition)){
								int computerFired = computerFire(fullPosition);
								if (computerFired == 0){
									computerHits = new int[2];
									computerHits[0] = newX;
									computerHits[1] = Y;
								}else if (computerFired == 2){
									computerShotHard();
								}
							}
						}
					}else{
						Integer newY = null;
						if (num == 2){
							newY = Y-1;
							if (newY > 0 && newY <= gridUser.length){
								fullPosition = generateFullPosition(X, newY);
								System.out.println("2: could fire on " + fullPosition);
							}
						}else if (num == 3){
							newY = Y+1;
							if (newY > 0 && newY <= gridUser.length){
								fullPosition = generateFullPosition(X, newY);
								System.out.println("3: could fire on " + fullPosition);
							}
						}
						if (fullPosition != null && newY != null){
							if (isValidPosition(fullPosition) && !isAlreadyFired(fullPosition)){
								int computerFired = computerFire(fullPosition);
								if (computerFired == 0){
									computerHits = new int[2];
									computerHits[0] = X;
									computerHits[1] = newY;
								}else if (computerFired == 2){
									computerShotHard();
								}
							}else{
								computerShotHard();
							}
						}
					}
				}else{
					showGrid(gridUser);
					System.out.println(computerHits[0] +" "+computerFirstHit[0]);
					System.out.println(computerHits[1] +" "+(computerFirstHit[1]-1));
					int x_diff =  computerHits[0] - computerFirstHit[0];
					int y_diff = Math.abs(computerHits[1]-1) - computerFirstHit[0];

					if (x_diff != 0){
						Integer X = null;
						if (x_diff > 0) {
							X = computerHits[0] + 1;
						}else if (x_diff < 0) {
							X = computerHits[0] - 1;
						}

						int yPos = computerHits[1];
						if (X != null) {
							if(X > 0 && X <= gridUser.length){
								fullPosition = generateFullPosition(computerFirstHit[0], computerFirstHit[1]);
								int computerFired = computerFire(fullPosition);

								if (computerFired == 2){
									computerShotHard();
								}else if (computerFired == 0){
									computerHits[0] = X;
									computerHits[1] = yPos;
									if (x_diff > 0 && boatDestroyed < 3){
										if (boatDestroyed == 0){
											computerHits[0] = computerFirstHit[0]; 
										}else{
											computerHits[0] = computerFirstHit[0] - 1; 
										}
										boatDestroyed++;
									}else if (x_diff < 0 && boatDestroyed < 3){
										if (boatDestroyed == 0){
											computerHits[0] = computerFirstHit[0]; 
										}else{
											computerHits[0] = computerFirstHit[0] + 1; 
										}
										boatDestroyed++;
									}else if (boatDestroyed == 2){
										//computerHits = null;
									}
								}
							}else{
								//computerHits = null;
							}
						}
					}else if (y_diff != 0){
						Integer Y = null;
						if (y_diff > 0) {
							Y = computerHits[1] + 1;
						}else if (y_diff < 0) {
							Y = computerHits[1] - 1;
						}

						int X = computerHits[0];
						if (Y != null) {
							if(Y > 0 && Y <= gridUser.length){
								int getNumXPos = ((char)('a') + X);
								char xPos = (char)getNumXPos;
								fullPosition = xPos+""+Y;

								int computerFired = computerFire(fullPosition);

								if (computerFired == 2){
									computerShotHard();
								}else if (computerFired == 0){
									computerHits[0] = xPos;
									computerHits[1] = Y;
									if (y_diff > 0 && boatDestroyed < 3){
										if (boatDestroyed == 0){
											computerHits[0] = computerFirstHit[0]; 
										}else{
											computerHits[0] = computerFirstHit[0] - 1; 
										}
										boatDestroyed++;
									}else if (y_diff < 0 && boatDestroyed < 3){
										if (boatDestroyed == 0){
											computerHits[0] = computerFirstHit[0]; 
										}else{
											computerHits[0] = computerFirstHit[0] + 1; 
										}
										boatDestroyed++;
									}else if (boatDestroyed == 2){
										//computerHits = null;
									}
								}else{
									//computerHits = null;
								}
							}
						}
					}
				}
			}
		}
	}				
	
	public static void computerShotGOD(){
		Random rand = new Random();
		int chance = rand.nextInt(9);
	
		if (chance != 8) {
			for (int i = 0; i < gridUser.length; i++) {
				for (int j = 0; j < gridUser[i].length; j++) {
					 if (gridUser[i][j] == ship) {
						String fullPosition = generateFullPosition(i, j+1);
	
						int fireResult = computerFire(fullPosition);
						if(fireResult == 0){
							return; 
						} else if (fireResult == 1){
							break;
						}
					 }
				 }
			}
		}else{
			if(computerRandFire() == 2){
				computerShotGOD();
			}
		}
	}
	

	public static String generateFullPosition(int X, int yPos){
		int getNumXPos = ((char)('a') + X);
		char xPos = (char)getNumXPos;
		String fullPosition = xPos+""+yPos;

		return fullPosition;
	}

	public static boolean isValidPosition(String position) {
		if (position.length() != 2) {
			return false;
		}
		char xPos = position.charAt(0);
		int yPos = Character.getNumericValue(position.charAt(1));
		if (xPos < 'a' || xPos > 'a' + gridUser.length - 1) {
			return false;
		}
		if (yPos < 1 || yPos > gridUser.length) {
			return false;
		}
		return true;
	}

	public static boolean isAlreadyFired(String position) {
		int row = position.charAt(1) - '1';
		int col = position.charAt(0) - 'A';
		if (row >= 0 && row < gridComputer.length && col >= 0 && col < gridComputer[0].length) {
			return gridComputer[row][col] == 'X' || gridComputer[row][col] == '0';
		} else {
			return false;
		}
	}
	
	public static int computerRandFire(){
		Random rand = new Random();
		int getNumXPos = ((char)('a') + rand.nextInt(9));
		char xPos = (char)getNumXPos;
		int yPos = rand.nextInt(9);
		String fullPosition = xPos +""+ yPos;

		int fired = computerFire(fullPosition);
		return fired;
	}

	public static int computerFire(String fullPosition){
		try {
			int[] fired = convertShotToGrid(fullPosition);
			
			if(gridUser[fired[0]][fired[1]] == ship){
				gridUser[fired[0]][fired[1]] = hit;
				if (computerFirstHit == null){
					computerFirstHit = new int[2];
					computerFirstHit[0]= fired[0];
					computerFirstHit[1] = fired[1];
				}
				System.out.println("Your Ship Grid: ");
				showGrid(gridUser);
				return 0;
			}else if (gridUser[fired[0]][fired[1]] == hit || gridUser[fired[0]][fired[1]] == miss){
				return 2;
			}else{
				gridUser[fired[0]][fired[1]] = miss;
				System.out.println("Your Ship Grid: ");
				showGrid(gridUser);
				return 1;
			}
		} catch (Exception e) {
			return 2;
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