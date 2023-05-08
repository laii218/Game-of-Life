import java.util.*;

// Path: src\Main.java
public class Main {
    private final int BOARD_SIZE = 10;
    private char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private int moves = 0;
    private final int MOVE_LIMIT = 20;
    private int[] playerPos = new int[]{0, 0};
    private int[] treasurePos = new int[]{9, 9};
    private List<int[]> obstacles = new ArrayList<>();
    private Map<int[], int[]> jumps = new HashMap<>();
    private int lives;
    private int money;
    private List<String> collectedItems;
    private int[] itemPos = new int[]{4, 6};
    private int[] moneyPos= new int[]{5, 5};
    private int[] livesPos = new int[]{5, 7};
    private int[] itemPos2 = new int[]{4, 7};
    private int[] moneyPos2= new int[]{0, 9};
    private int[] livesPos2 = new int[]{9, 0};
    private int[] livePos3 = new int[]{8, 9};
    private int[] moneyPos3 = new int[]{9, 4};
    private int[] itemPos3 = new int[]{9, 5};


    // constructor
    public Main() {
        System.out.println("\nWelcome to the board game! You have 20 moves to find the treasure ($). " +
                "\nAvoid the obstacles (X) and don't go off the board! Good luck!" +
                "\n@ is the player, $ is the treasure, X is an obstacle, and I,S,M,m are an items." +
                "\nyou have 3 lives and 0 dollars at the beginning. If you land on an item, you will get it and will be added to your collection.");

        // initialize board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = '.';
            }
        }

        // add obstacles
        obstacles.add(new int[]{1, 1});
        obstacles.add(new int[]{2, 2});
        obstacles.add(new int[]{3, 3});
        obstacles.add(new int[]{4, 4});
        obstacles.add(new int[]{6, 2});
        obstacles.add(new int[]{7, 1});
        obstacles.add(new int[]{8, 8});
        obstacles.add(new int[]{7, 7});
        obstacles.add(new int[]{8, 0});

        // add jumps
        jumps.put(new int[]{0, 2}, new int[]{5, 2});
        jumps.put(new int[]{2, 8}, new int[]{4, 1});

        // place player and treasure on board
        board[playerPos[0]][playerPos[1]] = '@';
        board[treasurePos[0]][treasurePos[1]] = '$';

        // place obstacles on board
        for (int[] obs : obstacles) {
            board[obs[0]][obs[1]] = 'X';
        }

        // place jumps on board
        for (int[] src : jumps.keySet()) {
            int[] dest = jumps.get(src);
            board[src[0]][src[1]] = '#';
            board[dest[0]][dest[1]] = '#';
        }
        // initialize lives, money, and collected items
        lives = 3;
        money = 0;
        collectedItems = new ArrayList<>();

        // place items on board
        board[itemPos[0]][itemPos[1]] = 'I';
        board[itemPos2[0]][itemPos2[1]] = 'S';
        board[itemPos3[0]][itemPos3[1]] = '!';
        // place money on board
        board[moneyPos[0]][moneyPos[1]] = 'M';
        board[moneyPos2[0]][moneyPos2[1]] = 'm';
        board[moneyPos3[0]][moneyPos3[1]] = '!';
        // place lives on board
        board[livesPos[0]][livesPos[1]] = 'L';
        board[livesPos2[0]][livesPos2[1]] = 'l';
        board[livePos3[0]][livePos3[1]] = '!';
    }

    // print board
    public void playGame() {
        // create scanner
        Scanner scanner = new Scanner(System.in);
        // print instructions
        System.out.println("Some squares make the player 'jump' to some other position!");
        printBoard();
        // game loop
        while (moves < MOVE_LIMIT && lives > 0) {
            System.out.println("Enter next move (u/d/l/r): ");
            String input = scanner.nextLine();

            int[] newPos = getNextPosition(input);
            // check if move is valid
            if (isValidMove(newPos)) {
                newPos = handleJump(newPos);
                updateBoard(newPos);
                moves++;
                printBoard();
                // check if player has collected an item
                if (Arrays.equals(newPos, treasurePos)) {
                    collectedItems.add("Treasure");
                    System.out.println("Congratulations! You found the treasure!");
                    System.out.println("Lives: " + lives);
                    System.out.println("Money: " + money);
                    System.out.println("Collected items: " + collectedItems);
                    return;
                }
            }   else {
                System.out.println("Try again.");
            }
        }

        System.out.println("Sorry, you ran out of moves. Game over.");
    }
    // update board with new player position
    private void printBoard() {
        System.out.println("Lives: " + lives);
        System.out.println("Money: " + money);
        System.out.println("Collected items: " + collectedItems);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // update board with new player position
    private int[] getNextPosition(String input) {
        int[] newPos = Arrays.copyOf(playerPos, playerPos.length);

        switch (input) {
            case "u":
                newPos[0] -= 1;
                break;
            case "d":
                newPos[0] += 1;
                break;
            case "l":
                newPos[1] -= 1;
                break;
            case "r":
                newPos[1] += 1;
                break;
            default:
                break;
        }

        return newPos;
    }
    // update board with new player position
    private boolean isValidMove(int[] newPos) {
        if (newPos[0] < 0 || newPos[0] >= BOARD_SIZE || newPos[1] < 0 || newPos[1] >= BOARD_SIZE) {
            return false;
        }
        // check if player hits an obstacle
        for (int[] obs : obstacles) {
            if (Arrays.equals(newPos, obs)) {
                lives--;
                obs = playerPos;
                if (lives == 0) {
                    System.out.println("You ran out of lives! You lose!");

                } else {
                    System.out.println("Oops, you hit an obstacle! You lost a life.");
                    System.out.println("Lives left: " + lives);
                }
                return false;
            }
        }
        return true;
    }

    // handle jump if player lands on a jump square
    private int[] handleJump(int[] newPos) {
        for (int[] src : jumps.keySet()) {
            if (Arrays.equals(newPos, src)) {
                return jumps.get(src);
            }
        }

        return newPos;
    }

    // update board with new player position
    private void updateBoard(int[] newPos) {
        int oldX = playerPos[0];
        int oldY = playerPos[1];
        int newX = newPos[0];
        int newY = newPos[1];

        // update board with new player position
        board[oldX][oldY] = '.';
        board[newX][newY] = '@';

        // update player position
        playerPos[0] = newX;
        playerPos[1] = newY;

        // handle obstacle collision
        for (int[] obs : obstacles) {
            if (Arrays.equals(newPos, obs)) {
                System.out.println("Oh no! You hit an obstacle and lost a life.");
                lives--;
                board[newX][newY] = 'X';
            }
        }

        // handle life collection
        if (livesPos != null && Arrays.equals(newPos, livesPos)) {
            System.out.println("Congratulations! You found a life and added 1 life to your balance.");
            lives++;
            livesPos = null;
        }

        // handle money collection
        if (moneyPos != null && Arrays.equals(newPos, moneyPos)) {
            System.out.println("Congratulations! You found money and added 100 coins to your balance.");
            money += 100;
            moneyPos = null;
        }

        // handle item collection
        if (itemPos != null && Arrays.equals(newPos, itemPos)) {
            System.out.println("Congratulations! You found a Iorn Sword and added it to your collection.");
            collectedItems.add("Iron Sword");
            itemPos = null;
        }
        // handle item2 collection
        if (itemPos2 != null && Arrays.equals(newPos, itemPos2)) {
            System.out.println("Congratulations! You found a Steel Shield and added it to your collection.");
            collectedItems.add("Steel Shield");
            itemPos2 = null;
        }
        // handle money2 collection
        if (moneyPos2 != null && Arrays.equals(newPos, moneyPos2)) {
            System.out.println("Congratulations! You found money and added 200 coins to your balance.");
            money += 200;
            moneyPos2 = null;
        }
        // handle life2 collection
        if (livesPos2 != null && Arrays.equals(newPos, livesPos2)) {
            System.out.println("Congratulations! You found a life and added 1 life to your balance.");
            lives++;
            livesPos2 = null;
        }
        // handle lif3 collection
        if (livePos3 != null && Arrays.equals(newPos, livePos3)) {
            System.out.println("Congratulations! You found a life and added 1 life to your balance.");
            lives++;
            livePos3 = null;
        }
        // handle money3 collection
        if (moneyPos3 != null && Arrays.equals(newPos, moneyPos3)) {
            System.out.println("Congratulations! You found money and added 300 coins to your balance.");
            money += 300;
            moneyPos3 = null;
        }
        // handle item3 collection
        if (itemPos3 != null && Arrays.equals(newPos, itemPos3)) {
            System.out.println("Congratulations! You found a Diamond Sword and added it to your collection.");
            collectedItems.add("Diamond Sword");
            itemPos3 = null;
        }
    }



    // main method
    public static void main(String[] args) {
        Main game = new Main();
        game.playGame();
    }
}