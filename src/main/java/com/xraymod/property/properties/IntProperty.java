package com.xraymod.property.properties;

import com.xraymod.property.Property;

import java.util.function.BooleanSupplier;

public class IntProperty extends Property<Integer> {
    private final Integer minimum;
    private final Integer maximum;

    public IntProperty(String name, Integer value, Integer minimum, Integer maximum) {
        this(name, value, minimum, maximum, null);
    }

    public IntProperty(String name, Integer value, Integer minimum, Integer maximum, BooleanSupplier booleanSupplier) {
        super(name, value, value1 -> value1 >= minimum && value1 <= maximum, booleanSupplier);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public String getValuePrompt() {
        return String.format("%d-%d", this.minimum, this.maximum);
    }

    @Override
    public String formatValue() {
        return String.format("§b%d", this.getValue());
    }

    @Override
    public boolean parseString(String string) {
        try {
            return this.setValue(Integer.parseInt(string));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Integer getMaximum() {
        return maximum;
    }

    public Integer getMinimum() {
        return minimum;
    }
}