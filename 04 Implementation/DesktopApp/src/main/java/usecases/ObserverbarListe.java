package usecases;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

/** @author Tommy */
public class ObserverbarListe<E> extends ArrayList<E> {
    /** Svarer til notification i Observer pattern */
    private PropertyChangeSupport support;

    public ObserverbarListe() {
        super();
        support = new PropertyChangeSupport(this);
    }

    public ObserverbarListe(int initialCapacity) {
        super(initialCapacity);
        support = new PropertyChangeSupport(this);
    }

    public ObserverbarListe(Collection<? extends E> c) {
        super(c);
        support = new PropertyChangeSupport(this);
    }

    public void tilfoejObserver(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

    @Override
    public boolean add(E e) {
        support.firePropertyChange("Ny Addition", null, e);
        return super.add(e);
    }

    @Override
    public E remove(int index) {
        support.firePropertyChange("Slet Index", null, index);
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        support.firePropertyChange("Slet Object", null, o);
        return super.remove(o);
    }

    @Override
    public E get(int index) {
        return super.get(index);
    }
}
