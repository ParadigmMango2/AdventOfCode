#include <iostream>
#include <fstream>
#include <vector>

bool Contains(const std::string &str, const char &key) {
  for (const char &ch : str)
    if (ch == key)
      return true;

  return false;
}

std::vector<std::vector<std::string>> ParseInput() {
  std::ifstream in("input.txt");

  std::vector<std::vector<std::string>> groups;

  while (!in.eof()) {
    std::vector<std::string> group;

    while (in.peek() != '\n' && !in.eof()) {
      char line[27];
      in.getline(line, 200, '\n');

      group.emplace_back(line);
    }

    in.get();

    groups.emplace_back(group);
  }

  return groups;
}

int main() {
  int question_sum = 0;

  std::vector<std::vector<std::string>> groups = ParseInput();

  for (std::vector<std::string> &group : groups) {
    int answer_sum = 0;

    for (char letter = 'a'; letter <= 'z'; letter++) {
      bool contains = true;

      for (const std::string &person : group)
        if (!Contains(person, letter))
          contains = false;

      answer_sum += contains;
    }


    question_sum += answer_sum;
  }

  std::cout << question_sum;

  return 0;
}
