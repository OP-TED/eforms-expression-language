package eu.europa.ted.efx.interfaces;

import org.antlr.v4.runtime.BaseErrorListener;

public interface TranslatorDependencyFactory {

  /**
   * Get the instance of the symbol resolver to be used by the translator to resolve symbols.
   */
  public SymbolResolver createSymbolResolver(String sdkVersion);

  /**
   * Get the instance of the script generator to be used by the translator to translate expressions.
   */
  public ScriptGenerator createScriptGenerator(String sdkVersion);

  /**
   * Get the instance of the markup generator to be used by the translator for rendering the target
   * template.
   */
  public MarkupGenerator createMarkupGenerator(String sdkVersion);

  /**
   * Get the instance of the error listener to be used for handling translation errors.
   */
  public BaseErrorListener createErrorListener();
}
