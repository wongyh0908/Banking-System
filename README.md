# Banking Management System

A Java console-based banking management application that supports multi-user operations and local JSON data storage.

## Features
- **Account Operations**: Creation and handling of Savings and Current accounts with balance tracking and basic input validations.
- **Transaction Handling**: Deposit, withdrawal, and account status inquiries via terminal menus.
- **Data Persistence**: Uses Google GSON to serialize and parse user records and account balances into local JSON files (`accounts.json`, `bank_accounts.json`, `users.json`).
- **User Authentication**: Standard user login and administrative menu navigation.

## Tech Stack
- **Language**: Java
- **Library**: Google GSON 2.11.0 (`gson-2.11.0.jar`)
- **Storage**: JSON files
- **Environment**: Visual Studio Code, Git

## Project Structure
```text
Banking-System/
├── Eugene/
│   └── Customer.java
├── Samuel/
│   ├── Transaction.java
│   └── TransactionManager.java
├── WongJunHoe/
│   ├── AdminMenu.java
│   ├── Main.java
│   └── ReportManager.java
├── WongYiHoe/
│   ├── Account.java
│   ├── CurrentAccount.java
│   └── SavingsAccount.java
├── AccountDetails.java
├── BankAccountRecord.java
├── FileManager.java
├── FirstTimeSetup.java
├── Login.java
├── MainMenu.java
├── Register.java
├── RegisterValidation.java
├── TerminalUI.java
├── UserCredentials.java
├── accounts.json
├── bank_accounts.json
├── users.json
├── gson-2.11.0.jar
└── README.md
```
## Getting Started

## Prerequisites:

- Java Development Kit (JDK 17 or higher)

- Git

## Build and Run:

Clone the repository:

```
git clone https://github.com/wongyh0908/Banking-System.git

cd Banking-System
```
Compile source files:

Windows:

```
javac -cp ".;gson-2.11.0.jar" *.java */*.java
```
Linux / macOS:
```
javac -cp ".:gson-2.11.0.jar" *.java */*.java
```
Run the application:

Windows:
```
java -cp ".;gson-2.11.0.jar" MainMenu
```
Linux / macOS:
```
java -cp ".:gson-2.11.0.jar" MainMenu
```
