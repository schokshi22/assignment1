package ass1;

import ass1.Card;

public class WarGame
{
    //static long seed = System.currentTimeMillis();
    private static SQueue<Card> player1DiscardPile = new SQueue<>(52);
    private static SQueue<Card> player2DiscardPile = new SQueue<>(52);


    public static void main(String[] args)
    {
        System.out.println("Welcome to the Game of War!\n");
        System.out.println("Now dealing cards to the players\n");
        int maxRounds = 0; //Maxiumum number of rounds, read from command line
        int rounds = -1;
        String result = "";


        if (args.length > 0) 
        {
            try
            {
                maxRounds = Integer.parseInt(args[0]);
            }
            catch(NumberFormatException e)
            {
                System.err.print("Invalid input for max rounds.");
                System.exit(1);
            }
            
        }

        //create a standard deck of cards and shuffle it
        SQueue<Card> deck = createShuffledDeck();

        //split deck between two players
        SQueue<Card> player1Hand = new SQueue<>(52);
        SQueue<Card> player2Hand = new SQueue<>(52);


        for (int i = 0; i < 52; i++)
        {
            if (i % 2 == 0)
            {
                player1Hand.enqueue(deck.dequeue());
            }
            else
            {
                player2Hand.enqueue(deck.dequeue());
            }
        }

        //print the initial state of Player 1's deck
        System.out.println("Player 1's Deck:");
        System.out.println(player1Hand + "\n");

        //print the initial state of player 2's deck 
        System.out.println("Player 2's Deck:");
        System.out.println(player2Hand + "\n");

        //print the game start message
        System.out.println("Starting the game of War!");
        System.out.println("Max number of rounds = " + maxRounds);
        

        while(rounds < maxRounds && !player1Hand.isEmpty() && !player2Hand.isEmpty())        
        {
            rounds++;
            result = playRound(player1Hand, player2Hand, rounds);
            System.out.println("Round " + rounds + " " + result);

            //check if either player is out of cards 
            if(player1Hand.isEmpty() && player1DiscardPile.getSize() < 2)
            {
                System.out.println("Player 1 is out of cards. Game over.");
                break;
            } 

            if(player2Hand.isEmpty() && player2DiscardPile.getSize() < 2)
            {
                System.out.println("Player 2 is out of cards. Game over.");
                break;
            }
        }

        //determine the winner based on the number of cards remaining 
        System.out.println("After " + rounds + " rounds here are the results:");
        System.out.println("Player 1: " + player1Hand.getSize() + " cards");
        System.out.println("Player 2: " + player2Hand.getSize() + " cards");
        if (player1Hand.getSize() > player2Hand.getSize())
        {
            System.out.println("Player 1 wins!");
        }
        else if (player2Hand.getSize() > player1Hand.getSize())
        {
            System.out.println("Player 2 wins!");
        }
        else
        {
            System.out.println("It's a tie!");
        }

    }

    //helper method to create a shuffled deck of cards
    private static SQueue<Card> createShuffledDeck()
    {
        SQueue<Card> deck = new SQueue<>(52);
        Card.Suits[] suits = Card.Suits.values();
        Card.Ranks[] ranks = Card.Ranks.values();
        
        
        for (Card.Suits suit : suits)
        {
            for(Card.Ranks rank : ranks)
            {
                deck.enqueue(new Card(suit, rank));
            }
        }
        //print the initial deck 
        System.out.println("INITIAL DECK:");
        System.out.println(deck + "\n");


        deck.shuffle(); // shuffle the deck using shuffle method in SQueue


        //print the deck after shuffling 
        System.out.println("INITIAL DECK AFTER SHUFFLING:");
        System.out.println(deck + "\n");

        return deck;
    }

    //helper method to play a single round of the game
    private static String playRound(SQueue<Card> player1Hand, SQueue<Card> player2Hand, int rounds)
    {
        String result = "";

        //check if either player is out of cards. 
        if(player1Hand.isEmpty() || player2Hand.isEmpty())
        {
            System.out.println("Game over!");
            return " ";
        }

        //check if player 1's hand is empty and reshuffle discard pile if needed 
        if(player1Hand.isEmpty())
        {
            System.out.println("Getting and shuffling the pile for Player 1.");
            reshuffleDiscardPile(player1Hand, player1DiscardPile);
        }

        //check if player 2's hand is empty and reshuffle discard pile if needed 
        if(player2Hand.isEmpty())
        {
            System.out.println("Getting and shuffling the pule for Player 2.");
            reshuffleDiscardPile(player2Hand, player2DiscardPile);
        }

        Card card1 = player1Hand.dequeue();
        Card card2 = player2Hand.dequeue();

        if (card1.compareTo(card2) > 0)
        {
            player1DiscardPile.enqueue(card1);
            player1DiscardPile.enqueue(card2);
            result = "Player 1 Wins: " + card1 + " beats " + card2;
        }
        else if (card1.compareTo(card2) < 0)
        {
            player2DiscardPile.enqueue(card1);
            player2DiscardPile.enqueue(card2);
            result = "Player 2 Wins: " + card1 + " loses to " + card2;
        }
        else if (card1.compareTo(card2) == 0)
        {
            System.out.println("WAR!");
            System.out.println("Round " + rounds + " " + result + " " + card1 + " ties " + card2);
            System.out.println("Face down.");


            SQueue<Card> warPile = new SQueue<>(52);
            warPile.enqueue(card1);
            warPile.enqueue(card2);

            boolean warOver = false;
            while(!warOver)
            {
                if(player1Hand.getSize() < 2 || player2Hand.getSize() < 2)
                {
                    //one  of the players doesn't have enough cards for a full war, end the game 
                    System.out.println("Not enough cards for full War game. Game over.");
                    System.exit(0);
                }

                Card warCard1 = player1Hand.dequeue();
                Card warCard2 = player2Hand.dequeue();
                warPile.enqueue(warCard1);
                warPile.enqueue(warCard2);

                Card warCard3 = player1Hand.dequeue();
                Card warCard4 = player2Hand.dequeue();
                warPile.enqueue(warCard3);
                warPile.enqueue(warCard4);

                System.out.println("Player 1 plays: " + warCard3);
                System.out.println("Player 2 plays: " + warCard4);

                if (warCard3.compareTo(warCard4) > 0)
                {
                    while (!warPile.isEmpty())
                    {
                        player1DiscardPile.enqueue(warPile.dequeue());
                    }
                    result = "Player 1 Wins: " + warCard3 + " beats " + warCard4;
                    warOver = true;
                }
                else if (warCard3.compareTo(warCard4) < 0)
                {
                    while (!warPile.isEmpty())
                    {
                        player2DiscardPile.enqueue(warPile.dequeue());
                    }
                    result = "Player 2 Wins: " + warCard3 + " loses to " + warCard4;
                    warOver = true;
                }
                else
                {
                    System.out.println("Another war!");
                }
            }

            
        }

        return result;
    }

    //helper method to reshuffle discard pile 
    private static void reshuffleDiscardPile(SQueue<Card> playerHand, SQueue<Card> discardPile)
    {
        //check if discard pile is empty
        if(!discardPile.isEmpty())
        {
            //shuffle discard pile
            discardPile.shuffle();
        }
        //move cards from discard pile to player's hand 
        if(playerHand.isEmpty())
        {
            while(!discardPile.isEmpty())
            {
                playerHand.enqueue(discardPile.dequeue());
            }
            
        }
    }
	
}