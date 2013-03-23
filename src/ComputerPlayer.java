import java.util.Random;



public class ComputerPlayer extends Player {


	Board b = new Board();
	int longestPath = 1;

	public ComputerPlayer(int playerNum) {
		super(playerNum);
		b.initiateBoard();
	}

	private boolean computeLongestLength(int playerNum, int x, int y, int length, Board.Direction dir){
		Board.PositionState player = playerNum == 1 ? Board.PositionState.MaxPlayer : Board.PositionState.MinPlayer;
		Board.PositionState opp = playerNum == 1 ? Board.PositionState.MinPlayer : Board.PositionState.MaxPlayer;
		b.visited[x][y] = true;
		if (playerNum == 1 && x == 7){
			if(length >= 6){
				if (longestPath < length)
					longestPath = length;
				return true;
			}
		}

		if (playerNum == 2 && y == 7){
			if(length >= 6){
				if (longestPath < length)
					longestPath = length;
				return true;
			}
		}
		//South:

		if (dir != Board.Direction.S){
			for(int i = y + 1; i < 8; i++){
				if(b.gameGrid[x][i] == opp){
					break;
				}
				if(b.gameGrid[x][i] == player && !b.visited[x][i]){
					boolean tmp = computeLongestLength(playerNum, x, i, length + 1, Board.Direction.S);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//southwest

		if (dir != Board.Direction.SW){
			for(int i = 0; x+i < 8 && y-i > 0; i++){
				if(b.gameGrid[x+i][y-i] == opp){
					break;
				}
				if(b.gameGrid[x+i][y-i] == player && !b.visited[x+i][y-i]){
					boolean tmp = computeLongestLength(playerNum, x+i, y-i, length + 1, Board.Direction.SW);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//southeast

		if (dir != Board.Direction.SE){
			for(int i = 0; x+i < 8 && y+i < 8; i++){
				if(b.gameGrid[x+i][y+i] == opp){
					break;
				}
				if(b.gameGrid[x+i][y+i] == player && !b.visited[x+i][y+i]){
					boolean tmp = computeLongestLength(playerNum, x+i, y+i, length + 1, Board.Direction.SE);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		//north

		if (dir != Board.Direction.N){
			for(int i = 0; x-i > 0; i++){
				if(b.gameGrid[x-i][y] == opp){
					break;
				}
				if(b.gameGrid[x-i][y] == player && !b.visited[x-i][y]){
					boolean tmp = computeLongestLength(playerNum, x-i, y, length + 1, Board.Direction.N);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		if (dir != Board.Direction.NW){
			for(int i = 0; x-i > 0 && y - i > 0; i++){
				if(b.gameGrid[x-i][y-i] == opp){
					break;
				}
				if(b.gameGrid[x-i][y - i] == player && !b.visited[x-i][y-i]){
					boolean tmp = computeLongestLength(playerNum, x-i, y-i, length + 1, Board.Direction.NW);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}
		}

		if (dir != Board.Direction.NE){
			for(int i = 0; x-i > 0 && y + i < 8; i++){
				if(b.gameGrid[x-i][y+i] == opp){
					break;
				}
				if(b.gameGrid[x-i][y + i] == player && !b.visited[x-i][y+i]){
					boolean tmp = computeLongestLength(playerNum,x-i, y+i, length + 1, Board.Direction.NE);
					if(!tmp){  
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		if (dir != Board.Direction.W){
			for(int i = 0; y - i > 0; i++){
				if(b.gameGrid[x][y-i] == opp){
					break;
				}
				if(b.gameGrid[x][y-i] == player && !b.visited[x][y-i]){
					boolean tmp = computeLongestLength(playerNum, x, y-i, length + 1, Board.Direction.W);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		if (dir != Board.Direction.E){
			for(int i = 0; y + i < 8; i++){
				if(b.gameGrid[x][y+i] == opp){
					break;
				}
				if(b.gameGrid[x][y+i] == player && !b.visited[x][y+i]){
					boolean tmp = computeLongestLength(playerNum, x, y+i, length + 1, Board.Direction.E);
					if(!tmp){
						b.visited[x][y] = false;
					}
					return tmp;
				}
			}

		}

		if (length > longestPath)
			longestPath = length;
		return false;

	}
	private int evalFunction(int playerNum){
		//win for us: +10
		//win for opponent: -10
		//difference of length of our longest path and
		//length of opponents longest path

		//our longest path
		longestPath = 1;
		if(playerNum == 1){
			for(int i = 1; i < 7; i++){
				if(b.gameGrid[0][i] == Board.PositionState.MaxPlayer){
					b.resetVisited(0, i);
					computeLongestLength(playerNum, 0, i, 1, Board.Direction.I);
				}
			}
		}
		if(playerNum == 2){
			for (int i = 1; i < 7; i++){
				if (b.gameGrid[i][0] == Board.PositionState.MinPlayer){
					b.resetVisited(i,0);
					computeLongestLength(playerNum, i, 0, 1, Board.Direction.I);
				}
			}
		}
		//store current player longest path
		int myLongestPath = longestPath;

		//opponent longest path
		//reset longest path
		longestPath = 1;
		if(playerNum == 2){
			for(int i = 1; i < 7; i++){
				if(b.gameGrid[0][i] == Board.PositionState.MaxPlayer){
					b.resetVisited(0, i);
					computeLongestLength(1, 0, i, 1, Board.Direction.I);
				}
			}
		}
		if(playerNum == 1){
			for (int i = 1; i < 7; i++){
				if (b.gameGrid[i][0] == Board.PositionState.MinPlayer){
					b.resetVisited(i,0);
					computeLongestLength(2, i, 0, 1, Board.Direction.I);
				}
			}
		}
		int opponentLongestPath = longestPath;

		if (myLongestPath >= 6){
			return 10;			
		}
		if (opponentLongestPath >= 6){
			return -10;
			
		}

		return myLongestPath - opponentLongestPath;
	}

	public Move miniMax(int playerNum){
		return new Move('a',0);
	}
	
	
	
	public void printBoard(){
		b.printBoard();
	}
	
	public boolean hasWon(int playerNum) {
		return b.hasWon(playerNum);
	}
	
	public Move getMove() {
		//all rows ok...Columns 2-7 ok
		char row = 'A';
		int col = 0;
		Random r = new Random();
		Move m = null;

		do{
			if(playerNum == 1){
				String possibleRows = "ABCDEFGH";
				row = possibleRows.charAt(r.nextInt(possibleRows.length()));
				col = 2 + (int)(Math.random() * ((7 - 2) + 1));
				m = new Move(row, col);
			} else{
				String possibleRows = "BCDEFG";
				row = possibleRows.charAt(r.nextInt(possibleRows.length()));
				col = 1 + (int)(Math.random() * ((8 - 1) + 1));	
				m = new Move(row, col);
			}

		} while(!(b.isLegal(m, playerNum)));

		return m;
	}

	@Override
	public void OpponentMove(Move m) {
		if(playerNum == 1){
			b.placeMove(m, 2);

		} else{
			b.placeMove(m, 1);
		}

	}

	public static void main(String[] args){
		String possibleRows = "ABCDEFGH";

		Random r = new Random();
		int col = 1 + (int)(Math.random() * ((8 - 1) + 1));
		char row = possibleRows.charAt(r.nextInt(possibleRows.length()));
		System.out.println(row +" "+ col);
	}

}
