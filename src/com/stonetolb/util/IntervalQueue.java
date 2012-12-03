package com.stonetolb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IntervalQueue<T>{
	
	/**
	 * Object that stores start and stop information of a section of interval
	 * <p>
	 * NOTE: Not a full implementation of an Interval Tree. Should a need for storing intervals 
	 * that may overlap arise, there are better packages out there for it.
	 * <p>
	 * This implementation is for sequential non overlapping interval listings.
	 * 
	 * @author james.baiera
	 *
	 * @param <T>
	 */
	public static class Interval<T> {
		
		protected int start;
		protected int stop;
		protected T data;
		
		public Interval(int pStart, int pStop, T pData){
			start = pStart;
			stop = pStop;
			data = pData;
		}
		
		public T get() {
			return data;
		}
		
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
		
		public boolean overlaps(int pPoint) {
			return (start <= pPoint && pPoint <= stop);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((data == null) ? 0 : data.hashCode());
			result = prime * result + start;
			result = prime * result + stop;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Interval other = (Interval) obj;
			if (data == null) {
				if (other.data != null)
					return false;
			} else if (!data.equals(other.data))
				return false;
			if (start != other.start)
				return false;
			if (stop != other.stop)
				return false;
			return true;
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
	
	public IntervalQueue() {
		data = new ArrayList<Interval<T>>();
		header = 0;
	}
	
	public void add(T pData, int pDuration) {
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
	
	public T getDataAt(int pTime) {
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
	
	public Interval<T> getIntervalAt(int pTime) {
		Interval<T> index = new Interval<T>(pTime,pTime,null);
		return getAt(index);
	}
	
	public Interval<T> getAt(Interval<T> pSearchInterval) {
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
