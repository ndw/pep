package org.xproc.pep;

public class CharacterRange {
    private final int first;
    private final int last;
    private final boolean negated;

    public CharacterRange(int first, int last, boolean negated) {
        if (first < 0 || last < 0) {
            throw new IllegalArgumentException("Character ranges cannot contain negative characters");
        }
        if (last < first) {
            throw new IllegalArgumentException("Last character in a range must not precede the first character");
        }
        this.first = first;
        this.last = last;
        this.negated = negated;
    }

    public CharacterRange(int charnum, boolean negated) {
        this(charnum, charnum, negated);
    }

    public CharacterRange(int first, int last) {
        this(first, last, false);
    }

    public CharacterRange(int charnum) {
        this(charnum, charnum, false);
    }

    public CharacterRange(char first, char last, boolean negated) {
        this((int) first, (int) last, negated);
    }

    public CharacterRange(char first, char last) {
        this((int) first, (int) last, false);
    }

    public CharacterRange(char first) {
        this((int) first, (int) first, false);
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public boolean getNegated() {
        return negated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterRange) {
            CharacterRange range = (CharacterRange) obj;
            return first == range.first && last == range.last && negated == range.negated;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (negated) {
            sb.append("^");
        }
        if (Character.isBmpCodePoint(first)) {
            sb.append((char) first);
        } else if (Character.isValidCodePoint(first)) {
            sb.append(Character.highSurrogate(first));
            sb.append(Character.lowSurrogate(first));
        } else {
            sb.append("???");
        }
        if (first != last) {
            sb.append("-");
            if (Character.isBmpCodePoint(last)) {
                sb.append((char) last);
            } else if (Character.isValidCodePoint(last)) {
                sb.append(Character.highSurrogate(last));
                sb.append(Character.lowSurrogate(last));
            } else {
                sb.append("???");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
