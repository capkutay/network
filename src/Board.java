import java.awt.Point;
import java.io.PrintWriter;
import java.util.ArrayList;




public class Board {

	public enum PositionState {Empty, MaxPlayer, MinPlayer};
	public enum Direction {I, N, NE, E, SE, S, SW, W, NW}; 
	PositionState[][] gameGrid = new PositionState[8][8];
	private int[][] player1Neighbors = new int[8][8];
	private int[][] player2Neighbors = new int[8][8];
	boolean[][] visited = new boolean[8][8];


	boolean isLegal(Move move, int playerNum) {
		/*
		 * a piece is already in that location, placing a piece at that location would form a cluster of 3 
		 * or more pieces, the location is in opponent's goal, the location is in corner, 
		 * move would create network for both players
		 */

		if(gameGrid[move.toRow-'A'][move.toCol-1] != PositionState.Empty){
			System.out.println("Invalid move: There is already a piece in that position.");
			return false;
		}
		if((move.moveType != Move.MoveType.PLACE_PIECE)){
			if(gameGrid[move.fromRow-'A'][move.fromCol-1] != player(playerNum)){
				System.out.println();
				return false;
			}
		}
		//check if in other players goal
		if (playerNum == 1){
			if(move.toCol == 1 || move.toCol == 8){
				System.out.println("Invalid move: Piece lands in opponents goal.");
				return false;
			}
		} else {
			if(move.toRow == 'A' || move.toRow == 'H'){
				System.out.println("Invalid move: Piece lands in opponents goal.");
				return false;
			}
		}
		//check if corner case
		if(move.toCol == 1 || move.toCol == 8){
			if(move.toRow == 'A' || move.toRow == 'H'){
				System.out.println("Invalid move: Piece lands in corner.");
				return false;
			}	
		}

		if(createsCluster(move, playerNum)){
			System.out.println("Invalid move: Creates cluster.");
			return false;
		}
		if (createsDualWin(move, playerNum)){
			System.out.println("Invalid move: Creates dual win.");
			return false;			
		}

		return true;
	}
	/*
	 * store the board in initial state, make move, if player 1
	 */

	private boolean createsDualWin(Move move, int playerNum){
//		if(!isLegal(move, playerNum)){
//			return false;
//		}
		
		


		boolean createsOpponentWin = false;		
		//store initial state of the board
		PositionState tmp = gameGrid[move.toRow-'A'][move.toCol-1];

		if (move.moveType == Move.MoveType.PLACE_PIECE){
			//place piece
			//if player1 is making move, check to see if it creates network for player 2
			if(playerNum==1){
				gameGrid[move.toRow - 'A'][move.toCol - 1] = PositionState.MaxPlayer;
				for (int i = 1; i < 7; i++){
					if (gameGrid[i][0] == PositionState.MinPlayer){
						resetVisited(i,0);
						createsOpponentWin = isNetwork(2, i, 0, 1, Direction.I);
						if(createsOpponentWin){
							break;					
						}
					}
				}
				if (isWinning(move, playerNum) && createsOpponentWin){
					//set board back to initial state
					gameGrid[move.toRow-'A'][move.toCol-1] = tmp;
					return true;
				}

				//if player 2, check to see if move creates network
			}
			//if player 2, check to see if move creates network for player1
			if (playerNum==2){
				gameGrid[move.toRow - 'A'][move.toCol - 1] = PositionState.MinPlayer;
				for(int i = 1; i < 7; i++){
					if(gameGrid[0][i] == PositionState.MaxPlayer){
						resetVisited(0, i);
						createsOpponentWin = isNetwork(1, 0, i, 1, Direction.I);
						if(createsOpponentWin){
							break;
						}
					}
				}
				if(isWinning(move, playerNum) && createsOpponentWin){
					//set board back to initial state
					gameGrid[move.toRow-'A'][move.toCol-1] = tmp;
					return true;
				}
				//restore initial state of board
			}
			gameGrid[move.toRow-'A'][move.toCol-1] = tmp;		

		} else if (move.moveType == Move.MoveType.MOVE_PIECE){
			PositionState fromTmp = gameGrid[move.fromRow - 'A'][move.fromCol-1];
			gameGrid[move.fromRow - 'A'][move.fromCol-1] = PositionState.Empty;
			gameGrid[move.toRow - 'A'][move.toCol -1] = fromTmp;
			//update board

			if(playerNum == 1){
				//check to see if it creates network for player 2
				for (int i = 1; i < 7; i++){
					if (gameGrid[i][0] == PositionState.MinPlayer){
						resetVisited(i,0);
						createsOpponentWin = isNetwork(2, i, 0, 1, Direction.I);
						if(createsOpponentWin){
							break;					
						}
					}
				}
				if (isWinning(move, playerNum) && createsOpponentWin){
					gameGrid[move.toRow - 'A'][move.toCol - 1] = tmp;
					gameGrid[move.fromRow - 'A'][move.fromCol -1] = fromTmp;
					return true;
				}
			}


			if (playerNum==2){

				for(int i = 1; i < 7; i++){
					if(gameGrid[0][i] == PositionState.MaxPlayer){
						resetVisited(0, i);
						createsOpponentWin = isNetwork(1, 0, i, 1, Direction.I);
						if(createsOpponentWin){
							break;
						}
					}
				}
				if(isWinning(move, playerNum) && createsOpponentWin){
					gameGrid[move.toRow-'A'][move.toCol-1] = tmp;
					gameGrid[move.fromRow - 'A'][move.fromCol-1] = tmp;
					return true;
				}
				
			}
			gameGrid[move.toRow-'A'][move.toCol-1] = tmp;
			gameGrid[move.fromRow - 'A'][move.fromCol-1] = tmp;
		}
		return false;
	}



