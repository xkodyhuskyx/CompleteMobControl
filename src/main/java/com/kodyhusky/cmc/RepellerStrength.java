package com.kodyhusky.cmc;

public enum RepellerStrength {

    SMALL(1), MEDIUM(2), LARGE(3), EXTREME(4), INVALID(0);

    private int size;

    private RepellerStrength(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        switch (size) {
        case 1: {
            return "small";
        }
        case 2: {
            return "medium";
        }
        case 3: {
            return "large";
        }
        case 4: {
            return "extreme";
        }
        default: {
            return "invalid";
        }
        }
    }
}
