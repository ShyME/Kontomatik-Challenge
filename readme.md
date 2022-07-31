# Kontomatik Challenge
The featured app supplies its' user with list of accounts, bound to the user whose login credentials are given, held in PKO BP bank and their respective balances.
## Setup
The project needs a file with valid login credentials for the tests to run properly. It is assumed that such file is named **loginCredentials.txt** and is placed in the project's base directory. Login should be placed in the first line of the file and the password in the second one.
Exemplary use of the app provided in **Application.main** relies on Standard Input to deliver login credentials (login first as well).
##  System dependencies
The project works on top of **Java 17** with **Maven** and also needs an **Internet connection**.
Other used libraries are described in Maven's **pom.xml**.