	private boolean createsCluster(Move move, int playerNum){
		//TODO:
		int toLocalCount = 0;
		int toRowIndex = move.toRow-'A';
		int toColIndex = move.toCol-1;

		if(move.moveType == Move.MoveType.PLACE_PIECE){
			toLocalCount = (playerNum == 1 ? player1Neighbors[move.toRow-'A'][move.toCol-1] :
				player2Neighbors[move.toRow-'A'][move.toCol-1]);

		} else if(move.moveType == Move.MoveType.MOVE_PIECE){
			//keeping track of original from piece and remove it temporarily
			PositionState tmp = gameGrid[move.fromRow-'A'][move.fromCol-1]; 
			gameGrid[move.fromRow-'A'][move.fromCol-1] = PositionState.Empty;

			modifyNeighborCount(playerNum, move.fromRow-'A', move.fromCol-1, false);
			toLocalCount = (playerNum == 1 ? player1Neighbors[toRowIndex][toColIndex] :
				player2Neighbors[toRowIndex][toColIndex]);

			modifyNeighborCount(playerNum, move.fromRow-'A', move.fromCol-1, true);
			//setting it back to initial state
			gameGrid[move.fromRow-'A'][move.fromCol-1] = tmp;

		}

		if(toLocalCount == 0){
			return false;
		}
		else if(toLocalCount == 2){
			System.out.println("Move creates cluster.");
			return true;
		}
		return hasConnectedNeighbor(playerNum, toRowIndex, toColIndex);
	}

