package eu.europa.ted.efx.interfaces;

import java.util.List;
import eu.europa.ted.efx.model.Expression;
import eu.europa.ted.efx.model.Expression.PathExpression;
import eu.europa.ted.efx.model.Expression.StringExpression;
import eu.europa.ted.efx.model.Markup;

/**
 * The role of this interface is to allow the reuse of the {@link EfxTemplateTranslator} to generate
 * markup for any target template language,
 * 
 * The methods provided by this interface cover two needs: a) Take an {@link Expression} as a
 * parameter and generate the target template markup necessary for rendering it; and b) Take
 * multiple {@link Markup} objects already generated by other method calls and generate the markup
 * to properly combine them in the target template.
 */
public interface MarkupGenerator {

    /**
     * Given a body (main content) and a set of fragments, this
     * method returns the full content of the target template file.
     */
    Markup composeOutputFile(final List<Markup> content, final List<Markup> fragments);

    /**
     * Given an expression (which will eventually, at runtime, evaluate to the value of a field), this
     * method returns the template code that dereferences it (retrieves the value) in the
     * target template.
     */
    Markup renderVariableExpression(final Expression variableExpression);

    /**
     * Given a label key (which will eventually, at runtime, be dereferenced to a label text), this
     * method returns the template code that renders this label in the target template
     * language.
     */
    Markup renderLabelFromKey(final StringExpression key);

    /**
     * Given an expression (which will eventually, at runtime, be evaluated to a label key and
     * subsequently dereferenced to a label text), this method returns the template code that
     * renders this label in the target template language.
     */
    Markup renderLabelFromExpression(final Expression expression);

    /**
     * Given a string of free text, this method returns the template code that adds this text
     * in the target template.
     */
    Markup renderFreeText(final String freeText);

    /**
     * Given a fragment name (identifier) and some pre-rendered content, this method returns
     * the code that encapsulates it in the target template.
     */
    Markup composeFragmentDefinition(final String name, String number, Markup content);

    /**
     * Given a fragment name (identifier), and an evaluation context, this method returns the
     * code that invokes (uses) the fragment.
     */
    Markup renderFragmentInvocation(final String name, final PathExpression context);
}
