# Hotel Database
Implemented a simple hotel database where you can do simple querying operations as an administrative user or guest.

## Installation

clone the repo

```sh
https://github.com/alexchsieh/Hotel-Database.git
```

## Tools

install postgres

```sh
sudo apt install postgresql
```

## Usage example

change into directory to run scripts

```sh
cd Hotel-Database/Project/sql/scripts
```

starting the postgres server

```sh
source startPostgreSQL.sh 
```

creating the postgres db

```sh
source createPostgreDB.sh
```

runs script that initializes a local postgres db with table creation, index setup, and data loading

```sh
source create_db.sh
```

change into directory to run program

```sh
cd Hotel-Database/Project/java/scripts
```

run the program

```sh
source compile.sh
```

## Available Queries

```sh
MAIN MENU
---------
1. View Hotels within 30 units
2. View Rooms
3. Book a Room
4. View recent booking history
5. Update Room Information
6. View 5 recent Room Updates Info
7. View booking history of the hotel
8. View 5 regular Customers
9. Place room repair Request to a company
10. View room repair Requests history
```

end the server when finished

```sh
cd Hotel-Database/Project/sql/scripts
source stopPostgreSQL.sh
```


## Disclaimer
you cannot use initdb and pg_ctl as root user, so use a unprivileged user or don't use sudo

## Contributing

1. Fork it (<https://github.com/alexchsieh/Hotel-Database/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
