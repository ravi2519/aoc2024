import requests
import argparse

# Function to fetch the Advent of Code input
# python fetch_inputs.py 2024 1 "<your_session_cookie_here>" --output "./inputs/day01.txt"
def fetch_advent_of_code_input(year: int, day: int, session_cookie: str, output_file: str = None):
    url = f"https://adventofcode.com/{year}/day/{day}/input"
    headers = {
        "Cookie": f"session={session_cookie}"
    }

    response = requests.get(url, headers=headers)

    # Check if the response is successful
    if response.status_code == 200:
        print("Input fetched successfully!")
        if output_file:
            with open(output_file, "w") as f:
                f.write(response.text)
            print(f"Input saved to {output_file}")
        else:
            print(response.text)
    else:
        print(f"Failed to fetch input: {response.status_code} - {response.reason}")

# Main block to parse arguments and execute the function
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Download Advent of Code input")
    parser.add_argument("year", type=int, help="Year of the Advent of Code challenge")
    parser.add_argument("day", type=int, help="Day of the Advent of Code challenge")
    parser.add_argument("session_cookie", type=str, help="Your session cookie for Advent of Code")
    parser.add_argument("--output", type=str, help="File to save the input (optional)")

    args = parser.parse_args()

    fetch_advent_of_code_input(args.year, args.day, args.session_cookie, args.output)
