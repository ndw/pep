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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A class that represents a range of Unicode characters.
 * <p>Ranges can be constructed from a literal string, from a range of Unicode codepoints, or
 * via Unicode character classes.</p>
 */
public class CharacterSet {
    private String charClass = null;
    private Pattern pattern = null;
    private String literal = null;
    private Set<Integer> codepoints = null;
    private int first = 0;
    private int last = 0;

    private CharacterSet(int first, int last) {
        if (first < 0 || last < 0) {
            throw new IllegalArgumentException("Character ranges cannot contain negative characters");
        }
        if (last < first) {
            throw new IllegalArgumentException("Last character in a range must not precede the first character");
        }
        this.first = first;
        this.last = last;
    }

    private CharacterSet(String literal) {
        HashSet<Integer> cps = new HashSet<>();
        literal.codePoints().forEach(cps::add);
        codepoints = cps;
        this.literal = literal;
    }

    private CharacterSet(Character charclass, Character subclass) {
        if (charclass == null) {
            throw new NullPointerException("null charclass");
        }
        if (subclass == null) {
            this.charClass = charclass.toString();
        } else {
            this.charClass = charclass.toString() + subclass;
        }
        String patn = "\\" + "p{" + charClass + "}";
        this.pattern = Pattern.compile(patn);
    }

    /**
     * Construct a character set containing each of the characters in the string.
     * @param literal The string of characters.
     * @return A character set that will match each of those characters.
     */
    public static CharacterSet literal(String literal) {
        return new CharacterSet(literal);
    }

    /**
     * Construct a character set containing each of the characters in the specified range, inclusive.
     * @param first The first codepoint.
     * @param last The last codepoint.
     * @return A character set that will match each of those characters.
     * @throws IllegalArgumentException if the range is invalid.
     */
    public static CharacterSet range(int first, int last) {
        return new CharacterSet(first, last);
    }

    /**
     * Construct a character set representing the specified Unicode character class.
     * @param charClass The character class, for example "L", or "Nd".
     * @return A character set that will match characters in that class.
     * @throws NullPointerException if the charClass is null.
     * @throws IllegalArgumentException if the charClass is less than 1 or more than 2 characters long.
     */
    public static CharacterSet unicodeClass(String charClass) {
        if (charClass == null) {
            throw new NullPointerException("Null charClass");
        }
        if (charClass.length() < 1 || charClass.length() > 2) {
            throw new IllegalArgumentException("The charClass must be one or two characters");
        }
        Character ch1 = charClass.charAt(0);
        Character ch2 = null;
        if (charClass.length() > 1) {
            ch2 = charClass.charAt(1);
        }
        return new CharacterSet(ch1, ch2);
    }

    /**
     * Tests for the equality of two <code>CharacterSet</code> objects.
     * <p>Two <code>CharacterSet</code> objects are equal only if they identify the same characters
     * expressed in the same way. A set created from the literal "0123456789" is not equal to
     * a set created from the range '0' to '9'.</p>
     * @param obj A CharacterSet to test for equality against.
     * @return true if and only if the character set provided identifies the same range of characters.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CharacterSet) {
            CharacterSet range = (CharacterSet) obj;
            return first == range.first && last == range.last
                    && ((pattern == null && range.pattern == null)
                         || (pattern != null && pattern.equals(range.pattern)))
                    && ((codepoints == null && range.codepoints == null)
                         || (codepoints != null && codepoints.equals(range.codepoints)));
        }
        return false;
    }

    /**
     * Test if a code point occurs in the set.
     * @param codepoint The Unicode codepoint to test.
     * @return true if and only if the codepoint is in the set.
     */
    public boolean matches(int codepoint) {
        if (pattern != null) {
            String str = new StringBuilder().appendCodePoint(codepoint).toString();
            return pattern.matcher(str).matches();
        } else if (codepoints != null) {
            return codepoints.contains(codepoint);
        } else {
            return codepoint >= first && codepoint <= last;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (pattern != null) {
            sb.append(charClass);
        } else if (codepoints != null) {
            sb.append("\"");
            sb.append(literal.replaceAll("\"", "\"\""));
            sb.append("\"");
        } else {
            if (Character.isBmpCodePoint(first)) {
                sb.append((char) first);
            } else if (Character.isValidCodePoint(first)) {
                sb.append("\"");
                sb.append(Character.highSurrogate(first));
                sb.append(Character.lowSurrogate(first));
                sb.append("\"");
            } else {
                sb.append("???");
            }
            if (first != last) {
                sb.append("-");
                if (Character.isBmpCodePoint(last)) {
                    sb.append("\"");
                    sb.append((char) last);
                    sb.append("\"");
                } else if (Character.isValidCodePoint(last)) {
                    sb.append(Character.highSurrogate(last));
                    sb.append(Character.lowSurrogate(last));
                } else {
                    sb.append("???");
                }
            }
        }
        return sb.toString();
    }
}
