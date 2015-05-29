package networkframework.core.algorithm

import networkframework.core.graph.Node
import networkframework.core.graph.GraphPart

object SortNodes{
	def sort[A <% GraphPart, T <% Ordered[T]](xs: List[A], sortBy : String):List[A] = {

    if (xs.isEmpty || xs.tail.isEmpty) xs
    else {
      
      val pivot = xs( xs.length / 2).getAttribute[T](sortBy)
 
      // list partition

      // initialize boxes
      var lows: List[A] = Nil
      var mids: List[A] = Nil
      var highs: List[A] = Nil
      
      for( val item <- xs) {

        // classify item
        val value = item.getAttribute[T](sortBy);
        if ( value == pivot) mids = item :: mids
        else if (value < pivot) lows = item :: lows
        else highs = item :: highs
      }

      // return sorted list appending chunks
      
      sort( lows, sortBy) ::: mids ::: sort( highs, sortBy) 
    }
  }
}