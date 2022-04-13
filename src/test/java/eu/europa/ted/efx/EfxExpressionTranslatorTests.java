package eu.europa.ted.efx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import eu.europa.ted.efx.exceptions.ThrowingErrorListener;
import eu.europa.ted.efx.mock.MockSymbolMap;
import eu.europa.ted.efx.xpath.XPathSyntaxMap;

public class EfxExpressionTranslatorTests {

    // TODO: Currently handling multiple SDK versions is not implemented.
    final private String SDK_VERSION = "latest";

    private String test(final String context, final String expression) {
        return EfxExpressionTranslator.transpileExpression(context, expression,
                MockSymbolMap.getInstance(SDK_VERSION), new XPathSyntaxMap(),
                ThrowingErrorListener.INSTANCE);
    }

    /*** Boolean expressions ***/

    @Test
    public void testParenthesizedBooleanExpression() {
        assertEquals("(true() or true()) and false()",
                test("BT-01-Text", "(ALWAYS or TRUE) and NEVER"));
    }
    
    @Test
    public void testLogicalOrCondition() {
        assertEquals("true() or false()", test("BT-01-Text", "ALWAYS or NEVER"));
    }

    @Test
    public void testLogicalAndCondition() {
        assertEquals("true() and 1 + 1 = 2", test("BT-01-Text", "ALWAYS and 1 + 1 == 2"));
    }



    @Test
    public void testAlwaysCondition() {
        assertEquals("true()", test("BT-01-Text", "ALWAYS"));
    }

    @Test
    public void testNeverCondition() {
        assertEquals("false()", test("BT-01-Text", "NEVER"));
    }


    @Test
    public void testcomparisonCondition() {
        assertEquals("2 > 1 and 3 >= 1 and 1 = 1 and 4 < 5 and 5 <= 5",
                test("BT-01-Text", "2 > 1 and 3>=1 and 1==1 and 4<5 and 5<=5"));
    }

    @Test
    public void testInListCondition() {
        assertEquals("not('x' = ('a','b','c'))", test("BT-01-Text", "'x' not in ('a', 'b', 'c')"));
    }

    @Test
    public void testEmptinessCondition() {
    assertEquals("PathNode/TextField/normalize-space(text()) != ''",
    test("BT-01-Text", "BT-01-Text is not empty"));
    }

    @Test
    public void testPresenceCondition() {
    assertEquals("PathNode/TextField",
    test("BT-01-Text", "BT-01-Text is present"));
    }

    @Test
    public void testLikePatternCondition() {
        assertEquals("fn:matches(normalize-space('123'), '[0-9]*')",
                test("BT-01-Text", "'123' like '[0-9]*'"));
    }

    @Test
    public void testMultiplicationExpression() {
        assertEquals("3 * 4", test("BT-01-Text", "3 * 4"));
    }

    @Test
    public void testAdditionExpression() {
        assertEquals("4 + 4", test("BT-01-Text", "4 + 4"));
    }

    @Test
    public void testParenthesizedExpression() {
        assertEquals("(2 + 2) * 4", test("BT-01-Text", "(2 + 2)*4"));
    }

    @Test
    public void testExplicitList() {
        assertEquals("'a' = ('a','b','c')", test("BT-01-Text", "'a' in ('a', 'b', 'c')"));
    }

    @Test
    public void testCodeList() {
        assertEquals("'a' = ('code1','code2','code3')",
                test("BT-01-Text", "'a' in (accessibility)"));
    }


    /*** Boolean functions ***/

    @Test
    public void testNotFunction() {
        assertEquals("not(true())", test("BT-01-Text", "not(ALWAYS)"));
        assertEquals("not(1 + 1 = 2)", test("BT-01-Text", "not(1 + 1 == 2)"));
        assertThrows(ParseCancellationException.class, () -> test("BT-01-Text", "not('text')"));
    }

    @Test
    public void testContainsFunction() {
        assertEquals("contains(PathNode/TextField/normalize-space(text()), 'xyz')",
                test("ND-0", "contains(BT-01-Text, 'xyz')"));
    }

    @Test
    public void testStartsWithFunction() {
        assertEquals("starts-with(PathNode/TextField/normalize-space(text()), 'abc')",
                test("ND-0", "starts-with(BT-01-Text, 'abc')"));
    }

    @Test
    public void testEndsWithFunction() {
        assertEquals("ends-with(PathNode/TextField/normalize-space(text()), 'abc')",
                test("ND-0", "ends-with(BT-01-Text, 'abc')"));
    }

    /*** Numeric functions ***/

    @Test
    public void testCountFunction() {
        assertEquals("count(PathNode/TextField)", test("ND-0", "count(BT-01-Text)"));
    }

    @Test
    public void testNumberFunction() {
        assertEquals("number(PathNode/TextField/normalize-space(text()))",
                test("ND-0", "number(BT-01-Text)"));
    }

    @Test
    public void testSumFunction() {
        assertEquals("sum(PathNode/TextField)", test("ND-0", "sum(BT-01-Text)"));
    }

    @Test
    public void testStringLengthFunction() {
        assertEquals("string-length(PathNode/TextField/normalize-space(text()))",
                test("ND-0", "string-length(BT-01-Text)"));
    }

    /*** String functions ***/

    @Test
    public void testSubstringFunction() {
        assertEquals("substring(PathNode/TextField/normalize-space(text()), 1, 3)",
                test("ND-0", "substring(BT-01-Text, 1, 3)"));
        assertEquals("substring(PathNode/TextField/normalize-space(text()), 4)",
                test("ND-0", "substring(BT-01-Text, 4)"));
    }

    @Test
    public void testNumberToStringFunction() {
        assertEquals("string(123)", test("ND-0", "string(123)"));
    }

    @Test
    public void testConcatFunction() {
        assertEquals("concat('abc', 'def')", test("ND-0", "concat('abc', 'def')"));
    };

    @Test
    public void testFormatNumberFunction() {
        assertEquals("format-number(PathNode/TextField/normalize-space(text()), '#,##0.00')",
                test("ND-0", "format-number(BT-01-Text, '#,##0.00')"));
    }


    /*** Date functions ***/

    @Test
    public void testDateFromStringFunction() {
        assertEquals("xs:date(PathNode/TextField/normalize-space(text()))",
                test("ND-0", "date(BT-01-Text)"));
    }

    /*** Time functions ***/

    @Test
    public void testTimeFromStringFunction() {
        assertEquals("xs:time(PathNode/TextField/normalize-space(text()))",
                test("ND-0", "time(BT-01-Text)"));
    }

    /*** Duration functions ***/

    @Test
    public void testDurationFromDatesFunction() {
        assertEquals(
                "(xs:date(PathNode/TextField/normalize-space(text())) - xs:date(PathNode/TextField/normalize-space(text())))",
                test("ND-0", "duration(BT-01-Text, BT-01-Text)"));
    }
}
