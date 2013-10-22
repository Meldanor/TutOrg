package de.meldanor.tutorg;

import java.util.List;

public interface EditDistance {

    public abstract int getDifference(String other);

    public abstract List<String> similarNames(List<String> names);

}