package net.notcoded.nqt.utils.fixes.tooltipfix;

/*
- Tooltip Fix "borrowed" from
- kyrptonaught | https://github.com/kyrptonaught/tooltipfix/blob/1.20/src/main/java/net/kyrptonaught/tooltipfix/OrderedTextToTextVisitor.java
*/

import net.minecraft.text.*;

public class OrderedTextToTextVisitor implements CharacterVisitor {
    private final MutableText text = Text.empty();

    @Override
    public boolean accept(int index, Style style, int codePoint) {
        String car = new String(Character.toChars(codePoint));
        text.append(Text.literal(car).setStyle(style));
        return true;
    }

    public Text getText() {
        return text;
    }

    public static Text get(OrderedText text) {
        OrderedTextToTextVisitor visitor = new OrderedTextToTextVisitor();
        text.accept(visitor);
        return visitor.getText();
    }
}