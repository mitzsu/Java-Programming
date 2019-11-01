/**
* Source: https://codercareer.blogspot.com/2011/12/no-27-area-of-rectangles.html?showComment=1572597555375
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class Rect implements Comparable<Rect>{
	int left;
	int right;
	int top;
	int bottom;
	
	public int compareTo(Rect rect) {
		return this.left-rect.left;
	}
}

class Range{
    int less;
    int greater;

    Range(int l, int g){
        less = (l < g) ? l : g;
        greater = (l + g) - less;
    }

    boolean IsOverlapping(Range other){
        return !(less > other.greater || other.less > greater);
    }

    void Merge(Range other){
        if(IsOverlapping(other)){
            less = (less < other.less) ? less : other.less;
            greater = (greater > other.greater) ? greater : other.greater;
        }
    }
}

class IntegerCompare implements Comparator<Integer>{

	@Override
	public int compare(Integer o1, Integer o2) {
		return (o1==o2)?0:( (o1<o2)?-1: 1 );
	}
	
}

public class RectangleSet {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("No. of Rectangles = ");
		int N = sc.nextInt();
		ArrayList<Rect> rects = new ArrayList<>();
		
		System.out.println("\n");
		for(int i=0; i<N; i++) {
			System.out.print((i+1)+": Iteration\nx = ");
			int x = sc.nextInt();
			
			System.out.print("y = ");
			int y = sc.nextInt();
			
			System.out.print("p = ");
			int p = sc.nextInt();
			
			System.out.print("q = ");
			int q = sc.nextInt();
			
			System.out.print("\n\n");
			Rect rect = new Rect();
			rect.left = x;
			rect.right = p;
			rect.top = q;
			rect.bottom = y;
			
			rects.add(rect);
		}
		sc.close();
		
		System.out.println("Total Area occupied = "+getTotalArea(rects));
	}
	
	public static int getTotalArea(ArrayList<Rect> rects){
		
		// Sort the Rectangles By Left Edge using Comparable Collection
	    Collections.sort(rects);

	    // Set all X-axe points
	    ArrayList<Integer> xes = new ArrayList<>();
	    xes = getAllX(rects);
	    
	    // Sort X-Axe By x-point using Comparator Collection
	    IntegerCompare ic = new IntegerCompare();
	    Collections.sort(xes, ic);

	    int area = 0;
	    for(int iterX1 = 0, iterRect = 0 ; iterX1 < xes.size() && iterX1 < xes.size() - 1; ++iterX1){
	    	
	        int iterX2 = iterX1 + 1;
	        
	        if(xes.get(iterX1) < xes.get(iterX2)){
	        	
	            Range rangeX = new Range(xes.get(iterX1), xes.get(iterX2));
	            
	            while(rects.get(iterRect).right < xes.get(iterX1))
	                ++ iterRect;

	            area += getRectArea(rangeX, getRangesOfY(rects, iterRect, rangeX));
	        }
	        
	    }

	    return area;
	}
	
	// Get all X-Axe points
	public static ArrayList<Integer> getAllX(ArrayList<Rect> rects){
		ArrayList<Integer> xes = new ArrayList<>();
		for(int i=0; i<rects.size(); ++i){
			xes.add(rects.get(i).left);
	        xes.add(rects.get(i).right);
	    }
		return xes;
	}
	
	// Get Y-Axe Ranges
	public static ArrayList<Range> getRangesOfY(ArrayList<Rect> rects, int iterRect, Range rangeX){
		ArrayList<Range> rangeOfY = new ArrayList<>();
	    for(; iterRect<rects.size(); ++iterRect){
	        if(rangeX.less < rects.get(iterRect).right && rangeX.greater > rects.get(iterRect).left)
	        	rangeOfY.add( insertRangeY(rangeOfY, new Range(rects.get(iterRect).top, rects.get(iterRect).bottom)) );
	    }
	    return rangeOfY;
	}
	
	// Add Y-Axes Coordinates
	public static Range insertRangeY(ArrayList<Range> rangesOfY, Range rangeY){
		int i=0;
	    while(i<rangesOfY.size()){
	        if( rangeY.IsOverlapping( rangesOfY.get(i) ) ){
	            rangeY.Merge(rangesOfY.get(i));
	            ++i;
	        }
	        else
	            ++i;
	    }

	    return rangeY;
	}

	// Rectangle Area
	public static int getRectArea(Range rangeX, ArrayList<Range> rangesOfY){
	    int width = rangeX.greater - rangeX.less;
	    int area = 0;
	    for(int i=0; i<rangesOfY.size(); ++i){
	        int height = rangesOfY.get(i).greater - rangesOfY.get(i).less;
	        area += width * height;
	    }
	    return area;
	}
}
