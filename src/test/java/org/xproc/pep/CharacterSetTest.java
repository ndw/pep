package org.xproc.pep;

public class CharacterSetTest extends PepFixture {
    public void testAlphaRange() {
        CharacterSet alpha = CharacterSet.range('A', 'Z');
        assertTrue(alpha.matches('A'));
        assertTrue(alpha.matches('Z'));
        assertFalse(alpha.matches('0'));
    }

    public void testSingleChar() {
        CharacterSet alpha = CharacterSet.literal("A");
        assertTrue(alpha.matches('A'));
        assertFalse(alpha.matches('Z'));
        assertFalse(alpha.matches('0'));
    }

    public void testLiteral() {
        CharacterSet alpha = CharacterSet.literal("ABC");
        assertTrue(alpha.matches('A'));
        assertTrue(alpha.matches('B'));
        assertTrue(alpha.matches('C'));
        assertFalse(alpha.matches('Z'));
        assertFalse(alpha.matches('0'));
    }

    public void testNegativeStart() {
        try {
            CharacterSet.range(-34, 100);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testNegativeRange() {
        try {
            CharacterSet.range('9', '0');
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testHigherRange() {
        CharacterSet boxes = CharacterSet.range(0x2610, 0x2611);
        assertTrue(boxes.matches('☐'));
        assertTrue(boxes.matches('☑'));
        assertFalse(boxes.matches('A'));
        assertFalse(boxes.matches('Z'));
        assertFalse(boxes.matches('0'));
    }

    public void testClass() {
        CharacterSet letters = CharacterSet.unicodeClass("L");
        assertFalse(letters.matches('☐'));
        assertFalse(letters.matches('☑'));
        assertTrue(letters.matches('A'));
        assertTrue(letters.matches('n'));
        assertFalse(letters.matches('0'));
        assertTrue(letters.matches('é'));
        assertTrue(letters.matches('π'));
    }

    public void testSubclass() {
        CharacterSet letters = CharacterSet.unicodeClass("Ll");
        assertFalse(letters.matches('☐'));
        assertFalse(letters.matches('☑'));
        assertFalse(letters.matches('A'));
        assertTrue(letters.matches('n'));
        assertFalse(letters.matches('0'));
        assertFalse(letters.matches('Ǵ'));
        assertTrue(letters.matches('π'));
    }
}
