import java.util.Scanner;


public class Network {
	
	/*
	 * 
	 */
	public void initiateGame(){
		Scanner sc = SingletonScanner.getInstance();
		int numPlayers = -1;
		while(numPlayers == -1){
			System.out.println("Enter number of players (0, 1, or 2): ");
			numPlayers = sc.nextInt();
			if (numPlayers == 0){
				ComputerPlayer player1 = new ComputerPlayer(1);
				ComputerPlayer player2 = new ComputerPlayer(2);
				playGame(player1, player2);

			} else if (numPlayers == 1){
				HumanPlayer player1 = new HumanPlayer(1);
				ComputerPlayer player2 = new ComputerPlayer(2);
				playGame(player1, player2);

			} else if (numPlayers == 2){
				System.out.println("true");
				HumanPlayer player1 = new HumanPlayer(1);
				HumanPlayer player2 = new HumanPlayer(2);
				playGame(player1, player2);

			} else{
				
				System.out.println("Invalid number of players.");
			}
		}
		
		
	}
	
	public void playGame(Player player1, Player player2){

		while(true){
			Move player1move1 = player1.getMove();
			player2.OpponentMove(player1move1);
			if (player1.hasWon(1))
				//TODO:  print win
				break;
				
			Move player2move1 = player2.getMove();
			player1.OpponentMove(player2move1);
			if (player2.hasWon(2))
				//TODO:  print win
				break;
			player1.printBoard();
			player2.printBoard();
			
		}

		
	}
	
	public static void main(String[] args){
		Network n = new Network();
		n.initiateGame();
		
	}

}
