package eu.europa.ted.efx.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import eu.europa.ted.efx.interfaces.MarkupGenerator;

public class ContentBlock {
  private final ContentBlock parent;
  private final String id;
  private final Integer indentationLevel;
  private final Markup content;
  private final Context context;
  private final Queue<ContentBlock> children = new LinkedList<>();
  private final int number;

  private ContentBlock() {
    this.parent = null;
    this.id = "block";
    this.indentationLevel = -1;
    this.content = new Markup("");
    this.context = null;
    this.number = 0;
  }

  public ContentBlock(final ContentBlock parent, final String id, final int number,
      final Markup content, Context contextPath) {
    this.parent = parent;
    this.id = id;
    this.indentationLevel = parent.indentationLevel + 1;
    this.content = content;
    this.context = contextPath;
    this.number = number;
  }

  public static ContentBlock newRootBlock() {
    return new ContentBlock();
  }

  public ContentBlock addChild(final int number, final Markup content, final Context context) {
    // number < 0 means "autogenerate", number == 0 means "no number", number > 0 means "use this number"
    final int outlineNumber = number >= 0 ? number
        : children.stream().map(b -> b.number).max(Comparator.naturalOrder()).orElse(0) + 1;

    String newBlockId = String.format("%s%02d", this.id, this.children.size() + 1);
    ContentBlock newBlock = new ContentBlock(this, newBlockId, outlineNumber, content, context);
    this.children.add(newBlock);
    return newBlock;
  }

  public ContentBlock addSibling(final int number, final Markup content, final Context context) {
    if (this.parent == null) {
      throw new ParseCancellationException("Cannot add sibling to root block");
    }
    return this.parent.addChild(number, content, context);
  }

  public ContentBlock findParentByLevel(final int parentIndentationLevel) {

    assert this.indentationLevel >= parentIndentationLevel : "Unexpected indentation tracker state.";

    ContentBlock targetBlock = this;
    while (targetBlock.indentationLevel > parentIndentationLevel) {
      targetBlock = targetBlock.parent;
    }
    return targetBlock;
  }

  public Queue<ContentBlock> getChildren() {
    return this.children;
  }

  public String getOutlineNumber() {
    if (this.number == 0 || this.children.size() == 0) {
      return "";
    }

    if (this.parent == null || this.parent.number == 0) {
      return String.format("%d", this.number);
    }

    final String parentNumber = this.parent.getOutlineNumber();
    if (parentNumber.isEmpty()) {
      return String.format("%d", this.number);
    }

    return String.format("%s.%d", parentNumber, this.number);
  }

  public Integer getIndentationLevel() {
    return this.indentationLevel;
  }

  public Context getContext() {
    return this.context;
  }

  public Context getParentContext() {
    if (this.parent == null) {
      return null;
    }
    return this.parent.getContext();
  }

  public Markup renderContent(MarkupGenerator markupGenerator) {
    StringBuilder sb = new StringBuilder();
    sb.append(this.content.script);
    for (ContentBlock child : this.children) {
      sb.append('\n').append(child.renderCallTemplate(markupGenerator).script);
    }
    return new Markup(sb.toString());
  }

  public void renderTemplate(MarkupGenerator markupGenerator, List<Markup> templates) {
    templates.add(markupGenerator.composeFragmentDefinition(this.id, this.getOutlineNumber(),
        this.renderContent(markupGenerator)));
    for (ContentBlock child : this.children) {
      child.renderTemplate(markupGenerator, templates);
    }
  }

  public Markup renderCallTemplate(MarkupGenerator markupGenerator) {
    return markupGenerator.renderFragmentInvocation(this.id, this.context.relativePath());
  }
}
