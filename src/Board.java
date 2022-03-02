import java.util.Random;

public class Board {
    private int width;
    private int height;
    private int[][] board;
    private int nextPlayer;
    private boolean isFinished;
    private boolean isFulled;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        board = new int[width][height];
        nextPlayer = 1; //1: red 2: blue
        isFinished = false;
        isFulled = false;
    }

    public void printBoard() {
        for (int i = height - 1; i >= 0; i--){
            System.out.print("|");
            for (int j = 0; j < width; j++){
                String s = switch (board[j][i]) {
                    case 1 -> "🟥";
                    case 2 -> "🟦";
                    default -> "　";
                };
                System.out.print(s);
            }
            System.out.print("|\n");
        }
    }

    private boolean isDirWin(int x, int y, int xDir, int yDir){
        int s = board[x][y];
        for (int mv = 0; mv < 5; mv++){
            if ((x + xDir * mv >= width)
                    || (x + xDir * mv < 0)
                    || (y + yDir * mv >= height)
                    || (y + yDir * mv < 0)){
                return false;
            }
            if (board[x+xDir * mv][y + yDir * mv] != s){
                return false;
            }
        }
        return true;
    }

    private boolean isFull(){
        if (isFulled){
            return true;
        }
        for (int x = 0; x < width; x++){
            if (board[x][height - 1] == 0){
                return false;
            }
        }
        isFulled = true;
        return true;
    }

    private boolean isWin(int x, int y){
        if (isDirWin(x, y, 0, -1)){
            return true; //down
        }
        if (isDirWin(x, y, 1, -1)){
            return true; //LowerRight
        }
        if (isDirWin(x, y, -1, -1)){
            return true; //LowerLeft
        }

        if (isDirWin(x, y, 1, 0)){
            return true; //Right
        }
        if (isDirWin(x, y, 1, 1)){
            return true; //UpperRight
        }
        if (isDirWin(x, y, -1, 0)){
            return true; //Left
        }
        if (isDirWin(x, y, -1, 1)){
            return true; //UpperLeft
        }
        return false;
    }

    public boolean validRow(int x){
        return board[x][height - 1] == 0;
    }

    public boolean place(int x){

        if (x < 0 || x >= width){
            System.out.println("Out of bound");
            return false;
        }

        if (!validRow(x)){
            System.out.println("Invalid");
            return false;
        }

        if (isFinished){
            System.out.println("Finished");
            return false;
        }

        String s = switch (nextPlayer) {
            case 1 -> "🟥";
            case 2 -> "🟦";
            default -> "　";
        };

        int y = height - 1;
        while (y > 0){
            if (board[x][y-1] == 0){
                y--;
            } else {
                break;
            }
        }

        System.out.printf("Putted %s at [%d, %d]\n", s, x, y);
        board[x][y] = nextPlayer;
        printBoard();

        if(isWin(x, y)){
            System.out.printf("Player #%d(%s) WIN!\n", nextPlayer, s);
            isFinished = true;
            return true;
        }
        if (isFull()){
            System.out.println("DRAW!");
            return true;
        }

        board[x][y] = nextPlayer;
        if (nextPlayer == 1){
            nextPlayer = 2;
        } else {
            nextPlayer = 1;
        }

        return true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void random() {

        if (isFinished || isFull()){
            System.out.println("Can't place");
            return;
        }

        Random random = new Random();
        int x = random.nextInt(width);
        while (!validRow(x)){
            x++;
            if (x == width){
                x = 0;
            }
        }
        place(x);
    }

    public static void main(String[] args) {
        Board b = new Board(7, 6);
        while (!b.isFull() && !b.isFinished){
            b.random();
        }
    }
}
