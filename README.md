
# AOC 2024
AOC 2024 Challenge

## Python Scripts in this Repo

### create_java_templates.py
```sh
python src/main/resources/create_java_templates.py
```
It creates Java template files. The Java files will be created in the `resources` directory and won't override the actual ones.

### fetch_inputs.py
```sh
python src/main/resources/fetch_inputs.py 2024 1 "<your_session_cookie_here>" --output "./inputs/day01.txt"
```
This script fetches the input for the specified day from the Advent of Code website. You need to supply your "session" cookie from a logged-in session.

### create_empty_input_files.py
```sh
python src/main/resources/create_empty_input_files.py
```
A simple script to create empty input files.

## Running
The main class intelligently picks the current day of the AOC 2024 challenge and runs the code for you. At other times, it will run all the challenges.
```sh
mvn clean compile exec:java
```