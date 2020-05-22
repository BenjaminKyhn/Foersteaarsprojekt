package com.example.android.androidapp.domain;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

public class ObserverbarListe extends ArrayList {
    private PropertyChangeSupport support;

    public ObserverbarListe(int initialCapacity) {
        super(initialCapacity);
        tilfoejSupport();
    }

    public ObserverbarListe() {
        super();
        tilfoejSupport();
    }

    @SuppressWarnings("unchecked")
    public ObserverbarListe(Collection c) {
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
    @SuppressWarnings("unchecked")
    public boolean add(Object o) {
        support.firePropertyChange("nyAddition", null, o);
        return super.add(o);
    }

    @Override
    public Object remove(int index) {
        support.firePropertyChange("nySletningIndex", null, index);
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        support.firePropertyChange("nySletningObjekt", null, o);
        return super.remove(o);
    }
}
