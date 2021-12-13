package org.xproc.pep;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
        this.pattern = Pattern.compile("\\p{" + charClass + "}");
    }

    public static CharacterSet literal(String literal) {
        return new CharacterSet(literal);
    }

    public static CharacterSet range(int first, int last) {
        return new CharacterSet(first, last);
    }

    public static CharacterSet unicodeClass(String charClass) {
        if (charClass == null) {
            throw new NullPointerException("Null charClass");
        }
        if (charClass.length() > 2) {
            throw new IllegalArgumentException("The charClass can be at most two characters");
        }
        Character ch1 = charClass.charAt(0);
        Character ch2 = null;
        if (charClass.length() > 1) {
            ch2 = charClass.charAt(1);
        }
        return new CharacterSet(ch1, ch2);
    }

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

    public boolean matches(int codepoint) {
        if (pattern != null) {
            String str = new StringBuilder().appendCodePoint(codepoint).toString();
            boolean match = pattern.matcher(str).matches();
            return match;
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
