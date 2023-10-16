#!/usr/bin/env -S scala-cli shebang -S 3

//> using dep org.scala-lang::toolkit:0.2.0

//This is a scala-cli shebang script, so you can run it directly from the command line with ./picknext.sc

import scala.util.Random.{nextInt, shuffle}
import java.time.LocalDate
import os.*
import upickle.default.*

given ReadWriter[LocalDate] = readwriter[ujson.Value]
    .bimap[LocalDate](
      x => ujson.Str(x.toString),
      json => LocalDate.parse(json.str)
    )

//Thanks to scala toolkit including uPickle for JSON serialization and os-lib for interacting with the filesystem, we can to read the roles and people from JSON files into the following case classes
case class Role(name: String) derives ReadWriter
case class Person(name: String, lastRole: Option[Role] = None, lastTime: Option[LocalDate] = None) derives ReadWriter
//Data will live in a folder called "people" in 3 files: roles.json, people.json and remaining.json
//roles.json will contain a list of roles, which is not expected to change often
//people.json and remaining.json will contain a list of people, which is expected to change at every pick
//if remaining.json is empty, then people.json will be copied to remaining.json. This is to avoid picking the same person twice in a row.
//people.json will be updated with the last role and last time the person took the role
//remaining.json will be updated with the remaining people to take the roles by removing the person who was picked after confirming the person is available.
//If the person is not available, the person will still be removed from remaining.json, and will be added back to people.json as having a special role "not available"

//So, let's create a function to init the people folder and roles.json file if needed and, if it was empty, create a template roles.json (containing just "mediator" role)
val rolesFile = os.pwd / "people" / "roles.json"
def initRoles() =
  val people = os.pwd / "people"
  if !os.exists(people) then
    os.makeDir.all(people)
  if !os.exists(rolesFile) then
    os.write.over(rolesFile, """[{"name": "mediator"}]""")

//And another function to initialize the remaining.json file if needed. If it is empty, then it will be copied from people.json if people.json exists, otherwise it will be created as an template with the people "John Doe", "Jane Doe", "Max Mustermann", "Maria Musterfrau", "Fulano de Tal", "Beltrano de Tal" and "Sicrano de Tal"
val remainingFile = os.pwd / "people" / "remaining.json"
val allPeopleFile = os.pwd / "people" / "people.json"
def initRemaining() =
  if !os.exists(allPeopleFile) then
    //create an empty allPeopleFile so it can be updated later
    os.write.over(allPeopleFile, "[]")
  if !os.exists(remainingFile) || readPeople(remainingFile).isEmpty then
    println("No remaining.json file found or it is empty. Creating a template remaining.json file with some people.")
    if readPeople(allPeopleFile).nonEmpty then
      println("Copying people.json to remaining.json")
      os.copy.over(allPeopleFile, remainingFile)
    else
      println("Creating a template remaining.json file with some people.")
      //instead of a simple string, we want to build the actual objects for readability and future extensibility
      val people = List(
        Person("John Doe"),
        Person("Jane Doe"),
        Person("Max Mustermann"),
        Person("Maria Musterfrau"),
        Person("Fulano de Tal"),
        Person("Beltrano de Tal"),
        Person("Sicrano de Tal")
      )
      os.write.over(remainingFile, upickle.default.write(people))

//And another function to read the roles from roles.json
def readRoles() =
  upickle.default.read[List[Role]](os.read(rolesFile))

//And another function to read the people from people.json or remaining.json
def readPeople(peopleFile: Path) =
  upickle.default.read[List[Person]](os.read(peopleFile))