	private boolean hasConnectedNeighbor(int playerNum, int rowIndex,
			int colIndex) {
		for (int i = rowIndex - 1; i <= rowIndex + 1; i++ ){
			if(i < 0 || i > 7){
				continue;
			}
			for(int j = colIndex - 1; j <= colIndex + 1; j++){
				if(j < 0 || j > 7 || (i == rowIndex && j == colIndex)){
					continue;
				}
				if(playerNum == 1){
					if (gameGrid[rowIndex][colIndex] == PositionState.MaxPlayer){
						if (player1Neighbors[rowIndex][colIndex] > 0){
							return true;
						}
					}
				} else if(playerNum == 2){
					if (gameGrid[rowIndex][colIndex] == PositionState.MinPlayer){
						if (player2Neighbors[rowIndex][colIndex] > 0){
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	boolean hasWon(int playerNum) {
		boolean win = false;
		if(playerNum == 1){
			for(int i = 1; i < 7; i++){
				if(gameGrid[0][i] == PositionState.MaxPlayer){
					resetVisited(0, i);
					win = isNetwork(playerNum, 0, i, 1, Direction.I);
					if(win){
						return true;
					}
				}
			}
		}
		if(playerNum == 2){
			for (int i = 1; i < 7; i++){
				if (gameGrid[i][0] == PositionState.MinPlayer){
					resetVisited(i,0);
					win = isNetwork(playerNum, i, 0, 1, Direction.I);
					if(win){
						return true;
					}
				}
			}
		}
		return false;
	}
	boolean isWinning(Move move, int playerNum) {

		if(move.moveType == Move.MoveType.PLACE_PIECE){

			gameGrid[move.toRow - 'A'][move.toCol - 1] = player(playerNum);

		} else if(move.moveType == Move.MoveType.MOVE_PIECE){
			gameGrid[move.fromRow - 'A'][move.fromCol - 1] = PositionState.Empty;
			gameGrid[move.toRow - 'A'][move.toCol -1] = player(playerNum);
		}
		boolean isWin = hasWon(playerNum);
		//reset board

		if(move.moveType == Move.MoveType.PLACE_PIECE){

			gameGrid[move.toRow - 'A'][move.toCol -1] = PositionState.Empty;

		} else if(move.moveType == Move.MoveType.MOVE_PIECE){
			gameGrid[move.toRow - 'A'][move.toCol - 1] = PositionState.Empty;
			gameGrid[move.fromRow - 'A'][move.fromCol -1] = player(playerNum);
		}
		if(isWin){
			System.out.println("Win for Player " + playerNum);
			return true;
		} else{
			return false;			
		}
	}

	boolean isNetwork(int playerNum, int x, int y, int length, Direction dir){
		PositionState player = playerNum == 1 ? PositionState.MaxPlayer : PositionState.MinPlayer;
		PositionState opp = playerNum == 1 ? PositionState.MinPlayer : PositionState.MaxPlayer;
		visited[x][y] = true;
		if (playerNum == 1 && x == 7){
			if(length >= 6){
				return true;
			}
		}

		if (playerNum == 2 && y == 7){
			if(length >= 6){
				return true;
			}
		}
		//South:

		if (dir != Direction.S){
			for(int i = y + 1; i < 8; i++){
				if(gameGrid[x][i] == opp){
					break;
				}
				if(gameGrid[x][i] == player && !visited[x][i]){
					boolean tmp = isNetwork(playerNum, x, i, length + 1, Direction.S);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//southwest

		if (dir != Direction.SW){
			for(int i = 0; x+i < 8 && y-i > 0; i++){
				if(gameGrid[x+i][y-i] == opp){
					break;
				}
				if(gameGrid[x+i][y-i] == player && !visited[x+i][y-i]){
					boolean tmp = isNetwork(playerNum, x+i, y-i, length + 1, Direction.SW);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//southeast

		if (dir != Direction.SE){
			for(int i = 0; x+i < 8 && y+i < 8; i++){
				if(gameGrid[x+i][y+i] == opp){
					break;
				}
				if(gameGrid[x+i][y+i] == player && !visited[x+i][y+i]){
					boolean tmp = isNetwork(playerNum, x+i, y+i, length + 1, Direction.SE);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//north

		if (dir != Direction.N){
			for(int i = 0; x-i > 0; i++){
				if(gameGrid[x-i][y] == opp){
					break;
				}
				if(gameGrid[x-i][y] == player && !visited[x-i][y]){
					boolean tmp = isNetwork(playerNum, x-i, y, length + 1, Direction.N);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		if (dir != Direction.NW){
			for(int i = 0; x-i > 0 && y - i > 0; i++){
				if(gameGrid[x-i][y-i] == opp){
					break;
				}
				if(gameGrid[x-i][y - i] == player && !visited[x-i][y-i]){
					boolean tmp = isNetwork(playerNum, x-i, y-i, length + 1, Direction.NW);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		if (dir != Direction.NE){
			for(int i = 0; x-i > 0 && y + i < 8; i++){
				if(gameGrid[x-i][y+i] == opp){
					break;
				}
				if(gameGrid[x-i][y + i] == player && !visited[x-i][y+i]){
					boolean tmp = isNetwork(playerNum, x-i, y+i, length + 1, Direction.NE);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		if (dir != Direction.W){
			for(int i = 0; y - i > 0; i++){
				if(gameGrid[x][y-i] == opp){
					break;
				}
				if(gameGrid[x][y-i] == player && !visited[x][y-i]){
					boolean tmp = isNetwork(playerNum, x, y-i, length + 1, Direction.W);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		if (dir != Direction.E){
			for(int i = 0; y + i < 8; i++){
				if(gameGrid[x][y+i] == opp){
					break;
				}
				if(gameGrid[x][y+i] == player && !visited[x][y+i]){
					boolean tmp = isNetwork(playerNum, x, y+i, length + 1, Direction.E);
					if(!tmp){
						visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		return false;
	}

	void resetVisited(int xStart, int yStart) {

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				visited[i][j] = false;
			}
		}
		if(xStart == 0){
			for(int i = 0; i < 8; i++){
				visited[0][i] = true;
			}
			visited[0][yStart] = false;
		}
		if(yStart ==0){
			for(int i = 0; i < 8; i++){
				visited[i][0] = true;
			}
			visited[xStart][0] = false;
			//TODO: same for y coordinate
		}
	}



	//	private boolean currentPostionHasNetwork(int playerNum){
	//		class Node {
	//			Point position;
	//			ArrayList<Point> excludedPositions;
	//			Direction direction;
	//			Node(Point pt, ArrayList<Point> list, Direction dir) {
	//				position = pt;
	//				excludedPositions = list;
	//				direction = dir;
	//			}
	//		} //first check to see if there are pieces in goal and target region
	//		boolean hasPiece = false;
	//		if (playerNum == 1){
	//			for (int i = 1; i < 7; i++){
	//				if(gameGrid[0][i] == PositionState.MaxPlayer){
	//					hasPiece = true;
	//					break;
	//				}
	//			} 
	//			if (!hasPiece){
	//				return false;
	//			}
	//			hasPiece = false;
	//			for (int j = 1; j < 7; j++){
	//				if (gameGrid[7][j] == PositionState.MaxPlayer){
	//					hasPiece = true;
	//					break;
	//				}
	//			}
	//			if (!hasPiece) {
	//				return false;
	//			}
	//		}
	//		else{
	//			//TODO: check if there are pieces in goal and target region for PLAYER 2
	//			for (int i = 1; i < 7; i++){
	//				if (gameGrid[i][0] == PositionState.MinPlayer){
	//					hasPiece = true;
	//					break;
	//				}
	//			}
	//			if (!hasPiece){
	//				return false;
	//			}
	//			hasPiece = true;
	//			for(int j = 1; j < 7; j++){
	//				if(gameGrid[j][7] == PositionState.MinPlayer){
	//					hasPiece = true;
	//					break;
	//				}
	//			}
	//			if(!hasPiece){
	//				return false;
	//			}
	//		}
	//		if (playerNum == 1){
	//			for(int i = 1; i < 7; i++){
	//				if (gameGrid[0][i] == PositionState.MaxPlayer){
	//					//create initial excludedPosition list
	//					//includes any piece in the top row
	//
	//					ArrayList<Point> excludedList = new ArrayList<Point>();
	//					for (int j = 1; j < 7; j++){
	//						//add every position in top row other than initial piece
	//						if (j != i && gameGrid[0][j] == PositionState.MaxPlayer)
	//							excludedList.add(new Point(0,j));
	//					}
	//					Node root = new Node(new Point(0,i), excludedList, Direction.I);
	//				}
	//				//find excluded positions in next row
	//
	//			}
	//		}
	//
	//
	//		return false;
	//	}



	private PositionState player(int playerNum)
	{
		if (playerNum == 1)
			return PositionState.MaxPlayer;
		if (playerNum == 2)
			return PositionState.MinPlayer;
		return null;
	}


	boolean placeMove(Move move, int playerNum) {
		int rowIndex = move.toRow-'A';
		int colIndex = move.toCol-1;
//		if (!isLegal(move, playerNum))
//			return false;

		if (move.moveType == Move.MoveType.PLACE_PIECE)
		{
			gameGrid[rowIndex][colIndex] = player(playerNum);
			modifyNeighborCount(playerNum, rowIndex, colIndex, true);
		}
		else
		{
			gameGrid[move.fromRow-'A'][move.fromCol-1] = PositionState.Empty;
			modifyNeighborCount(playerNum, move.fromRow-'A', move.fromCol-1, false);
			gameGrid[rowIndex][colIndex] = player(playerNum);
			modifyNeighborCount(playerNum, rowIndex, colIndex, true);
		}

		return true;
	}

	private void modifyNeighborCount(int playerNum, int rowIndex, int colIndex, boolean increment) {

		for (int i = rowIndex - 1; i <= rowIndex + 1; i++ ){
			if(i < 0 || i > 7){
				continue;
			}
			for(int j = colIndex - 1; j <= colIndex + 1; j++){
				if(j < 0 || j > 7 || (i == rowIndex && j == colIndex)){
					continue;
				}
				if(playerNum == 1){
					if(increment){
						player1Neighbors[i][j]++;						
					} else{
						player1Neighbors[i][j]--;
					}
				} else if(playerNum == 2){
					if(increment){
						player2Neighbors[i][j]++;						
					} else{
						player2Neighbors[i][j]--;
					}
				}
			}
		}
	}

	public void initiateBoard(){
		for(int i = 0; i < 8; i ++){
			for(int j = 0; j < 8; j++){
				gameGrid[i][j] = PositionState.Empty;
			}
		}
	}

	public void printBoard(){
		System.out.print("   1 2 3 4 5 6 7 8");
		System.out.print("\n");

		for(int i = 0; i < 8; i++){
			System.out.print((char)('A' + i));
			System.out.print("  ");

			for(int j = 0; j < 8; j++){
				if(gameGrid[i][j] == PositionState.Empty){
					System.out.print(". ");
				}
				if(gameGrid[i][j] == PositionState.MaxPlayer){
					System.out.print("1 ");
				}
				if(gameGrid[i][j] == PositionState.MinPlayer){
					System.out.print("2 ");
				}
			}
			System.out.print("\n");
		}	
	}

	public static void main(String[] args){
		Board b = new Board();
		b.initiateBoard();
		b.printBoard();
		b.isLegal(new Move('A', 4), 1);
		b.placeMove(new Move('A', 4), 1);
		b.placeMove(new Move('D', 3), 2);

		b.printBoard();

	}


}
