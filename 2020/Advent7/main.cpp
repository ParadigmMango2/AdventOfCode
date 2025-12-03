#include <iostream>
#include <fstream>
#include <array>
#include <vector>

struct Bag {
  std::string val;
  std::vector<std::pair<std::string, int>> contains_vals;

  Bag() = default;

  Bag(std::string val) {
    Bag::val = val;
  }

  void EmplaceBack(std::string val, int num) {
    contains_vals.emplace_back(val, num);
  }
};

std::vector<std::string> GetWords(std::string str) {
  std::vector<std::string> words;

  std::string word("");
  for (char c : str) {
    if (c != ' ') {
      word += c;
    } else {
      words.emplace_back(word);
      word = "";
    }
  }

  return words;
}

std::array<Bag, 594> ParseInput() {
  std::ifstream in("input.txt");

  std::array<Bag, 594> arr;

  for (int i = 0; i < 594; i++) {
    char line[150];
    in.getline(line, 150, '\n');

    std::vector<std::string> words = GetWords(std::string(line));

    arr[i] = {words[0] + " " + words[1]};

    for (int j = 7; j <= words.size(); j += 4)
      arr[i].EmplaceBack(words[j - 2] + " " + words[j - 1], std::stoi(words[j - 3]));
  }

  return arr;
}

bool EventuallyContains(const Bag &bag, const std::vector<std::string> &eventually_contain) {
  for (const std::pair<std::string, int> &pair : bag.contains_vals)
    for (const std::string &val : eventually_contain)
      if (pair.first == val)
        return true;

  return false;
}

bool Contains(const std::string &val, const std::vector<std::string> &arr) {
  for (const std::string &val_2 : arr)
    if (val == val_2)
      return true;

  return false;
}

int CountContains(const std::array<Bag, 594> &in) {
  int count = 0;

  std::vector<std::string> eventually_contain = {"shiny gold"};

  for (int i = 0; i < 594; ++i) {
    for (const Bag &bag : in) {
      if (!Contains(bag.val, eventually_contain) && EventuallyContains(bag, eventually_contain)) {
        ++count;
        eventually_contain.emplace_back(bag.val);
      }
    }
  }

  return count;
}

const Bag &GetBag(const std::string &key, const std::array<Bag, 594> &in) {
  for (int i = 0; i < 594; ++i)
    if (in[i].val == key)
      return in[i];
}

int CountContainedBy(const Bag &root, const std::array<Bag, 594> &in) {
  if (root.contains_vals.size() == 0)
    return 0;

  int count = 0;

  for (const std::pair<std::string, int> &pair : root.contains_vals) {
    count += pair.second;
    count += pair.second * CountContainedBy(GetBag(pair.first, in), in);
  }

  return count;
}

int main() {
  std::array<Bag, 594> in = ParseInput();

  std::cout << CountContainedBy(GetBag("shiny gold", in), in);

  return 0;
}
