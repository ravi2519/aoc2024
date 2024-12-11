import os

template = """package in.ravir.{day};

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class {day_class} {{

    @Setter
    private List<String> inputLines;

    public void part1() {{
        log.info("{day_class}, Part 01");
    }}

    public void part2() {{
        log.info("{day_class}, Part 02");
    }}
}}
"""

for i in range(1, 26):
    day = f"day{i:02d}"
    day_class = f"Day{i:02d}"
    content = template.format(day=day, day_class=day_class)
    file_path = f"src/main/java/in/ravir/{day}/{day_class}.java"

    os.makedirs(os.path.dirname(file_path), exist_ok=True)

    with open(file_path, "w") as file:
        file.write(content)

print("Java file templates created successfully.")