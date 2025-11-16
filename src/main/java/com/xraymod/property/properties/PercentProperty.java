package com.xraymod.property.properties;

import com.xraymod.property.Property;

import java.util.function.BooleanSupplier;

public class PercentProperty extends Property<Integer> {
    private final Integer minimum;
    private final Integer maximum;

    public PercentProperty(String name, Integer value) {
        this(name, value, null);
    }

    public PercentProperty(String name, Integer value, BooleanSupplier check) {
        this(name, value, 0, 100, check);
    }

    public PercentProperty(String name, Integer value, Integer minimum, Integer maximum, BooleanSupplier booleanSupplier) {
        super(name, value, value1 -> value1 >= minimum && value1 <= maximum, booleanSupplier);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @Override
    public String getValuePrompt() {
        return String.format("%d-%d%%", this.minimum, this.maximum);
    }

    @Override
    public String formatValue() {
        return String.format("§b%d%%", this.getValue());
    }

    @Override
    public boolean parseString(String string) {
        try {
            return this.setValue(Integer.parseInt(string.replace("%", "")));
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