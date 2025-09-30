package org.example;
import klondike.model.hw02.*;
import klondike.view.KlondikeTextualView;

import java.util.List;

public class App 
{
    public static void main( String[] args ) {
        BasicKlondike model = new BasicKlondike();
        List<KlondikeCard> deck = model.createNewDeck();
        model.startGame(deck, true, 7, 1);
        KlondikeTextualView view = new KlondikeTextualView(model);
        System.out.println(view);
    }
}
