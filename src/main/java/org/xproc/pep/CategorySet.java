package org.xproc.pep;

import java.util.HashMap;
import java.util.List;

public class CategorySet extends Category {
    private final boolean negated;
    private final List<String> tokens;

    public CategorySet(String name, List<String> tokens, boolean negated, boolean repeatable) {
        super(name, true, repeatable);
        this.tokens = tokens;
        this.negated = negated;
    }

    public CategorySet(String name, List<String> tokens, boolean negated) {
        super(name, true, false);
        this.tokens = tokens;
        this.negated = negated;
    }

    public CategorySet(String name, List<String> tokens) {
        super(name, true);
        this.tokens = tokens;
        negated = false;
    }

    /**
     * Tests whether this category is equal to another.
     * @return <code>true</code> iff the specified object is an instance
     * of <code>CategorySet</code> and its name, terminal status, and tokens are equal
     * to this category's name, terminal status, and tokens.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CategorySet) {
            CategorySet oc = (CategorySet)obj;
            if (oc != Category.START &&
                    terminal == oc.terminal && name.equals(oc.name)) {
                HashMap<String,Boolean> myTokens = new HashMap<>();
                for (String token : tokens) {
                    myTokens.put(token, false);
                }
                for (String token : oc.tokens) {
                    if (myTokens.containsKey(token)) {
                        myTokens.put(token, true);
                    } else {
                        return false;
                    }
                }
                for (String token : myTokens.keySet()) {
                    if (!myTokens.get(token)) {
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
        boolean found = false;
        for (String item : tokens) {
            found = found || item.equals(token) || (ignoreCase && item.equalsIgnoreCase(token));
        }
        if (negated) {
            return !found;
        } else {
            return found;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (negated) {
            sb.append("!");
        }
        sb.append("{");
        boolean first = true;
        for (String token : tokens) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append(token);
        }
        sb.append("}");
        return sb.toString();
    }
}
