package net.notcoded.nqt.utils.fixes;

/*
- Tooltip Fix "borrowed" from
- kyrptonaught | https://github.com/kyrptonaught/tooltipfix/blob/1.18/src/main/java/net/kyrptonaught/tooltipfix/mixin/FixToolTipMixin.java
*/

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolTipFix {
    public static int x, width;
    private static int mouseX;
    static boolean flipped;

    public static void set(int x, int width) {
        ToolTipFix.x = Math.max(0, x);
        ToolTipFix.width = width - 20;
        mouseX = ToolTipFix.x - 24;
        flipped = false;
    }

    public static List<Text> doFix(List<Text> text, TextRenderer textRenderer) {
        List<Text> originalText = text;
        text = new ArrayList<>(text);

        if (text.size() == 0 || (text.size() == 1 && text.get(0).getString().length() <= 12))
            return text;
        for (int i = 0; i < text.size(); i++) {
            if (isTooWide(textRenderer, text.get(i).getString())) {
                Style style = text.get(i).getStyle();
                List<String> words = new ArrayList<>(Arrays.asList(text.get(i).getString().split(" ")));
                if (words.isEmpty()) return text;

                String newLine = words.remove(0);
                if (isTooWide(textRenderer, newLine)) {
                    if (!flipped && x > width / 2) {
                        flipped = true;
                        return doFix(originalText, textRenderer);
                    } else {
                        String oldLine = newLine;
                        while (isTooWide(textRenderer, newLine + "-")) {
                            newLine = newLine.substring(0, newLine.length() - 1);
                        }
                        words.add(0, "-" + oldLine.substring(newLine.length()));
                        newLine = newLine + "-";
                    }
                } else
                    while (words.size() > 0)
                        if (!isTooWide(textRenderer, newLine + " " + words.get(0)))
                            newLine += " " + words.remove(0);
                        else break;
                text.set(i, Text.literal(newLine).setStyle(style));
                if (words.size() > 0)
                    text.add(i + 1, Text.literal(String.join(" ", words)).setStyle(style));
            }
        }
        return text;
    }

    private static boolean isTooWide(TextRenderer textRenderer, String line) {
        if (flipped) {
            if (mouseX - textRenderer.getWidth(line) > 0) {
                attemptUpdateMaxWidth(line, textRenderer);
                return false;
            }
            return true;
        }
        return x + textRenderer.getWidth(line) > width;
    }

    private static void attemptUpdateMaxWidth(String newLine, TextRenderer textRenderer) {
        int lineWidth = textRenderer.getWidth(newLine);
        if (lineWidth > mouseX - x)
            x = mouseX - lineWidth;
    }
}
