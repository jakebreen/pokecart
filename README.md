![header](https://github.com/Jakebreen/pokecart/blob/master/images/heading-image.png)

# Overview
Pokecart is my spin on the typical Poke-dex app. Featuring a complete list of the first 251 pokemon filterable by their primary and secondary types and each of their four stats.
A user can apply chosen filters to quickly find the pokemon they are looking for and add them to their shopping basket. Before purchase the user can review their order detailing the number of pokemon they are about to purchase and the total expenditure as well as *"poketax"* applied on top.

This project acts as my first introduction to LiveData, Coroutines and Koin. Working on this project has proven to be an insight into building an application without the usual dependencies on Dagger and RxJava that I regularly find myself accustomed to implementing.

The core technologies featured:
* Room
* DataBinding
* LiveData
* Coroutines
* Koin

![listview](https://github.com/Jakebreen/pokecart/blob/master/images/listview.gif) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ![filter](https://github.com/Jakebreen/pokecart/blob/master/images/filter.gif) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ![cart](https://github.com/Jakebreen/pokecart/blob/master/images/cart.gif)

# Architecture
The 251 pokemon are supplied via api and added to the database upon the first-start of the application with access to the persistence layer abstracted through repositories.
Each view is DataBound following the MVVM pattern utilising LiveData to provide a reactive stream directly from repository to view, and vice-versa.
The three core features, Shop, Filter and Cart and denoted with their own scopes with each of their modules bridging the repository to the view within scope.

Each viewmodel and repository has subsequent unit tests driving the overall development of the project with testing-in-mind!

![architecture](https://github.com/Jakebreen/pokecart/blob/master/images/architecture.jpg)
