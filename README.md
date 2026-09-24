# Your Dictionary

## Overview
A dictionary app with search functionality and the ability to create an account a login. 
Uses a room database with two tables, one for Users and their information, one for Favorite Words to allow for data to be stored locally.
Currently there is no ability for users to add favorite words to the table, so this would be something we'd like to add in future.
## Features
* A dashboard landing page that has a search bar and sample word/definition
* A Login page where users can use to log in to their account
* A Create an Account page so users can add an account to the database, and are logged in after doing so
* A Screen that displays the definition of a word separately from the other screens

## Installation and Usage
1) Clone the repo to your android IDE of choice that has a built in emulator,
  (Either by cloning it through Git-Hub or by running `git clone https://github.com/sarahW05/Project1.git` in the IDE terminal)
2) Run the emulator you've installed the app on and open the app, Or Hit the Run button in Android Studio
3) Create an account or login as a test user to see the icon change
4) Search on the dashboard to find a definition for your word (Not all words supported by API, fetching may be slow)

## Introduction
* All Communications were done through a Slack Group DM
* We initially considered 25 stories
* We were able to complete about 15 stories

## Team Retrospective
### Nikolii Proud
1. Niko's Pull Requests are [here](https://github.com/sarahW05/Project1/pulls?q=is%3Apr+state%3Aclosed+author%3ANiko-Proud)
2. Niko's Issues are [here](https://github.com/sarahW05/Project1/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3ANiko-Proud)

#### What was your role / which stories did you work on
Niko mostly did work on the Database and Kotlin objects and their D.A.O.'s, as well as gap filling in other places, like setting up the functionality for persistent login,
and implementing the Code Smells plugins like Detekt

+ What was the biggest challenge?
  + The biggest challenge was probably that we just had so much new information to learn in so little time to be able to put anything together.
+ Why was it a challenge?
  + Kotlin and compose were both almost entirely new languages/systems for me.
+ Favorite / most interesting part of this project
  + My favorite part was that it ended up being a lot simpler to do some of our ideas than we initially believed they would be
+ What is the most valuable thing you learned?
  + Assign issues to yourself and your teammates early and often, make sure everyone knows what they're working on and what each-other is working on.

### Sarah Wafa
1. Sarah's Pull Requests are [here](https://github.com/sarahW05/Project1/pulls?q=is%3Apr+state%3Aclosed+author%3AsarahW05)
2. Sarah's Issues are [here](https://github.com/sarahW05/Project1/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3AsarahW05)

#### What was your role / which stories did you work on
Sarah worked on the functionality driving the Login and User Settings pages, as well as coordinating team efforts for members who finished their work a little faster than expected
towards work that needed to be done

+ What was the biggest challenge?
  + Biggest challenge was learning Kotlin since. Also it had been a little bit since I had worked with git so getting back into the flow of pushing and pulling.
+ Why was it a challenge?
  +  It was a new language and I had never interacted with it before
+ Favorite / most interesting part of this project
  + My favorite part was everyone working well with eachother and helping one another if needed. I also enjoyed learning jetpack compose.
+ What is the most valuable thing you learned?
  + Most valuable thing I learned was jetpack compose.

### Michael Conley
1. Michael's Pull Requests are [here](https://github.com/sarahW05/Project1/pulls?q=is%3Apr+state%3Aclosed+author%3Amike-dc-ca)
2. Michael's Issues are [here](https://github.com/sarahW05/Project1/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3Amike-dc-ca)

#### What was your role / which stories did you work on
Michael primarily worked on the view, using Jetpack Compose to setup several screens

+ What was the biggest challenge?
  + I think the largest challenge was keeping the app working without github or android studio breaking it
+ Why was it a challenge?
  + Because it often tried very hard to
+ Favorite / most interesting part of this project
  + My favorite part was learning that I can use jetpack compose instead of xml for android
+ What is the most valuable thing you learned?
  + The most valuable thing I learned was to make sure to learn your tools ASAP and go to office hours if needed

### Tony Tan
1. Tony's Pull Requests are [here](https://github.com/sarahW05/Project1/pulls?q=is%3Apr+state%3Aclosed+author%3ANotaCatgirl)
2. Tony's Issues are [here](https://github.com/sarahW05/Project1/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3ANotaCatgirl)

#### What was your role / which stories did you work on
Tony worked on Finding the API, the API integration, dashboard sample word showcase, search functionality, and the definition page.

+ What was the biggest challenge?
  + The largest challenge was learning to use Kotlin and Android Studio and making sure that it worked as intended.
+ Why was it a challenge?
  + Because Kotlin was a new language
+ Favorite / most interesting part of this project
  + My favorite part was that we mostly coordinated well as a team and made decent progress without meaningful conflicts.
+ What is the most valuable thing you learned?
  + The most valuable thing I learned was making sure that your machine can run the IDE that you are using, since my main working laptop could not run the device emulator in Android Studio, and I had to get another one that could.

## Conclusion
- How successful was the project?
  - Think in terms of what did you set out to do and what actually got done?
    - We believe the project was mostly successful, many of the cut features would be relatively easy to add, and were just cut for either time or due to setbacks from technical issues 
- What was the largest victory?
  - Getting everything working mostly how we wanted.
- Final assessment of the project
  - The project was fun and challenging due to it being all new language/syntax
