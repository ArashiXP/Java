// Given an arrayList of strings, we are tasked to convert them into coordinates and place them on a board and check if
// any of them (the queens) either rest on the same row, column, or diagonals.  Similar to how a queen in chess would work

//Imports
import java.util.*;

public class SneakyQueens
{
    // Given an ArrayList of coordinate strings representing the locations of 
    // the queens on a boardSize × boardSize chess board, return true if none
    // of the queens can attack one another. Otherwise, return false.
    public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize)
    {
        int i, j, letterLen = 0, curRow, curCol, curDiagUpper, curDiagLower;
        // Since the numbers are still part of the string I need to make a temporary string to hold it
        String curRowStr = "";
        // I made 4 arrays to hold each direction mostly because I am still not comfortable in using 
        // multidimensional arrays, its hard to wrap my head around
        int [] rows = new int[boardSize + 1];
        int [] cols = new int[boardSize + 1];
        // Diagonals each go in 2 directions, so gotta double it
        int [] upperDiagonal = new int[(boardSize * 2) + 1];
        int [] lowerDiagonal = new int[(boardSize * 2) + 1];

        // Iterate through each of the coordinate strings
        for (i = 0; i < coordinateStrings.size(); i++)
        {
            // Iterate through the characters in each string
            for (j = 0; j < coordinateStrings.get(i).length(); j++)
            {
                // If letter than increase the amount of letters
                if (Character.isLetter(coordinateStrings.get(i).charAt(letterLen)))
                    letterLen++;
                else
                    // else we concatenate the numbers
                    curRowStr += coordinateStrings.get(i).charAt(j);
            }// end of inner loop

            // Call helper function to convert the letters to a number and store in cols array
            curCol = stringToNumber(coordinateStrings.get(i), letterLen);
            // Turn the concatenated number into an actual number and store in row array
            curRow = Integer.parseInt(curRowStr);

            // Check Rows
            // If the current row or column has any one already in it, we have a kill
            if (rows[curRow] == 1 || cols[curCol] == 1)
                return false;
            // If not, then we mark the row and column indexes
            rows[curRow] = 1;
            cols[curCol] = 1;

            // Universal formula to find similar diagonals
            // For postive or upper diagonals:  row - column + n, where n = size of grid
            // For negative or lower diagonals: row + column
            curDiagUpper = curRow - curCol + boardSize;
            curDiagLower = curRow + curCol;

            // Check the diagonals
            // If any diagonal is filled, whether it be upper or lower, we have a kill
            if (upperDiagonal[curDiagUpper] == 1 || lowerDiagonal[curDiagLower] == 1)
                return false;
        
            // If not, then we mark the diagonal indexes
            upperDiagonal[curDiagUpper] = 1;
            lowerDiagonal[curDiagLower] = 1;

            // Reset the needed variables for the next coordinate
            j = 0;
            letterLen = 0;
            curRowStr = "";
        }// end of outer loop

        // if no issues were found, all the queens are in fact safe
        return true;
    }// end of allTheQueensAreSafe

    // This is a helper function to convert the letters into into numbers and then use the algorithm given by the
    // assignment to get the column numbers for each individual string
    public static int stringToNumber(String coordinate, int letterLen) 
    {
        int finalCol = 0,index = 0, i;
        for (i = letterLen-1; i >= 0; i--)
        {
            // the algorithm that I went with went
            // <last letter's base 26 equivlent + 1> * 26^i + ,,, + <first letter's base 26 equivlent + 1> 26^(i+n) i starts at 0 and goes up to letterLen - 1
            // the letterLen - 1 is imporant since we will go out of bounnds and that we need the index to go to zero and not just 1
            finalCol += ((int)coordinate.charAt(i) - 'a' + 1) * Math.pow(26, index);
            index++;
        }

        return finalCol;
    }// end of stringToNumber
}
