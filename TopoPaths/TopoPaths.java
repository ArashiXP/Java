// Combination of a topological sort and hamiltonian path.

import java.util.*;
import java.io.*;

public class TopoPaths 
{
	private List<ArrayList<Integer>> digraph;
	private int numVertices;

	// Constructor for the graph
	private TopoPaths(String filename) throws IOException
	{
		int i,k,j;
		Scanner s = new Scanner(new File(filename));
		digraph = new ArrayList<>();

		// read in the number of vertices and then fill the arraylist with n arraylists
		numVertices = s.nextInt();
		for (i = 0; i < numVertices; i++)
		{
			digraph.add(new ArrayList<Integer>());
		}

		// Fill each arraylist with the outdegrees
		for (i = 0; i < numVertices; i++)
		{
			k = s.nextInt();
			for (j = 0; j < k; j++)
			{
				digraph.get(i).add(j, s.nextInt());
			}
		}
	}

	// wrapper function to make the digraph then call topoPath
	public static int countTopoPaths(String filename) throws IOException
	{
		TopoPaths graph = new TopoPaths(filename);
		return graph.topoPath();
	}

	// Check for topological sort and if it has a valid path, returns number of topopaths
	// Heavily inspired by the code from webcourses toposort.java written by Sean Szumlanski 
	// on Feburary 21 2023
	private int topoPath()
	{
		int i, j, outDegree, count = 0;
		int [] incoming = new int[numVertices];
		Queue<Integer> q = new ArrayDeque<>();
		List<ArrayList<Integer>> inDegrees = new ArrayList<>();

		// Hold the indegrees of the vertices
		for (i = 0; i < numVertices; i++)
		{
			inDegrees.add(new ArrayList<Integer>());
		}
		
		// Convert the outdegrees into indegrees
		for (i = 0; i < numVertices; i++)
		{
			for (j = 0; j < digraph.get(i).size(); j++)
			{
				outDegree = (digraph.get(i).get(j) - 1);
				inDegrees.get(outDegree).add((i));
			}
		}

		// Count the number of incoming edges for each vertex
		for (i = 0; i < numVertices; i++)
		{
			incoming[i] = inDegrees.get(i).size();
		}

		// If the vertex has 0 indegree edges then they will be added to the queue since
		// they're ready to be visited
		for (i = 0; i < numVertices; i++)
		{
			if (incoming[i] == 0) q.add(i);
		}

		// Now we begin the sort, until the queue is empty
		while (!q.isEmpty())
		{
			// Pull a vertex out of the queue and add it to the toposort
			int node = q.remove();

			// If vertex and it has no path to previous, but a 
			// valid toposort, then we have to stop
			if (!q.isEmpty()) return 0;

			++count;

			// Vertices that can be reached directly by the current vertex must have
			// inDegree decremented, and if doing so makes it 0, then it can be visited
			for (i = 0; i < numVertices; i++)
			{
				if (inDegrees.get(i).contains(node) && --incoming[i] == 0) q.add(i);
			}
		}

		// If cycle is present, we stop
		if (count != numVertices){
			return 0;
		}

		// A topopath has been found!
		return 1;
	}
}
