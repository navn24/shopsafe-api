# Shop Safe API

The Shop Safe API is the backend API for the Shop Safe android app (see a detailed description of the project in the app github below). It was developed using Java Spring Boot, and it serves the app from an docker cluster in the cloud. 

Devops practices were -ahem-'attempted' - this was my first real try at implementing pipelines. Some sensitive information has been redacted, but most of the original content is there, enjoy!

See a detailed description of the android app and its uses on github [here](https://github.com/navn24/ShopSafe_AndroidApp_Public) and download the app on the play store [here](https://play.google.com/store/apps/details?id=com.navn.safeshop)

## Features
- Manages client signup and login with password validation, and google OAuth validation
- Allows users to change their profile settings and coordinates session info
- Stores numeric review information for 3 categories for each business 
- Stores comment based review information linked to each user for each location, and allows users to modify comments later
- Suggest most related locations to users based on their device location
- Implements an algorithm to dynamically add unknown locations to the database if user selects so, and notifies the user accordingly

## Architecture
This project makes use of the client-server-database architecture in a containerized context. 

Containerization was chosen (because its more fun!) due to the ease of dependency and environment management, to allow the minimization of resources by only spinning up the service when requests are made, and to allow the service to run on a container cluster. 

In this project, the client is the android app, the server (containing both private and public endpoints) is this API, and the database is a mysql database hosted in the cloud. 
