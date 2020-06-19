# Cryptocurrency

This application provides a comparative view of cryptocurrency values using [Cryptocompare API](https://min-api.cryptocompare.com/)

## **Description of functionality**

The first screen contains all the cryptocurrencies supported by the API.

Each crypto value info in the list contain:
* name
* symbol
* image
<img src="https://github.com/miloscabrilo/Cryptocurrency/blob/master/first_screen.jpg" width="200">

When cryptocurrency is selected from the list, details for the selected currency are shown in two tabs.

*General info*
<img src="https://github.com/miloscabrilo/Cryptocurrency/blob/master/general_info.jpg" width="200">

*Graph view*

Graph view tab consists of three graphs. 
* First graph shows historical cryptocurency value by day in the last X days, where X can be choosen by user.
Implemented time frames: one day, one week, two weeks, one month. 
<img src="https://github.com/miloscabrilo/Cryptocurrency/blob/master/graph_view1.jpg" width="200">

* Second graph shows historical cryptocurency value by hour in the last X days, where X can be choosen by user.
Implemented time frames: one day, three days, one week. 
<img src="https://github.com/miloscabrilo/Cryptocurrency/blob/master/graph_view2.jpg" width="200">

* Third graph shows historical cryptocurency value by minute in the last X days, where X can be choosen by user.
Implemented time frames: one hour, three hours, one day. 
<img src="https://github.com/miloscabrilo/Cryptocurrency/blob/master/graph_view3.jpg" width="200">


Graph view is created without using third-party libraries.

Also, the application provides the ability to compare multiple cryptocurrencies on the same graph. (ADD and DELETE)

The data is stored in the local SQLite database each time the application is launched again.
In case when users don't have an internet access, tha data is read from the database.
