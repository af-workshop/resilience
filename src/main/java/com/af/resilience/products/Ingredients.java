package com.af.resilience.products;

import java.util.Objects;

public class Ingredients {

    private String contents;

    public Ingredients(String contents) {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredients that = (Ingredients) o;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }

    @Override
    public String toString() {
        return "Ingredients{" + "contents='" + contents + '\'' + '}';
    }
}
