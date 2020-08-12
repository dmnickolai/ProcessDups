/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processphotodups;

/**
 *
 * @author Dennis
 */
enum ActionButton {
    
    MOVE ("Move"),
    SKIP("Skip"),
    REPLACE("Replace"),
    DELETE("Delete"),
    RENAME("Rename");

    private String text;

    ActionButton(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ActionButton fromString(String text) {
        for (ActionButton b : ActionButton.values()) {
            if (b.text.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
