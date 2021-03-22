/****
 * A class to store and traverse a maze
 * @author esahe2
 *
 */

import java.util.*;

public class Maze {

    /**
     * Two dimensional array to represent a maze
     */
    private char[][] maze;

    /**
     * Constructor initializing the maze array
     *
     * @param maze
     */
    public Maze(char[][] maze) {
        this.maze = maze;
    }

    /**
     * You need to implement this method
     *
     * @param start: The start Position in the maze
     * @param goal:  The goal Position in the maze
     * @return An array of Position which stores a solution to the maze. If a solution is not found a null value should be returned.
     */
    public Position[] traversewithStack(Position start, Position goal) {
        /* create new maze */
        char[][] maze_clone = mazeCopy(maze);
        /* checks if start and goal are valid positions */
        if (!validInput(start, goal)) {
            System.out.println("Invalid start or goal positions");
            return null;
        }
        /* markers for locations */
        char visited = 'v';
        char blocked = 'x';
        /* create a new stack and add start position to it */
        Stack<Position> stack = new Stack<Position>();
        stack.push(start);

        /* loops until start equals goal and stack is not empty */
        while (!start.equals(goal) && !stack.empty()) {
            int currentRow = start.getRow(); // holds starting row #
            int currentCol = start.getColumn(); // holds starting col #
            /* check if can go left, right, up, down or must backtrack */
            if (!deadEnd(currentRow, currentCol - 1)) {
                maze_clone[currentRow][currentCol] = visited; // mark position as visited
                start = new Position(currentRow, currentCol - 1); // create a new position at left coordinate
                stack.push(start); // push it onto the stack
            } else if (!deadEnd(currentRow, currentCol + 1)) {
                maze_clone[currentRow][currentCol] = visited;
                start = new Position(currentRow, currentCol + 1); // create new position at right coordinate
                stack.push(start);
            } else if (!deadEnd(currentRow + 1, currentCol)) {
                maze_clone[currentRow][currentCol] = visited;
                start = new Position(currentRow + 1, currentCol); // create new position at top coordinate
                stack.push(start);
            } else if (!deadEnd(currentRow - 1, currentCol)) {
                maze_clone[currentRow][currentCol] = visited;
                start = new Position(currentRow - 1, currentCol); // create new position at bottom coordinate
                stack.push(start);
            } else {
                maze_clone[currentRow][currentCol] = blocked; // set current position as blocked
                stack.pop(); // pop off the top position of the stack
                if (stack.isEmpty()) // if no where else to go return null
                    return null;
                else
                    start = stack.peek(); // set start to the next position on stack
            }
        }

        Position[] route = new Position[stack.size()]; // crete new array to hold the route

        return stack.toArray(route); // return the array
    }

    /**
     * You need to implement this method
     *
     * @param start: The start Position in the maze
     * @param goal:  The goal Position in the maze
     * @return An array of Position which stores a solution to the maze. If a solution is not found a null value should be returned.
     */
    public Position[] traverseRecursive(Position start, Position goal) {
        /* create new maze */
        char[][] maze_clone = mazeCopy(maze);
        /* check if start and goal are valid */
        if (!validInput(start, goal)) {
            System.out.println("Invalid start or goal positions");
            return null;
        }

        Stack<Position> stack = new Stack<Position>(); // create a new empty stack
        stack.push(start); // add the start position to the stack
        stack = recursiveHelper(stack, start, goal, maze_clone); // call the recursive helper method

        Position[] route = new Position[stack.size()]; // create an array to hold route

        return (stack.empty() ? null : stack.toArray(route)); // checks if array is empty and returns result
    }

    private Stack<Position> recursiveHelper(Stack<Position> stack, Position start, Position end, char[][] maze) {
        char visited = 'v'; // holds char of visited
        char blocked = 'x'; // holds char of blocked

        if (start.equals(end)) { // base case
            return stack;
        }

        if (stack.empty()) { // base case
            return null;
        }

        int currentRow = start.getRow(); // start row
        int currentCol = start.getColumn(); // start col

        /* check left, right, up, down or must backtrack */
        if (!deadEnd(currentRow, currentCol - 1)) {
            maze[currentRow][currentCol] = visited; // mark current position as visited
            start = new Position(currentRow, currentCol - 1); // create a new Position at left coordinate
            stack.add(start); // add to stack
            recursiveHelper(stack, start, end, maze); // call the method using left coordinate as start
        } else if (!deadEnd(currentRow, currentCol + 1)) {
            maze[currentRow][currentCol] = visited;
            start = new Position(currentRow, currentCol + 1); // create a new Position at right coordinate
            stack.add(start);
            recursiveHelper(stack, start, end, maze); // call the method using right coordinate as start
        } else if (!deadEnd(currentRow + 1, currentCol)) {
            maze[currentRow][currentCol] = visited;
            start = new Position(currentRow + 1, currentCol); // create a new Position at top coordinate
            stack.add(start);
            recursiveHelper(stack, start, end, maze); // call the method using top coordinate as start
        } else if (!deadEnd(currentRow - 1, currentCol)) {
            maze[currentRow][currentCol] = visited;
            start = new Position(currentRow - 1, currentCol); // create a new Position at bottom coordinate
            stack.add(start);
            recursiveHelper(stack, start, end, maze); // call the method using bottom coordinate as start
        } else {
            maze[currentRow][currentCol] = blocked; // current position is blocked
            stack.pop(); // pop top position off stack
            if (!stack.empty()) // if stack isn't empty top is new start
                start = stack.peek();

            recursiveHelper(stack, start, end, maze); // call the method with backtracked position as start
        }

        return stack;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean validInput(Position start, Position end) {
        int startRow = start.getRow();
        int startCol = start.getColumn();
        int endRow = end.getRow();
        int endCol = end.getColumn();

        if (startRow < 0 || startRow > maze.length || startCol < 0 || startCol > maze[0].length
                || maze[startRow][startCol] == 'x')
            return false;

        return endRow >= 0 && endRow <= maze.length && endCol >= 0 && endCol <= maze[0].length
                && maze[endRow][endCol] != 'x';
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean deadEnd(int row, int col) {
        char[] deadCh = {'x', 'v'};
        char mazeChar;

        try {
            mazeChar = maze[row][col];
            if (mazeChar == deadCh[0] || mazeChar == deadCh[1])
                return true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return true;
        }

        return false;
    }

    /* creates a copy of the maze */
    private char[][] mazeCopy(char[][] maze) {
        char[][] copy = new char[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++)
            System.arraycopy(maze[i], 0, copy[i], 0, maze[i].length);

        return copy;
    }
}
