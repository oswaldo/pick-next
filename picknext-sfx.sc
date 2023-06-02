#!/usr/bin/env -S scala-cli shebang -S 3

//WIP

//Similar to picknext.sc, but using ScalaFX for rendering the names on the screen, with a close button just below the names.

//> using dep org.scalafx::scalafx:20.0.0-R31

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font
import scalafx.scene.text.FontWeight
import scalafx.scene.text.Text
import scala.util.Random.{nextInt, shuffle}

// Define the roles and the people who can take them
// You can use as many roles as you want. At least one role is required and there should be enough people to take them.
// You could take this as an example
val Roles = "mediator" :: "time keeper" :: "note taker" :: Nil
// Or keep it simple with a single role "mediator"
// val Roles = "mediator" :: Nil

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

// Generate the sentences
val sentences = results.map { case (role, person) =>
  val adjective = Adjectives(nextInt(Adjectives.size))
  val emoji = Emojis(nextInt(Emojis.size))
  s"The next $adjective $role is $person! $emoji"
}

// Create the UI
val app: JFXApp3 = new JFXApp3 {
    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
        title.value = "PickNext"
            scene = new Scene {
                content = new VBox {
                    padding = Insets(20)
                    spacing = 10
                    children = sentences.map { sentence =>
                        new Label {
                            text = sentence
                            font = Font.font("Verdana", FontWeight.Bold, 20)
                        }
                    }
                    children += new Button {
                        text = "Close"
                        onAction = _ => close()
                    }
                }
            }
        }
    }
}

// Start the UI
app.main(Array.empty)
