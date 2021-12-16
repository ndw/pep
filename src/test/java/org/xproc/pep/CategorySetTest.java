package org.xproc.pep;

import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategorySetTest extends PepFixture {

    public void testVowels() {
        Grammar grammar = new Grammar("vowels");

        Category z = new Category("z", true);
        Category avz = new Category("a_vowel_z");
        Category avsz = new Category("a_vowels_z");
        Category vcv = new Category("v_c_v");
        grammar.addRule(new Rule(vcv, Vowel, Consonant, Vowel));
        grammar.addRule(new Rule(avz, a, Vowel, z));
        grammar.addRule(new Rule(avsz, a, Vowel, z));

        EarleyParser parser = new EarleyParser(grammar);

        List<String> aOz = Arrays.asList("a", "O", "z");
        List<String> aEz = Arrays.asList("a", "E", "z");
        List<String> abz = Arrays.asList("a", "b", "z");
        List<String> AbO = Arrays.asList("A", "b", "O");
        List<String> EbI = Arrays.asList("E", "b", "I");

        try {
            Parse parse = parser.parse(aOz, avz);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
            parse = parser.parse(aEz, avz);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
            parse = parser.parse(abz, avz);
            Assert.assertTrue(parse.getStatus() == Status.REJECT);
            parse = parser.parse(AbO, vcv);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
            parse = parser.parse(EbI, vcv);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }
    }

    public void testCharacterRanges() {
        Grammar grammar = new Grammar("charranges");

        CharacterSet upperAlpha = CharacterSet.range('A', 'Z');
        CharacterSet lowerAlpha = CharacterSet.range('a', 'z');
        CategoryCharacterSet alphas = CategoryCharacterSet.inclusion("alpha", Arrays.asList(upperAlpha, lowerAlpha), true);
        CategoryCharacterSet digits = CategoryCharacterSet.inclusion("digits", Collections.singletonList(CharacterSet.range('0', '9')));

        Category digitLetterDigit = new Category("dld");

        grammar.addRule(new Rule(digitLetterDigit, digits, alphas, digits));

        EarleyParser parser = new EarleyParser(grammar);

        List<String> zabc = Arrays.asList("0", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "a", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "a", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "A", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "z", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "Z", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }

        zabc = Arrays.asList("0", "π", "9");
        try {
            Parse parse = parser.parse(zabc, digitLetterDigit);
            Assert.assertFalse(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }
    }
}
