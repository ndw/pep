package org.xproc.pep;

public class CharacterRangeTest extends PepFixture {
    public void testAlphaRange() {
        CharacterRange alpha = new CharacterRange('A', 'Z');
        assertEquals((int) 'A', alpha.getFirst());
        assertEquals((int) 'Z', alpha.getLast());
        assertEquals(false, alpha.getNegated());
    }

    public void testNotAlphaRange() {
        CharacterRange alpha = new CharacterRange('A', 'Z', true);
        assertEquals((int) 'A', alpha.getFirst());
        assertEquals((int) 'Z', alpha.getLast());
        assertEquals(true, alpha.getNegated());
    }

    public void testSingleChar() {
        CharacterRange alpha = new CharacterRange('A');
        assertEquals((int) 'A', alpha.getFirst());
        assertEquals((int) 'A', alpha.getLast());
        assertEquals(false, alpha.getNegated());
    }

    public void testNotSingleChar() {
        CharacterRange alpha = new CharacterRange('A', 'A', true);
        assertEquals((int) 'A', alpha.getFirst());
        assertEquals((int) 'A', alpha.getLast());
        assertEquals(true, alpha.getNegated());
    }

    public void testNegativeStart() {
        try {
            CharacterRange alpha = new CharacterRange(-34, 100);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testNegativeRange() {
        try {
            CharacterRange alpha = new CharacterRange('9', '0');
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    public void testHigherRange() {
        CharacterRange boxes = new CharacterRange(0x2610, 0x2611);
        assertEquals(0x2610, boxes.getFirst());
        assertEquals(0x2611, boxes.getLast());
        assertEquals(false, boxes.getNegated());
    }

    public void testNotHigherRange() {
        CharacterRange boxes = new CharacterRange(0x2610, 0x2611, true);
        assertEquals(0x2610, boxes.getFirst());
        assertEquals(0x2611, boxes.getLast());
        assertEquals(true, boxes.getNegated());
    }
}
