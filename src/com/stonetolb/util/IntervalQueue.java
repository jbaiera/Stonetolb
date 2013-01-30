/* 
 * Copyleft (o) 2012 James Baiera
 * All wrongs reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.stonetolb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An immutable Container that stores Objects associated with an interval of time.
 * Similar to a time line.
 * <p>
 * An example of the internal structure can be viewed as such<br>
 * |--------1-------||---2---||3||-----------4-----------|
 * <p>
 * The usage case for this container is when objects must be stored in sequence but also
 * contain data on how "long" they are in time. To retrieve an object from the list, you can
 * specify any "point in time" along the time line and the {@link IntervalQueue} will return 
 * the object in that point in time
 * <p>
 * NOTE: Not a full implementation of an Interval Tree. This container only supports
 * storing non overlapping intervals. Should a need for storing intervals 
 * that may overlap arise, please use the Interval Tree object
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
	public static class IntervalQueueBuilder<T> {
		private IntervalQueue<T> internalQueue;
		
		private IntervalQueueBuilder() {
			internalQueue = new IntervalQueue<T>();
		}
		
		/**
		 * Appends the given object to the time line
		 * 
		 * @param pObject Object to be added
		 * @param pLifeTime Amount of time Object should be represented by an interval
		 * @return This builder object for command chaining
		 */
		public IntervalQueueBuilder<T> append(T pObject, int pLifeTime) {
			internalQueue.add(pObject, pLifeTime);
			return this;
		}
		
		/**
		 * 
		 * @return New Immutable {@link IntervalQueue}
		 */
		public IntervalQueue<T> build() {
			IntervalQueue<T> returnValue = internalQueue;
			internalQueue = new IntervalQueue<T>();
			return returnValue;
		}
	}
	
	/**
	 * Object that stores start and stop information of a section of interval
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
		
		/**
		 * 
		 * @param pOther
		 * @return true if other interval overlaps, false if it does not
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
			Interval<?> other = (Interval<?>) obj;
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
	
	public static <T> IntervalQueueBuilder<T> builder(){
		return new IntervalQueueBuilder<T>();
	}
	
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
	 * @param pData Object to store
	 * @param pDuration Length of interval to store for
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
	 * @return The sum of the durations of Interval objects in the queue
	 */
	public int getQueueLength() {
		return header;
	}
	
	/**
	 * Returns value stored at given point in time on the time line.
	 * 
	 * @param pTime
	 * @return null if value is not found in the time line
	 */
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
	
	/**
	 * Returns Interval that overlaps the given point in the time line
	 * 
	 * @param pTime
	 * @return null if value is not found in the time line
	 */
	private Interval<T> getIntervalAt(int pTime) {
		Interval<T> index = new Interval<T>(pTime,pTime,null);
		return getAt(index);
	}
	
	/**
	 * Returns Interval that overlaps the given Interval in the time line
	 * 
	 * @param pSearchInterval
	 * @return null if value is not found in the time line<br>
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
