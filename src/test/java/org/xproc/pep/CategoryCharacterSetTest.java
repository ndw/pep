package org.xproc.pep;

import java.util.Arrays;
import java.util.Collections;

public class CategoryCharacterSetTest extends PepFixture {
    public void testEquality() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        CharacterSet digits = CharacterSet.range('0', '9');
        CategoryCharacterSet set1 = CategoryCharacterSet.inclusion("test", Arrays.asList(alpha, digits));
        CategoryCharacterSet set2 = CategoryCharacterSet.inclusion("test", Arrays.asList(digits, alpha));
        assertTrue(set1.equals(set2));
    }

    public void testInequality() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        CharacterSet digits = CharacterSet.range('0', '9');
        CategoryCharacterSet set1 = CategoryCharacterSet.inclusion("test", Arrays.asList(alpha));
        CategoryCharacterSet set2 = CategoryCharacterSet.inclusion("test", Arrays.asList(digits));
        assertFalse(set1.equals(set2));
    }

    public void testMatchesInRange() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        CharacterSet digits = CharacterSet.range('0', '9');
        CategoryCharacterSet set1 = CategoryCharacterSet.inclusion("test", Arrays.asList(digits, alpha));
        assertTrue(set1.matches("A", false));
        assertTrue(set1.matches("M", false));
        assertTrue(set1.matches("Z", false));
        assertTrue(set1.matches("0", false));
        assertTrue(set1.matches("5", false));
        assertTrue(set1.matches("9", false));
        assertFalse(set1.matches("a", false));
        assertFalse(set1.matches("!", false));
    }

    public void testMatchesNotInRange() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        CategoryCharacterSet set1 = CategoryCharacterSet.exclusion("test", Arrays.asList(alpha));
        assertFalse(set1.matches("A", false));
        assertFalse(set1.matches("M", false));
        assertFalse(set1.matches("Z", false));
        assertTrue(set1.matches("0", false));
        assertTrue(set1.matches("5", false));
        assertTrue(set1.matches("9", false));
        assertTrue(set1.matches("a", false));
        assertTrue(set1.matches("!", false));
    }

    public void testMatchesIgnoreCase() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        CharacterSet digits = CharacterSet.range('0', '9');
        CategoryCharacterSet set1 = CategoryCharacterSet.inclusion("test", Arrays.asList(digits, alpha));
        assertTrue(set1.matches("a", true));
        assertTrue(set1.matches("m", true));
        assertTrue(set1.matches("z", true));
        assertTrue(set1.matches("0", true));
        assertTrue(set1.matches("5", true));
        assertTrue(set1.matches("9", true));
        assertFalse(set1.matches("\u2611", true));
        assertFalse(set1.matches("!", true));
    }
}
