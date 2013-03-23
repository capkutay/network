Network
=======
In this project you will implement a program that plays the game Network
against a human player or another computer program.  Network is played on an
8-by-8 board.  There are two players, "Black" and "White."  Each player has ten
chips of its own color to place on the board.  White moves first.

                 -----------------------------------------
                 |    | 10 | 20 | 30 | 40 | 50 | 60 |    |
                 -----------------------------------------
                 | 01 | 11 | 21 | 31 | 41 | 51 | 61 | 71 |
                 -----------------------------------------
                 | 02 | 12 | 22 | 32 | 42 | 52 | 62 | 72 |
                 -----------------------------------------
                 | 03 | 13 | 23 | 33 | 43 | 53 | 63 | 73 |
                 -----------------------------------------
                 | 04 | 14 | 24 | 34 | 44 | 54 | 64 | 74 |
                 -----------------------------------------
                 | 05 | 15 | 25 | 35 | 45 | 55 | 65 | 75 |
                 -----------------------------------------
                 | 06 | 16 | 26 | 36 | 46 | 56 | 66 | 76 |
                 -----------------------------------------
                 |    | 17 | 27 | 37 | 47 | 57 | 67 |    |
                 -----------------------------------------

The board has four goal areas:  the top row, the bottom row, the left column,
and the right column.  Black's goal areas are squares 10, 20, 30, 40, 50, 60
and 17, 27, 37, 47, 57, 67.  Only Black may place chips in these areas.
White's goal areas are 01, 02, 03, 04, 05, 06 and 71, 72, 73, 74, 75, 76; only
White may play there.  The corner squares--00, 70, 07, and 77--are dead;
neither player may use them.  Either player may place a chip in any square not
on the board's border.

Object of Play
==============
Each player tries to complete a "network" joining its two goal areas.
A network is a sequence of six or more chips that starts in one of the player's
goal areas and terminates in the other.  Each consecutive pair of chips in the
sequence are connected to each other along straight lines, either orthogonally
(left, right, up, down) or diagonally.

The diagram below shows a winning configuration for Black.  (There should be
White chips on the board as well, but for clarity these are not shown.)  Here
are two winning black networks.  Observe that the second one crosses itself.

    60 - 65 - 55 - 33 - 35 - 57
    20 - 25 - 35 - 13 - 33 - 55 - 57

                 -----------------------------------------
                 |    |    | BB |    |    |    | BB |    | _0
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _1
                 -----------------------------------------
                 |    |    |    |    | BB |    |    |    | _2
                 -----------------------------------------
                 |    | BB |    | BB |    |    |    |    | _3
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _4
                 -----------------------------------------
                 |    |    | BB | BB |    | BB | BB |    | _5
                 -----------------------------------------
                 |    |    |    |    |    |    |    |    | _6
                 -----------------------------------------
                 |    |    | BB |    |    | BB |    |    | _7
                 -----------------------------------------
                   0_   1_   2_   3_   4_   5_   6_   7_

An enemy chip placed in the straight line between two chips breaks the
connection.  In the second network listed above, a white chip in square 56
would break the connection to Black's lower goal.

Although more than one chip may be placed in a goal area, a network can have
only two chips in the goal areas:  the first and last chips in the network.
Neither of the following are networks, because they both make use of two chips
in the upper goal.

    60 - 20 - 42 - 33 - 35 - 57
    20 - 42 - 60 - 65 - 55 - 57

A network cannot pass through the same chip twice, even if it is only counted
once.  For that reason the following is not a network.

    20 - 25 - 35 - 33 - 55 - 35 - 57

A network cannot pass through a chip without turning a corner (i.e. changing
direction).  Because of the chip in square 42, the following is not a network.

    60 - 42 - 33 - 35 - 25 - 27

Legal Moves
===========
To begin the game, choose who is Black and who is White in any manner (we use a
random number generator).  The players alternate taking turns, with White
moving first.

The first three rules of legal play are fairly simple.
  1)  No chip may be placed in any of the four corners. 
  2)  No chip may be placed in a goal of the opposite color.
  3)  No chip may be placed in a square that is already occupied.

The fourth rule is a bit trickier.
  4)  A player may not have more than two chips in a connected group, whether
      connected orthogonally or diagonally.

This fourth rule means that you cannot have three or more chips of the same
color in a cluster.  A group of three chips form a cluster if one of them is
adjacent to the other two.  In the following diagram, Black is not permitted to
place a chip in any of the squares marked with an X, because doing so would
form a group of 3 or more chips.  (Of course, the far left and right columns
are also off-limits to Black.)

                 -----------------------------------------
                 |    |  X |  X | BB |  X |    |    |    |
                 -----------------------------------------
                 |    |  X | BB |  X |  X |  X |  X |    |
                 -----------------------------------------
                 |    |  X |  X |  X |  X | BB |  X |    |
                 -----------------------------------------
                 |    |    |    |    |  X | BB |  X |    |
                 -----------------------------------------
                 |    |    | BB |    |  X |  X |  X |    |
                 -----------------------------------------
                 |    |  X |  X |    |    |    | BB |    |
                 -----------------------------------------
                 |    | BB |    |    |    |  X |    |    |
                 -----------------------------------------
                 |    |    |    |    | BB |    |    |    |
                 -----------------------------------------

