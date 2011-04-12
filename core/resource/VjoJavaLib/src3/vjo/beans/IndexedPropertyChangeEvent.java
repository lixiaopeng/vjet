package vjo.beans;

import vjo.lang.* ;

/**
 * An "IndexedPropertyChange" event gets delivered whenever a component that
 * conforms to the JavaBeans<TM> specification (a "bean") changes a bound 
 * indexed property. This class is an extension of <code>PropertyChangeEvent</code>
 * but contains the index of the property that has changed.
 * <P>
 * Null values may be provided for the old and the new values if their
 * true values are not known.
 * <P>
 * An event source may send a null object as the name to indicate that an
 * arbitrary set of if its properties have changed.  In this case the
 * old and new values should also be null.
 *
 * @version 1.4 12/19/03
 * @since 1.5
 * @author Mark Davidson
 */
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {

    private int index;

    /**
     * Constructs a new <code>IndexedPropertyChangeEvent</code> object.
     *
     * @param source  The bean that fired the event.
     * @param propertyName  The programmatic name of the property that
     *             was changed.
     * @param oldValue      The old value of the property.
     * @param newValue      The new value of the property.
     * @param index index of the property element that was changed.
     */
    public IndexedPropertyChangeEvent(Object source, String propertyName,
				      Object oldValue, Object newValue,
				      int index) {
	super (source, propertyName, oldValue, newValue);
	this.index = index;
    }


    /**
     * Gets the index of the property that was changed.
     *
     * @return The index specifying the property element that was
     *         changed.
     */
    public int getIndex() {
	return index;
    }
}


