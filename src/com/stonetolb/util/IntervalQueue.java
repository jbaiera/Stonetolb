package com.stonetolb.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

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
	 * @param <T>
	 * @author james.baiera
	 */
	public static class Builder<T> {
		private RangeMap<Integer, T> mapping;
		private int count;

		/**
		 * Creates new IntervalQueue Builder object.
		 */
		public Builder() {
			mapping = TreeRangeMap.create();
			count = 0;
		}

		/**
		 * Appends the given object to the time line
		 *
		 * @param pObject
		 * 		- Object to be added
		 * @param pLifeTime
		 * 		- Length of the Object's lifetime period.
		 * @return This current builder object for fluid command chaining.
		 */
		public Builder<T> append(T pObject, int pLifeTime) {
			checkArgument(pLifeTime > 0, "Lifetime must be longer than zero");
			mapping.put(Range.closedOpen(count, count + pLifeTime), pObject);
			count += pLifeTime;
			return this;
		}

		/**
		 * Constructs the {@link IntervalQueue} and reinitializes the builder. Further calls to build will not affect
		 * objects already built, nor will they contain any prior data from the last object.
		 *
		 * @return New Immutable {@link IntervalQueue}
		 */
		public IntervalQueue<T> build() {
			checkState(count != 0, "No objects added to queue.");
			IntervalQueue<T> returnValue = new IntervalQueue<T>(mapping);
			mapping = TreeRangeMap.create();
			count = 0;
			return returnValue;
		}
	}
	
	private RangeMap<Integer, T> data;
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
	private IntervalQueue(RangeMap<Integer, T> mapping) {
		data = mapping;
		header = data.span().upperEndpoint();
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
		return data.get(pTime);
	}
}
