package help

import scala.io.Source 
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.io._;
import java.io.File
import scala.io.StdIn.readLine;
import org.mongodb.scala._
import org.mongodb.scala.model._
import org.mongodb.scala.model.InsertOneModel
import org.mongodb.scala.model.InsertManyOptions
import org.mongodb.scala.model.InsertOneOptions
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.Document
import help.Helper._


object CardData 
{
    def main(args: Array[String])
    {
        println("Accessing Card Database")

        val client: MongoClient = MongoClient()
        val database: MongoDatabase = client.getDatabase("Project0")
        var status: Boolean = true
        var deck: String = ""
        var choice: String = ""
        var username: String = ""
        var usertype: String = ""
        var usercmc: String = ""
        var amount: String = ""
        var username1: String = ""
        var lookin: String = ""
        var countin: String = ""
        var mult: String = ""
        var coll: String = ""
        var inst: Int = 0

// runs the operation until the user is finished by initiating a while loop
        while (status == true)

            {
                println("What would you like to do? (type comm to see commands)")
                choice = scala.io.StdIn.readLine()

                if (choice.equalsIgnoreCase("comm"))
                {
                    println("\n add- lets you add a card to desired location"+
                    "\n drop- lets you remove a card from desired location"+
                    "\n look- checks to see if a card is in the database"+
                    "\n update- lets you change a cards information in desired location"+
                    "\n count- will count the amount of cards of a certain name or type within the desired loaction (valid types: land, creature, enchantment, artifact, instant, sorcery, planeswalker)"+
                    "\n show- shows the list of the available collections in the database"+
                    "\n expand- shows the deck list of chosen deck"+
                    "\n quit- exit")

                }
                


                //add a card to the specified collection
                else if (choice.equalsIgnoreCase("add"))
                {
                    //specify the desire collection
                    println("Where would you like to add a card?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)

                    println("Is it multiples of the same card?")
                    mult = scala.io.StdIn.readLine()

                    if (mult.equalsIgnoreCase("yes"))
                    {
                        //collect card information
                        println("What is the name?")
                        username = scala.io.StdIn.readLine()
                        println("What is the card type?")
                        usertype = scala.io.StdIn.readLine()
                        println("What is the cmc?")
                        usercmc = scala.io.StdIn.readLine()
                        println("How many?")
                        inst = scala.io.StdIn.readInt()
                        for (i <- 1 to inst)
                        {
                            var newdoc: Document = Document("Name" -> username, "Type" -> usertype, "CMC" -> usercmc)
                            collection.insertOne(newdoc).printResults()
                        }
                    }
                    else if (mult.equalsIgnoreCase("no"))
                    {
                        //collect card information
                        println("What is the name?")
                        username = scala.io.StdIn.readLine()
                        println("What is the card type?")
                        usertype = scala.io.StdIn.readLine()
                        println("What is the cmc?")
                        usercmc = scala.io.StdIn.readLine()
                        var newdoc: Document = Document("Name" -> username, "Type" -> usertype, "CMC" -> usercmc)
                        collection.insertOne(newdoc).printResults()
                    }

                    else
                    {
                    println("Not a valid input")
                    }
                }

                //Remove card from the specified location
                else if (choice.equalsIgnoreCase("drop"))
                {
                    //specify the desired collection
                    println("Where would you like to remove a card?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)

                    //choose how many instances to remove
                    println("Delete all or one instance of the card?")
                    amount = scala.io.StdIn.readLine()

                    if (amount.equalsIgnoreCase("all"))
                    {
                        //collect card information
                        println("What is the name?")
                        username = scala.io.StdIn.readLine()
                        collection.deleteMany(equal("Name", username)).printResults()
                    }
                     else if (amount.equalsIgnoreCase("one"))
                    {
                        //collect card information
                        println("What is the name?")
                        username = scala.io.StdIn.readLine()
                        collection.deleteOne(equal("Name", username)).printResults()
                    }

                     else
                    {
                    println("Not a valid input")
                    }
                }

                //find a card(s) matching the specified data
                else if (choice.equalsIgnoreCase("look"))
                {
                    //specify the search location
                    println("Where would you like to look?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)

                    //specify the desired search parameter
                    println("What would you like to search? (name, type, cmc)")
                    lookin = scala.io.StdIn.readLine()

                    if (lookin.equalsIgnoreCase("name"))
                    {
                        println("What is the card name?")
                        username = scala.io.StdIn.readLine()
                        collection.find(equal("Name", username)).printResults()
                    }

                    else if (lookin.equalsIgnoreCase("type"))
                    {
                        println("What is the card type?")
                        usertype = scala.io.StdIn.readLine()
                        collection.find(equal("Type", usertype )).printResults()
                    }

                    else if (lookin.equalsIgnoreCase("cmc"))
                    {
                        println("What is the card cmc?")
                        usercmc = scala.io.StdIn.readLine()
                        collection.find(equal("CMC", usercmc )).printResults()
                    }

                     else
                    {
                        println("Not a valid input")
                    }

                }
                //change the information of a card by replacing it
                else if (choice.equalsIgnoreCase("update"))
                {
                    //specify the desired collection
                    println("Where would you like to update a card?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)

                    //choose what card to replace
                    println("What is the name of the card you are updating?")
                    username1 = scala.io.StdIn.readLine()
                    
                    //collected update data
                     println("What is the name?")
                    username = scala.io.StdIn.readLine()
                    println("What is the card type?")
                    usertype = scala.io.StdIn.readLine()
                    println("What is the cmc?")
                    usercmc = scala.io.StdIn.readLine()

                    collection.replaceOne(equal("Name", username1), 
                    Document("Name" -> username, "Type" -> usertype, "CMC" -> usercmc)
                    ).printResults()
                }

                else if (choice.equalsIgnoreCase("count"))
                {
                    //specify the desired collection
                    println("Where would you like to count cards?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)

                    //specify what you are counting
                    println("What are you Counting?"+
                    "\n name- count cards with the chose name"+
                    "\n type- count cards with the chosen type"+
                    "\n cmc- count cards with the chosen cmc"+
                    "\n all- count the total amount of cards in the collection")
                    countin = scala.io.StdIn.readLine()

                    if (countin.equalsIgnoreCase("name"))
                    {
                        println("What is the card name?")
                        username = scala.io.StdIn.readLine()
                        collection.countDocuments(equal("Name", username)).printResults()
                    }

                    else if (countin.equalsIgnoreCase("type"))
                    {
                        println("What is the card type? (Creature, Enchantment, Sorcery, Land, Instant, Artifact, or Planeswalker)")
                        usertype= scala.io.StdIn.readLine()
                        collection.countDocuments(equal("Type", usertype)).printResults()
                    }
                    
                    else if (countin.equalsIgnoreCase("cmc"))
                    {
                        println("What is the card cmc?")
                        usercmc = scala.io.StdIn.readLine()
                        collection.countDocuments(equal("CMC", usercmc)).printResults()
                    }

                    else if (countin.equalsIgnoreCase("all"))
                    {
                        collection.countDocuments.printResults()
                    }

                     else
                    {
                    println("Not a valid input")
                    }

                }

                else if (choice.equalsIgnoreCase("show"))
                {
                   database.listCollectionNames().printResults()
                }

                //Show the entire list of chosen deck
                else if (choice.equalsIgnoreCase("expand"))
                {
                    //specify the search location
                    println("Where deck you like to expand?")
                    deck = scala.io.StdIn.readLine()
                    //establish connection to the correct collection
                    var collection: MongoCollection[Document] = database.getCollection(deck)
                    collection.find().printResults()
                }

                //println("Would you like to continue? (yes/no)")
                //var answer: String = scala.io.StdIn.readLine()

                else if (choice.equalsIgnoreCase("quit"))
                {
                status = false
                println("Goodbye")
                }

                 
                 
                 else
                {
                    println("Not a valid input")
                }
            }
    }
}