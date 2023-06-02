# PickNext

## Description

PickNext is a simple tool to help you pick the persons who will take certain roles in an upcoming meeting.

For instance, if a team rotates the role of mediator, PickNext can be used at the beginning of the meeting as a fun way to pick the next mediator.

Also, if for the next meeting the role names are different, you can easily change them the values defined at the beginning of the script.

It was implemented as a scala-cli script to be run from the command line with a simple `./picknext.sc` call.

This solution is totally safe to use even considering GDPR restrictions as it does not store any personal data or whatsoever, and does not require any external service call.

The fun added by this solution lies in the positive sentences to present the results. Instead of a boring "The next mediator is John Doe", it will print "The next super awesome mediator is John Doe! 🎉"

Case multiple roles are defined, the script will print the results for each role in a separate line with different random positive adjectives added to the role and random positive emoji at the end.

## Requirements

If you are using Mac OS, you can install scala-cli with Homebrew:

```bash
brew install Virtuslab/scala-cli/scala-cli
```

Or go to Scala CLI [installation page](https://scala-cli.virtuslab.org/install) for other options.

## Usage

Clone this repository and change the values defined at the beginning of the script to match your needs.

Then, run the script with `./picknext.sc` and it will print the selected people.

## Why the code looks kind of funny?

It was written mainly to experiment with GitHub Copilot in a super simple self contained problem, to get a feeling of the interference between documentation and generated code.
