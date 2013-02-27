package com.stonetolb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Objects;

/**
 * An immutable container that stores Objects associated with an interval of time.
 * Similar to a changing time line.
 * <p>
 * An example of the internal structure can be viewed as such<br>
 * <br>
 * <b>|{--------1-------} {---2---} {3} {-----------4-----------}|</b>
 * <p>
 * The usage case for this container is for when objects must be stored in sequence but also
 * contain an associated period of time. To retrieve an object from the list, you must
 * specify a "point in time" along the time line and the {@link IntervalQueue} will return 
 * the object that overlaps with the given point in time
 * <p>
 * NOTE: Not a full implementation of an Interval Tree. This container only supports
 * storing non overlapping intervals. Should a need for storing intervals 
 * that may overlap arise, please research the Interval Tree object.
 * 
 * @author james.baiera
 * 
 */
public class IntervalQueue<T>{
	
	/**
	 * Builder class used to construct an {@link IntervalQueue} object.
	 * This builder does not persist it's state between build calls,
	 * therefore, subsequent modifications made to this builder will not 
	 * affect the state of already created objects.
	 * 
	 * @author james.baiera
	 *
	 * @param <T>
	 */
	public static class Builder<T> {
		private IntervalQueue<T> internalQueue;
		
		private Builder() {
			internalQueue = new IntervalQueue<T>();
		}
		
		/**
		 * Appends the given object to the time line
		 * 
		 * @param pObject - Object to be added
		 * @param pLifeTime - Length of the Object's lifetime period.
		 * @return This current builder object for fluid command chaining.
		 */
		public Builder<T> append(T pObject, int pLifeTime) {
			internalQueue.add(pObject, pLifeTime);
			return this;
		}
		
		/**
		 * Constructs the {@link IntervalQueue} and reinitializes the builder.
		 * @return New Immutable {@link IntervalQueue}
		 */
		public IntervalQueue<T> build() {
			IntervalQueue<T> returnValue = internalQueue;
			internalQueue = new IntervalQueue<T>();
			return returnValue;
		}
	}
	
	/**
	 * Internal entry structure that couples a stored object with the beginning and
	 * end of the object's lifetime.
	 * 
	 * @author james.baiera
	 *
	 * @param <T> - Type of object being stored.
	 */
	public static class Interval<T> {
		
		protected int start;
		protected int stop;
		protected T data;
		
		/**
		 * Default constructor.
		 * 
		 * @param pStart - Start time of life time period.
		 * @param pStop - End time of life time period.
		 * @param pData - Object to be stored.
		 */
		public Interval(int pStart, int pStop, T pData){
			start = pStart;
			stop = pStop;
			data = pData;
		}
		
		/**
		 * Gets stored object.
		 * @return Object stored in this Interval.
		 */
		public T get() {
			return data;
		}
		
		/**
		 * Method used to test overlapping Intervals.
		 * @param pOther - Interval to test against.
		 * @return true if the Intervals overlap, false if they do not.
		 */
		public boolean overlaps(Interval<? extends T> pOther)
		{
			//Figure out when it would not be overlapped
			//   When start is greater than end or when end is less than start
			boolean startAfterEnd = start > pOther.stop;
			boolean endBeforeStart = stop < pOther.start;
			
			if (startAfterEnd || endBeforeStart) {
				return false;
			}
			
			return true;
		}
		
		/**
		 * Method used to test if a given point in time overlaps this Interval. 
		 * @param pPoint - A point in the timeline.
		 * @return true if the point overlaps this Interval, false if it does not.
		 */
		public boolean overlaps(int pPoint) {
			return (start <= pPoint && pPoint <= stop);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(start, stop, data);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Interval))
				return false;
			Interval<?> other = (Interval<?>) obj;
			return Objects.equal(start, other.start)
					&& Objects.equal(stop, other.stop)
					&& Objects.equal(data, other.data);
		}
	}
	
	/**
	 * Internal class meant to compare intervals that may overlap.
	 * <p>
	 * Using this instead of setting {@link Interval} to be {@link Comparable}, due to the fact that 
	 * this needs to return equal when the given {@link Interval} is overlapping with another {@link Interval},
	 * NOT when they are the same interval. Using the {@link Comparable} interface would have caused
	 * confusion if the documentation were not read.
	 * 
	 * @author james.baiera
	 *
	 * @param <T>
	 */
	private static class IntervalOverlapComparator<T> implements Comparator<Interval<T>>{

		/**
		 * @return -1 if arg0 interval is before arg1 interval<br>
		 * 0 if arg0 interval is overlapping with arg1 interval<br>
		 * 1 if arg0 interval is after arg1 interval
		 */
		@Override
		public int compare(Interval<T> arg0, Interval<T> arg1) {
			if (arg0.stop < arg1.start) { //LESS THAN
				return -1;
			}
			else if (arg1.stop < arg0.start) //GREATER THAN
			{
				return 1;
			}
			else if(arg0.overlaps(arg1)) { //OVERLAP is an equals in this case
				return 0;
			}
			else
			{
				return -1; //WTF??
			}
		}
	}
	
	// START INTERVAL QUEUE CODE BELOW ---------------------------------
	
	private List<Interval<T>> data;
	private int header;
	
	/**
	 * Gets the builder object.
	 * @return IntervalQueue builder object.
	 */
	public static <T> Builder<T> builder(){
		return new Builder<T>();
	}
	
	/**
	 * Default constructor.
	 */
	private IntervalQueue() {
		data = new ArrayList<Interval<T>>();
		header = 0;
	}
	
	/**
	 * Adds object to end of queue with an interval 
	 * of (END OF QUEUE) to (END OF QUEUE PLUS DURATION)
	 * <p>
	 * Internal method only.
	 * 
	 * @param pData - Object to store
	 * @param pDuration - Length of interval to store for
	 */
	private void add(T pData, int pDuration) {
		data.add(
				new Interval<T>(
						  header              //From next spot in time
						, header + pDuration  //To end of duration
						, pData               //With given data
					)
			);
		
		//Update header to next beginning spot
		header = header + pDuration;
	}
	
	/**
	 * Returns the total length of the queue.
	 * @return The sum of the durations of all Interval objects in the queue.
	 */
	public int getQueueLength() {
		return header;
	}
	
	/**
	 * Returns value stored at given point in time on the time line.
	 * 
	 * @param pTime - Point in time index.
	 * @return object at given point. null if not found.
	 */
	public T getDataAt(int pTime) {
		//TODO: return Optional object. throw Array out of bounds exception
		Interval<T> value = getIntervalAt(pTime);
		if (value == null)
		{
			return null;
		}
		else 
		{
			return value.get();
		}
	}
	
	/**
	 * Returns Interval that overlaps the given point in the time line
	 * 
	 * @param pTime - Point in time index.
	 * @return Interval at given point. null if not found.
	 */
	private Interval<T> getIntervalAt(int pTime) {
		Interval<T> index = new Interval<T>(pTime,pTime,null);
		return getAt(index);
	}
	
	/**
	 * Returns Interval that overlaps the given Interval in the time line
	 * 
	 * @param pSearchInterval - Singleton search interval index.
	 * @return null if value is not found in the time line.<br>
	 * first value found if multiple intervals overlap.
	 */
	private Interval<T> getAt(Interval<T> pSearchInterval) {
		int location = Collections.binarySearch(data, pSearchInterval, new IntervalOverlapComparator<T>());
		if (location < 0) {
			return null;
		}
		else 
		{
			return data.get(location);
		}
	}
}
