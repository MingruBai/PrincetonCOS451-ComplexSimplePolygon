# PrincetonCOS451-ComplexSimplePolygon

Design: 

The general idea of my algorithm is taking a randomly generated complex polygon and resolving the self-intersections until the polygon becomes simple. More specifically, the algorithm takes as input the number of vertices n, and randomly generates an array of n points. The algorithm then finds all the self-intersections of the polygon, and puts all self-intersections into a priority queue. The priority used by the priority queue is the amount of change in total boundary that would result from the intersection being resolved. The algorithm then resolves all the intersections in the queue by reversing the section of the polygon array in-between the two edges that make the intersection. Note that if the any one of the two edges that make the intersection is already “removed” while another intersection is resolved, we skip this intersection. After the priority queue is emptied, we again find all intersections of the current polygon. If no more self-intersections remain; else we repeat the previous step to eliminate remaining self-intersections. The pseudocode that describes the algorithm is given below:


Analysis:

A naïve O(n2) algorithm is used to find all self-intersections of the polygon. There are O(n2) self-intersections and resolving each intersection takes O(n), so each “sweep” (the procedures within the outer while loop) takes O(n3). It is unclear, however, how many such sweeps are needed to achieve a simple polygon. According to a paper by Van Leeuwen and Schoone [vLS82], untangling a travelling salesman tour in the plane using similar techniques needs to resolve O(n3) intersections, and resolving each intersection take O(n). Also according to a problem in IMO2014 and the provided solutions, the upper bound for the number of times such a “disentangling” can be performed is O(n3). [IMO2014]. Therefore, an overall run time of O(n4) shall be expected. At the end of the report, I discussed an experiment that I ran to observe the run time, and the results seem to indicate a O(n2) time complexity. 

Implementation:

Please see attached java program for the implementation. Some of the data structures used in the program: 

1)	A Point class with x coordinate, y coordinate, and the point’s index in the polygon array is implemented to represent a vertex; 
2)	An Intersection class with the four points that make the two edges that produce the intersection is implemented. The class implements Comparable, and is compared by the change in total polygon boundary that would be resulted by the intersection being resolved;
3)	An array of Points is used to store the polygon;
4)	A priority queue of intersections is used to keep all the intersections.

Some of the functions of the class:

1)	A dist() function that calculate the distrance between two points
2)	A printPoly() function that prints out the vertices that make the polygon
3)	A reverse() function that reverses a segment of the array
4)	A checkInter() function that checks whether two edges intersect

The constructor is the function that will perform the main steps of the algorithm to produce a simple polygon. The main() function takes the number of vertices as a command-line argument, calls the constructor, and prints out the resulting polygon.

Since the program is densely commented, I hope it is easy enough to follow without including further implementation details in the report.

Reference:

[IMO2014] 55th International Mathematical Olympiad. Problems Short List With Solution. https://www.imo-official.org/problems/IMO2014SL.pdf

[vLS82] J. van Leeuwen and A.A. Schoone. Untangling a Travelling Salesman Tour in the Plane. In J.R. Muhlbacher, editor, Proc. 7th Conf. Graph-theoretic Concepts in Comput. Sci. (WG 81), pages 87-98, 1982.
