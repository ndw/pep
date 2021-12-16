package org.xproc.pep;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;

public class CategoryOptionalTest extends TestCase {

    public void testOptionalTerminal() {
        Grammar grammar = new Grammar("opt");

        Category A = new Category("A");
        Category B = new Category("B");
        Category a = new Category("a", true);
        Category b = new Category("b", true);
        Category x = new Category("x", true, true);

        Category opt = new Category("Opt");

        grammar.addRule(new Rule(opt, A, x, B));
        grammar.addRule(new Rule(A, a));
        grammar.addRule(new Rule(B, b));

        EarleyParser parser = new EarleyParser(grammar);
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("a");
        //tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }
    }

    public void testOptionalNonterminal() {
        Grammar grammar = new Grammar("opt");

        Category A = new Category("A");
        Category B = new Category("B");
        Category X = new Category("X", false, true);
        Category a = new Category("a", true);
        Category b = new Category("b", true);
        Category x = new Category("x", true);

        Category opt = new Category("Opt");

        grammar.addRule(new Rule(opt, A, X, B));
        grammar.addRule(new Rule(A, a));
        grammar.addRule(new Rule(B, b));
        grammar.addRule(new Rule(X, x));

        EarleyParser parser = new EarleyParser(grammar);
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("a");
        //tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }
    }

    public void testOptionalRepeatedTerminal() {
        Grammar grammar = new Grammar("opt");

        Category A = new Category("A");
        Category B = new Category("B");
        Category a = new Category("a", true);
        Category b = new Category("b", true);
        Category x = new Category("x", true, true);

        Category opt = new Category("Opt");

        grammar.addRule(new Rule(opt, A, x, x, B));
        grammar.addRule(new Rule(A, a));
        grammar.addRule(new Rule(B, b));

        EarleyParser parser = new EarleyParser(grammar);
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("x");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.REJECT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }
    }

    public void testOptionalRepeatedNonterminal() {
        Grammar grammar = new Grammar("opt");

        Category A = new Category("A");
        Category B = new Category("B");
        Category X = new Category("X", false, true);
        Category Y = new Category("Y", false, true);
        Category Z = new Category("Z", false, true);
        Category a = new Category("a", true);
        Category b = new Category("b", true);
        Category x = new Category("x", true);

        Category opt = new Category("Opt");

        grammar.addRule(new Rule(opt, A, X, B));
        grammar.addRule(new Rule(opt, A, X, X, B));
        grammar.addRule(new Rule(opt, A, X, Y, B));
        grammar.addRule(new Rule(opt, A, X, Y, Z, B));
        grammar.addRule(new Rule(A, a));
        grammar.addRule(new Rule(B, b));
        grammar.addRule(new Rule(X, x));
        grammar.addRule(new Rule(Y, x));
        grammar.addRule(new Rule(Z, x));

        EarleyParser parser = new EarleyParser(grammar);
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("x");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.ACCEPT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }

        tokens = new ArrayList<>();
        tokens.add("a");
        tokens.add("x");
        tokens.add("x");
        tokens.add("x");
        tokens.add("x");
        tokens.add("b");

        try {
            Parse parse = parser.parse(tokens, opt);
            Assert.assertEquals(Status.REJECT, parse.getStatus());
        } catch (PepException ex) {
            fail();
        }
    }

}
