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
        grammar.addRule(new Rule(vcv, Vowel, Consonant, Vowels));
        grammar.addRule(new Rule(avz, a, Vowel, z));
        grammar.addRule(new Rule(avsz, a, Vowels, z));

        EarleyParser parser = new EarleyParser(grammar);

        List<String> aOz = Arrays.asList("a", "O", "z");
        List<String> aIEOz = Arrays.asList("a", "I", "E", "O", "z");
        List<String> aEz = Arrays.asList("a", "E", "z");
        List<String> abz = Arrays.asList("a", "b", "z");
        List<String> AbO = Arrays.asList("A", "b", "O");
        List<String> EbI = Arrays.asList("E", "b", "I");

        try {
            Parse parse = parser.parse(aIEOz, avsz);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
            parse = parser.parse(aOz, avz);
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

        Category digitLettersDigit = new Category("dld");

        grammar.addRule(new Rule(digitLettersDigit, digits, alphas, digits));

        EarleyParser parser = new EarleyParser(grammar);

        List<String> zabc = Arrays.asList("0", "a", "b", "c", "X", "Y", "Z", "9");

        try {
            Parse parse = parser.parse(zabc, digitLettersDigit);
            Assert.assertTrue(parse.getStatus() == Status.ACCEPT);
        } catch (PepException ex) {
            fail();
        }
    }

}