There are two kinds of moves:  add moves and step moves.  In an add move, a
player places a chip on the board (following the rules above).  Each player has
ten chips, and only add moves are permitted until those chips are exhausted.
If neither player has won when all twenty chips are on the board, the rest of
the game comprises step moves.  In a step move, a player moves a chip to a
different square, subject to the same restrictions.  A player is not permitted
to decline to move a piece (nor to "move from square ij to square ij").

A step move may create a network for the opponent by unblocking a connection
between two enemy chips.  If the step move breaks the network at some other
point, the enemy does not win, but if the network is still intact when the chip
has been placed back on the board, the player taking the step move loses.  If
a player makes a move that causes both players to complete a network, the other
player wins.


Bibliographic note:  Network is taken from Sid Sackson, "A Gamut of Games,"
Dover Publications (New York), 1992.

Your Task
=========
Your job is to implement a MachinePlayer class that plays Network well.  One
subtask is to write a method that identifies legal moves; another subtask is to
write a method that finds a move that is likely to win the game.

The MachinePlayer class is in the player package and extends the abstract
Player class, which defines the following methods.

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove();

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m);

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m);

In addition to the methods above, implement two constructors for MachinePlayer.

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color)

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth)

As usual, do not change the signatures of any of these methods; they are your
interface to other players.  You may add helper methods.

Your MachinePlayer must record enough internal state, including the current
board configuration, so that chooseMove() can choose a good (or at the very
least, legal) move.  In a typical game, two players and a referee each have
their own internal representation of the board.  If all the implementations are
free of bugs, they all have the same idea of what the board looks like,
although each of the three uses different data structures.  The referee keeps
its own copy to prevent malicious or buggy players from cheating or corrupting
the board.  If your MachinePlayer is buggy and attempts to make an illegal
move, the referee will grant the win to your opponent.

Most of your work will be implementing chooseMove().  You will be implementing
the minimax algorithm for searching game trees, described in Lecture 17.
A game tree is a mapping of all possible moves you can make, and all possible
responses by your opponent, and all possible responses by you, and so on to a
specified "search depth."  You will NOT need to implement a tree data
structure; a "game tree" is the structure of a set of recursive method calls.

The forceMove() method forces your player to make a specified move.  It is for
testing and grading.  We can set up particular board configurations by
constructing a MachinePlayer and making an alternating series of forceMove()
and opponentMove() calls to put the board in the desired configuration.  Then
we will call chooseMove() to ensure that your MachinePlayer makes a good
choice.

The second MachinePlayer constructor, whose second parameter searchDepth is the
chosen search depth, is also used for debugging and testing your code.
A search depth of one implies that your MachinePlayer considers all the moves
and chooses the one that yields the "best" board.  A search depth of two
implies that you consider your opponent's response as well, and choose the move
that will yield the "best" board after your opponent makes the best move
available to it.  A search depth of three implies that you consider two
MachinePlayer moves and one opponent move between them.

The first MachinePlayer constructor should create a MachinePlayer whose search
depth you have chosen so that it always returns a move within five seconds.
(This precise time limit will be important only for the Network tournament late
in the semester.)  The second MachinePlayer constructor MUST create a
MachinePlayer that always searches to exactly the specified search depth.

You may want to design the MachinePlayer constructed by your first constructor
so that it searches to a variable depth.  In particular, you will almost
certainly want to reduce your search depth for step moves, because there are
many more possible step moves than add moves, and a search depth that is fast
for add moves will be very slow for step moves.

The Move class in Move.java is a container for storing the fields needed to
define one move in Network.  It is not an ADT and it has no interesting
invariants, so all its fields are public.  It is part of the interface of your
MachinePlayer, and it is how your MachinePlayer communicates with other
programs, so you cannot change Move.java in any way.  If you would like to have
additional methods or fields, feel free to extend the Move class; your
MachinePlayer may return subclasses of Move without any fear.

Strategy
========


Game trees rely on an "evaluation function" that assigns a score to each board
that estimates how well your MachinePlayer is doing.  An evaluation function is
necessary because it is rarely possible to search all the way to the end of the
game.  You need to estimate your odds of winning if you make a particular move.
Your evaluation function should assign a maximum positive score to a win by
your MachinePlayer, and a minimum negative score to a win by the opponent.

Assign an intermediate score to a board where neither player has completed a
network.  One of the most important but difficult parts of implementing game
search is inventing a board evaluation function that reliably evaluates these
intermediate boards.  For example, a rough evaluation function might count how
many pairs of your chips can see each other, and subtract the opponent's pairs.
A slightly better evaluation function would also try to establish at least one
chip in each goal early in the game.  I leave you to your own wits to improve
upon these ideas.

You should assign a slightly higher score to a win in one move than to a win in
three moves, which should get a higher score that a win in five moves, and so
on.  Otherwise, your MachinePlayer might always choose the win in three over
the win in one, move after move, and never get around to actually winning.