//And another function to update people.json by replacing the person with the updated person. In the case a new person was created directly in remaining.json, then the person will be added to people.json
def updatePeople(selectedPeople: List[Person]) =
  val previousAllPeople = readPeople(allPeopleFile)
  val updatedPeople = previousAllPeople.map { person =>
    selectedPeople.find(_.name == person.name).getOrElse(person)
  }
  //check for new people
  val newPeople = selectedPeople.filterNot(person => updatedPeople.exists(_.name == person.name))
  val allSelectedPeople = updatedPeople ++ newPeople
  //update people.json
  os.write.over(allPeopleFile, upickle.default.write(allSelectedPeople))
  //update remaining.json removing the ones from allPeople
  val remainingPeople = readPeople(remainingFile).filterNot(person => allSelectedPeople.exists(_.name == person.name))
  os.write.over(remainingFile, upickle.default.write(remainingPeople))


// Define the positive adjectives to be used when presenting the results
// You can use as many adjectives as you want. No adjectives is ok, but boring 😉
val Adjectives = "awesome" :: "cool" :: "marvelous" :: "focused" :: "super" :: "great" :: "fantastic" :: "amazing" :: "incredible" :: "wonderful" :: "fabulous" :: "excellent" :: "nice" :: "superb" :: "splendid" :: "perfect" :: "brilliant" :: "outstanding" :: "exceptional" :: "magnificent" :: "terrific" :: "stupendous" :: "phenomenal" :: "tremendous" :: "spectacular" :: "remarkable" :: "extraordinary" :: "mind-blowing" :: "mind-boggling" :: "mind-bending" :: "mind-expanding" :: "mind-stretching" :: "mind-altering" :: "mind-melting" :: Nil

// Define the nice emojis to be used when presenting the results
// You can use as many emojis as you want. No emojis is ok, but boring, but why would you want that???
val Emojis = "🎉" :: "🎇" :: "🔥" :: "❤️" :: "🙌" :: "👏🏽" :: "🎈" :: "🌟" :: "💯" :: "🍾" :: "🎁" :: "🌺" :: "✨" :: Nil

//Now let's define a function to pick the next people to take the roles
def pickAndPrint(remainingPeople: List[Person], remainingRoles: List[Role]) = 
  //Pick a random person for each role, guaranteeing that no person is picked twice
  val selection = shuffle(remainingPeople).zip(shuffle(remainingRoles)).map { case (person, role) => (role, person) }

  //Some message to increase the tension...
  println("... by the powers of the universe, the next people to take the roles are...")
  println()

  //Wait for it...
  //Thread.sleep(2000)

  //Print the results
  selection.foreach { case (role, person) =>
    val adjective = Adjectives(nextInt(Adjectives.size))
    val emoji = Emojis(nextInt(Emojis.size))
    println(s"The next $adjective ${role.name} is ${person.name}! $emoji")
  }

  selection

//Finally, we need a function to ask the user if each pearson is available to take the role, updating the people and remaining files accordingly and returning the roles that need another round of picking
def askForNotAvailable(selection: List[(Role, Person)]): List[Role] =
  //Ask the user if each pearson is available to take the role
  val availability: List[Person] =
    selection.map { case (role, person) =>
      val answer = System.console.readLine(s"Is ${person.name} available to take the ${role.name} role? (y/n) ")
      if answer == "y" || answer == "yes" then
        person.copy(lastRole = Some(role), lastTime = Some(LocalDate.now()))
      else
        person.copy(lastRole = Some(Role("not available")), lastTime = Some(LocalDate.now()))
    }
  updatePeople(availability)
  //returns roles mentioned in selection but missing from the availability list, so those can be retried
  selection.map(_._1).filterNot(role => availability.exists(_.lastRole.contains(role)))
  


//Now the fun begins
//Let's prepare to pick the next people to take the roles
//First, init the people folder and roles.json file if needed
initRoles()
//Then, init the remaining.json file if needed
initRemaining()
//Then, read the roles from roles.json
var roles = readRoles()
//Then, keep picking people until all roles are filled
while roles.nonEmpty do
  //Read the remaining people from remaining.json
  val remaining = readPeople(remainingFile)
  //Pick the next people to take the roles
  val selection = pickAndPrint(remaining, roles)
  //Ask the user if each pearson is available to take the role, updating the people and remaining files accordingly and returning the roles that need another round of picking
  roles = askForNotAvailable(selection)

//That's all folks!
