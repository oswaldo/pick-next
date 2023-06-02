#!/usr/bin/env -S scala-cli shebang -S 3

//This is a scala-cli shebang script, so you can run it directly from the command line with ./picknext.sc

import scala.util.Random.{nextInt, shuffle}

// Define the roles and the people who can take them
// You can use as many roles as you want. At least one role is required and there should be enough people to take them.
// You could take this as an example
// val Roles = "mediator" :: "time keeper" :: "note taker" :: Nil
// Or keep it simple with a single role "mediator"
val Roles = "mediator" :: Nil

// Define the people who can take the roles
// You can use as many people as you want. At least one person is required and there should be enough people to take the roles.
val People = "John Doe" :: "Jane Doe" :: "Foo Bar" :: Nil

// Define the positive adjectives to be used when presenting the results
// You can use as many adjectives as you want. No adjectives is ok, but boring 😉
val Adjectives = "awesome" :: "cool" :: "marvelous" :: "focused" :: "super" :: "great" :: "fantastic" :: "amazing" :: "incredible" :: "wonderful" :: "fabulous" :: "excellent" :: "nice" :: "superb" :: "splendid" :: "perfect" :: "brilliant" :: "outstanding" :: "exceptional" :: "magnificent" :: "terrific" :: "stupendous" :: "phenomenal" :: "tremendous" :: "spectacular" :: "remarkable" :: "extraordinary" :: "mind-blowing" :: "mind-boggling" :: "mind-bending" :: "mind-expanding" :: "mind-stretching" :: "mind-altering" :: "mind-melting" :: Nil

// Define the nice emojis to be used when presenting the results
// You can use as many emojis as you want. No emojis is ok, but boring, but why would you want that???
val Emojis = "🎉" :: "🎇" :: "🔥" :: "❤️" :: "🙌" :: "👏🏽" :: "🎈" :: "🌟" :: "💯" :: "🍾" :: "🎁" :: "🌺" :: "✨" :: Nil

//Now the fun begins
//Pick a random person for each role, guaranteeing that no person is picked twice
val results = shuffle(People).zip(shuffle(Roles)).map { case (person, role) => (role, person) }

//Some message to increase the tension...
println("... by the powers of the universe, the next people to take the roles are...")
println()

//Wait for it...
Thread.sleep(2000)

//Print the results
results.foreach { case (role, person) =>
  val adjective = Adjectives(nextInt(Adjectives.size))
  val emoji = Emojis(nextInt(Emojis.size))
  println(s"The next $adjective $role is $person! $emoji")
}

//That's all folks!
