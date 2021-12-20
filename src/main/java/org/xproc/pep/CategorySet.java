/*
 * Copyright (C) 2021 Norman Walsh
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. The GNU Lesser General Public License is
 * distributed with this software in the file COPYING.
 */
package org.xproc.pep;

import java.util.HashMap;
import java.util.List;

/**
 * A special kind of {@link Category category} that represents sets of tokens.
 *
 * <p>This extension allows PEP to recognize sets of strings with a single {@link Category}.</p>
 *
 * <p>Where a simple terminal {@link Category} matches only a single token, a
 * <code>CategorySet</code> matches any of a set of strings.
 * <p>For example, you could make a set that matched "January", "February", "March", ...</p>
 *
 * <p>A set can be an <em>inclusion</em>, where it matches any string that appears
 * in the set, or an <em>exclusion</em>, where it matches any string that does
 * not appear in the set.</p>
 */
public class CategorySet extends Category {
    private final boolean negated;
    private final List<String> tokens;

    private CategorySet(String name, List<String> tokens, boolean negated) {
        super(name, true);
        this.tokens = tokens;
        this.negated = negated;
    }

    /**
     * Create a set that matches any one of a set of tokens.
     * @param name The category name.
     * @param tokens The list of tokens.
     * @return A category that matches any one of those tokens.
     */
    public static CategorySet inclusion(String name, List<String> tokens) {
        return new CategorySet(name, tokens, false);
    }

    /**
     * Create a set that matches any string except a set of tokens.
     * @param name The category name.
     * @param tokens The list of tokens.
     * @return A category that matches any string except one of the specified tokens.
     */
    public static CategorySet exclusion(String name, List<String> tokens) {
        return new CategorySet(name, tokens, true);
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
