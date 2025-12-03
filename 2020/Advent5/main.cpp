#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

std::vector<int> GetBoardingPassIds() {
  std::vector<int> boarding_pass_ids;
  boarding_pass_ids.reserve(743);

  std::ifstream in("input.txt");

  while (!in.eof()) {
    char raw_pass[10];
    in >> raw_pass;

    int row = 0, col = 0;

    for (int y = 64, i = 0; i < 7; y /= 2, i++)
      if (raw_pass[i] == 'B')
        row += y;

    for (int x = 4, i = 7; i < 10; x /= 2, i++)
      if (raw_pass[i] == 'R')
        col += x;

    boarding_pass_ids.emplace_back(row * 8 + col);
  }

  return boarding_pass_ids;
}

int main() {
  std::vector<int> boarding_passes = GetBoardingPassIds();

  std::sort(boarding_passes.begin(), boarding_passes.end());

  int previous_id = boarding_passes[0];
  for (int i = 1; i < boarding_passes.size(); i++) {
    if (previous_id + 2 == boarding_passes[i]) {
      previous_id += 1;
      break;
    }

    previous_id = boarding_passes[i];
  }

  std::cout << previous_id;

  return 0;
}
