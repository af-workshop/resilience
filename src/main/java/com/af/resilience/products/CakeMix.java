package com.af.resilience.products;

import java.util.Objects;

public class CakeMix {

    private String name;

    public CakeMix(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CakeMix cakeMix = (CakeMix) o;
        return Objects.equals(name, cakeMix.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "CakeMix{" + "name='" + name + '\'' + '}';
    }
}
