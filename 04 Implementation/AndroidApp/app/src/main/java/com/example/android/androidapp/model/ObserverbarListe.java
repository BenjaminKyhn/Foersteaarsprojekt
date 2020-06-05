package com.example.android.androidapp.model;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

/** @author Tommy **/
public class ObserverbarListe<E> extends ArrayList<E> {
    private PropertyChangeSupport support;

    public ObserverbarListe(int initialCapacity) {
        super(initialCapacity);
        tilfoejSupport();
    }

    public ObserverbarListe() {
        super();
        tilfoejSupport();
    }

    public ObserverbarListe(Collection<? extends E> c) {
        super(c);
        tilfoejSupport();
    }

    private void tilfoejSupport() {
        support = new PropertyChangeSupport(this);
    }

    public void tilfoejListener(PropertyChangeListener propertyChangeListener) {
        support.addPropertyChangeListener(propertyChangeListener);
    }

    public void fjernListener(PropertyChangeListener propertyChangeListener) {
        support.removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public boolean add(E e) {
        support.firePropertyChange("nyAddition", null, e);
        return super.add(e);
    }

    @Override
    public E remove(int index) {
        support.firePropertyChange("nySletningIndex", null, index);
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        support.firePropertyChange("nySletningObjekt", null, o);
        return super.remove(o);
    }

    @Override
    public E get(int index) {
        return super.get(index);
    }
}
