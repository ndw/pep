package org.xproc.pep;

import java.util.List;

public class CategoryCharacterSet extends Category {
    private final List<CharacterRange> ranges;

    // FIXME: don't store ranges that are wholly subsumed by other ranges.

    public CategoryCharacterSet(String name, List<CharacterRange> ranges, boolean repeatable) {
        super(name, true, repeatable);
        this.ranges = ranges;
    }

    public CategoryCharacterSet(String name, List<CharacterRange> ranges) {
        super(name, true, false);
        this.ranges = ranges;
    }

    /**
     * Tests whether this category is equal to another.
     * @return <code>true</code> iff the specified object is an instance
     * of <code>CategorySet</code> and its name, terminal status, and tokens are equal
     * to this category's name, terminal status, and tokens.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CategoryCharacterSet) {
            CategoryCharacterSet oc = (CategoryCharacterSet)obj;
            if (oc != Category.START &&
                    terminal == oc.terminal && name.equals(oc.name)) {
                for (CharacterRange range : ranges) {
                    if (!oc.ranges.contains(range)) {
                        return false;
                    }
                }
                for (CharacterRange range : oc.ranges) {
                    if (!ranges.contains(range)) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a given token matches this category.
     * @param token The input token.
     * @param ignoreCase Should case be ignored?
     * @return <code>true</code> iff the token is considered a match for this category.
     */
    @Override
    public boolean matches(String token, boolean ignoreCase) {
        if (token.codePointCount(0, token.length()) > 1) {
            return false;
        }
        int cp = token.codePointAt(0);
        if (ignoreCase) {
            cp = Character.toUpperCase(cp);
        }
        for (CharacterRange range : ranges) {
            boolean found = cp >= range.getFirst() && cp <= range.getLast();
            if (ignoreCase) {
                found = cp >= Character.toUpperCase(range.getFirst()) && cp <= Character.toUpperCase(range.getLast());
            }
            if ((found && !range.getNegated()) || (!found && range.getNegated())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (CharacterRange range : ranges) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append(range);
        }
        sb.append("}");
        return sb.toString();
    }
}
