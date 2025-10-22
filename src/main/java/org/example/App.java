//package org.example;
//import klondike.controller.KlondikeController;
//import klondike.controller.KlondikeTextualController;
//import klondike.model.hw02.*;
//import klondike.view.KlondikeTextualView;
//
//import java.io.InputStreamReader;
//import java.util.List;
//
//public class App
//{
//    public static void main( String[] args ) {
////        BasicKlondike model = new BasicKlondike();
////        List<KlondikeCard> deck = model.createNewDeck();
////        model.startGame(deck, true, 7, 1);
////        KlondikeTextualView view = new KlondikeTextualView(model);
////        System.out.println(view);
//
//        // Create the model
//        BasicKlondike model = new BasicKlondike();
//
//        // Create a standard deck
//        List<KlondikeCard> deck = model.createNewDeck();
//
//        // Create the controller with console input/output
//        Readable input = new InputStreamReader(System.in);
//        Appendable output = System.out;
//        KlondikeController controller = new KlondikeTextualController(input, output);
//
//        // Display welcome message
//        System.out.println("Welcome to Klondike Solitaire!");
//        System.out.println("Commands:");
//        System.out.println("  mpp <src> <numCards> <dest> - Move cards between cascade piles");
//        System.out.println("  md <dest>                    - Move top draw card to cascade pile");
//        System.out.println("  mpf <src> <foundation>       - Move card to foundation");
//        System.out.println("  mdf <foundation>             - Move draw card to foundation");
//        System.out.println("  dd                           - Discard top draw card");
//        System.out.println("  q or Q                       - Quit game");
//        System.out.println("\nNote: All pile numbers start from 1");
//        System.out.println("\nStarting game with 7 piles and 3 draw cards...\n");
//
//        // Play the game
//        controller.playGame(model, deck, false, 7, 3);
//
//        System.out.println("\nThank you for playing!");
//    }
//}
