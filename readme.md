# Kontomatik Challenge
The featured app provides a list of accounts and their respective balances held by the user in PKO BP bank.
## Setup
The project needs a file with valid login credentials for the tests to run properly. The account needs to have at least one banking account connected. It is assumed that such file is named **credentials.txt** and is placed in the project's base directory. Login should be placed in the first line of the file and the password in the second one.
Core functionality of printing the list of accounts implemented in **Application.main** relies on Standard Input to deliver login credentials (login first as well).
##  System dependencies
The project works on top of **Java 17** with **Maven** and also needs an **Internet connection**.
Other used libraries are described in Maven's **pom.xml**.