import java.util.Scanner;


public class HumanPlayer extends Player {

	Board b = new Board();
	public HumanPlayer(int playerNum) {
		super(playerNum);
		// TODO Auto-generated constructor stub
		b.initiateBoard();
	}

	@Override
	public Move getMove() {
		// TODO Auto-generated method stub
		Scanner sc = SingletonScanner.getInstance();

		Move move = null;
		while(move==null){
			System.out.println("Player" +" " + playerNum + ": Enter row and column position to place piece");
			String row = sc.next();
			int col = sc.nextInt();
			//			System.out.println(input.charAt(0));
			//			System.out.println(((int) (input.charAt(1))));
			System.out.println(row.charAt(0) + " " + col);
			move = new Move(row.charAt(0), col);
			if(!b.isLegal(move, playerNum)){
				move = null;
			}
			//			 else if(input.length() == 4){
			//				move = new Move(input.charAt(0), (int)input.charAt(1), input.charAt(2), (int)input.charAt(3));
			//			}
		}

		b.placeMove(move, playerNum);
		//		b.printBoard();
		return move;
	}

	public boolean hasWon(int playerNum) {
		return b.hasWon(playerNum);
	}
	
	public void printBoard(){
		b.printBoard();
	}

	@Override
	public void OpponentMove(Move m) {
		if(playerNum == 1){
			b.placeMove(m, 2);			
		} else{
			b.placeMove(m, 1);
		}

	}

	public static void main(String args[]){
		Scanner sc = SingletonScanner.getInstance();
		String a = "A5";
		System.out.println(a.charAt(0));
		System.out.println(a.charAt(1));
	}

}
