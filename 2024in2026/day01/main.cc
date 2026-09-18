#include <algorithm>
#include <cmath>
#include <fstream>
#include <iostream>
#include <string>
#include <unordered_map>
#include <utility>
#include <vector>

#include <scn/scan.h>


std::pair<std::vector<int>, std::vector<int>> ParsePart1() {
  std::ifstream file("input/real.txt");
  std::vector<int> list_a;
  std::vector<int> list_b;

  std::string line;
  while (std::getline(file, line)) {
    auto r = scn::scan<int, int>(line, "{}   {}");
    if (r) {
      auto [a, b] = r->values();
      list_a.push_back(a);
      list_b.push_back(b);
    }
  }

  return std::make_pair(list_a, list_b);
}

void Part1() {
  auto [list_a, list_b] = ParsePart1();

  std::sort(list_a.begin(), list_a.end());
  std::sort(list_b.begin(), list_b.end());

  // for (int& i : list_a) std::cout << i << std::endl;
  // for (int& i : list_b) std::cout << i << std::endl;

  long total_dif = 0;

  for (int i = 0; i < list_a.size(); i++) {
    int dif = std::abs(list_a[i] - list_b[i]);
    total_dif += dif;
  }

  std::cout << "Part 1: " << total_dif << std::endl;
}


void Part2() {
  auto [list_a, list_b] = ParsePart1();

  // for (int& i : list_a) std::cout << i << std::endl;
  // for (int& i : list_b) std::cout << i << std::endl;

  std::sort(list_b.begin(), list_b.end());
  std::unordered_map<int, int> list_b_frequencies; // key: list b item -> value: frequency

  int list_b_idx = 0;
  while (list_b_idx < list_b.size()) {
    int item = list_b[list_b_idx];

    int frequency = 0;

    while (list_b_idx < list_b.size() && item == list_b[list_b_idx]) {
      frequency++;
      list_b_idx++;
    }

    // std::cout << "Key: " << item << ", Value: " << frequency << "\n";
    list_b_frequencies[item] = frequency;

    // list_b_idx++;
  }

  // for (const auto& pair : list_b_frequencies) {
  //   std::cout << "Key: " << pair.first << ", Value: " << pair.second << "\n";
  // }

  long total_similarity = 0;
  for (int& num: list_a) {
    if (list_b_frequencies.count(num) > 0) {
      long similarity = num * list_b_frequencies[num];
      total_similarity += similarity;
    }
  }
  
  std::cout << "Part 2: " << total_similarity << std::endl;
}


int main() {
  Part1();
  Part2();

  return 0;
}
