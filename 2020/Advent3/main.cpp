#include <iostream>
#include <fstream>

long TreeEncounters(int right, int down) {
  int toboggan_col = 0; // Range: 0-30 inclusive

  std::ifstream in("input.txt");
  char row[32];

  int tree_encounters = 0;
  for (int i = 0; i < 323; i += down) {
    in >> row;

    if (row[toboggan_col] == '#')
      tree_encounters++;

    in.seekg((down - 1) * 32, std::ios::cur);

    toboggan_col += right;
    if (toboggan_col > 30)
      toboggan_col -= 31;
  }

  return tree_encounters;
}

int main() {
  std::cout << TreeEncounters(1, 2) << '\n';
  std::cout << TreeEncounters(1, 1) * TreeEncounters(3, 1) * TreeEncounters(5, 1)
      * TreeEncounters(7, 1) * TreeEncounters(1, 2);

  return 0;
}
