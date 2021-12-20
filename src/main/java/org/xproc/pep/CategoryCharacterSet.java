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

import java.util.Collections;
import java.util.List;

/**
 * A special kind of {@link Category category} that represents sets of characters.
 *
 * <p>This extension allows PEP to recognize ranges of Unicode characters.</p>
 *
 * <p>Where a simple terminal {@link Category} matches only a single token, the
 * <code>CategoryCharacterClass</code> matches any character that satisfies a range.
 * For example, you could make a character set that matched any
 * of the digits from 0 to 9 or any vowel.</p>
 *
 * <p>Character sets are composed from a union of {@link CharacterSet} ranges.</p>
 *
 * <p>A set can be an <em>inclusion</em>, where it matches any character that appears
 * in any range, or an <em>exclusion</em>, where it matches any character that does
 * not appear in <em>any</em> range.</p>
 *
 * <p>For example, given <code>letters</code>, a range representing any Unicode character
 * in the "letter" class, and <code>digits</code>, a range representing the digits "0" to "9", inclusive:</p>
 * <ul>
 *     <li><code>CategoryCharacterSet.inclusion("letters", letters)</code> matches any letter.</li>
 *     <li><code>CategoryCharacterSet.exclusion("not-letters", letters)</code> matches any character
 *     that is not a letter.</li>
 *     <li><code>CategoryCharacterSet.inclusion("letters-or-digits", Arrays.asList(letters, digits)</code> matches
 *     any letter or digit.</li>
 * </ul>
 *
 * <p>If you ask whether a single character matches a set, the answer is obvious. If you ask whether a string
 * of characters matches, it's less obvious. At present, this class takes the position that any token
 * of more than one Unicode code point <em>does not match</em> any set. An alternative would be to say that
 * it matches if each and all of its characters match. Please raise an issue or a pull-request if you'd prefer
 * that interpretation. I don't feel strongly about it.</p>
 */

public class CategoryCharacterSet extends Category {
    private final List<CharacterSet> ranges;
    private final boolean inclusion;

    private CategoryCharacterSet(String name, List<CharacterSet> ranges, boolean inclusion) {
        super(name, true);
        this.ranges = ranges;
        this.inclusion = inclusion;
    }

    /**
     * Construct a category for the given range.
     * <p>This is a convenience class for the case where you want to match a single range.</p>
     * @param name The name of the category.
     * @param range The range.
     * @return A character set {@link Category} that matches any character in that range.
     */
    public static CategoryCharacterSet inclusion(String name, CharacterSet range) {
        return new CategoryCharacterSet(name, Collections.singletonList(range), true);
    }

    /**
     * Construct a category for the given set of ranges.
     * @param name The name of the category.
     * @param ranges The ranges.
     * @return A character set {@link Category} that matches any character in any range.
     */
    public static CategoryCharacterSet inclusion(String name, List<CharacterSet> ranges) {
        return new CategoryCharacterSet(name, ranges, true);
    }

    /**
     * Construct a category excluding the given range.
     * <p>This is a convenience class for the case where you want to match a single range.</p>
     * @param name The name of the category.
     * @param range The range.
     * @return A character set {@link Category} that matches any character <em>not</em> in the range.
     */
    public static CategoryCharacterSet exclusion(String name, CharacterSet range) {
        return new CategoryCharacterSet(name, Collections.singletonList(range), false);
    }

    /**
     * Construct a category excluding the given range.
     * @param name The name of the category.
     * @param ranges The range.
     * @return A character set {@link Category} that matches any character <em>not</em> in the range.
     */
    public static CategoryCharacterSet exclusion(String name, List<CharacterSet> ranges) {
        return new CategoryCharacterSet(name, ranges, false);
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
                for (CharacterSet range : ranges) {
                    if (!oc.ranges.contains(range)) {
                        return false;
                    }
                }
                for (CharacterSet range : oc.ranges) {
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
        for (CharacterSet range : ranges) {
            boolean found = false;
            if (ignoreCase) {
                found = range.matches(Character.toUpperCase(cp)) || range.matches(Character.toLowerCase(cp));
            } else {
                found = range.matches(cp);
            }
            if ((found && inclusion) || (!found && !inclusion)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!inclusion) {
            sb.append("~");
        }
        sb.append("[");
        boolean first = true;
        for (CharacterSet range : ranges) {
            if (!first) {
                sb.append(";");
            }
            first = false;
            sb.append(range);
        }
        sb.append("]");
        return sb.toString();
    }
}
